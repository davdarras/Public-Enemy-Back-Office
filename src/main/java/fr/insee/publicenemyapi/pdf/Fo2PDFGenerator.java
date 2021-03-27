package fr.insee.publicenemyapi.pdf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.area.AreaTreeModel;
import org.apache.fop.area.AreaTreeParser;
import org.apache.fop.area.RenderPagesModel;
import org.apache.fop.fonts.FontInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import fr.insee.publicenemyapi.exceptions.PublicEnemyException;
import fr.insee.publicenemyapi.util.xsl.XSLTransform;

public class Fo2PDFGenerator implements PDFGenerator {

	private static final Logger logger = LoggerFactory.getLogger(Fo2PDFGenerator.class);

	private FopFactory fopFactory = null;

	@Override
	public void initializeGenerator() throws PublicEnemyException {

		// Step 1: Construct a FopFactory by specifying a reference to the
		// configuration file
		// (reuse if you plan to render multiple documents!)

		logger.info("initializeGenerator (Fo2PDFGenerator)");

		// Used to resolve image Paths (cf. fop.xconf in /src/main/resources )
		URL foImagesPathDirectory = this.getClass().getClassLoader().getResource("img");

		Path pathImg = Paths.get(URI.create(foImagesPathDirectory.toString()));

		// Used to load fonts (cf. fop.xconf in /src/main/resources )
		InputStream isFOPConfigTemplate = this.getClass().getClassLoader().getResourceAsStream("fo/fop.xconf");

		try {
			fopFactory = FopFactory.newInstance(pathImg.toUri(), isFOPConfigTemplate);
		} catch (SAXException | IOException e) {
			throw new PublicEnemyException("Problem while getting FopFactory newInstance", e);
		}
	}

	@Override
	public String generatePDF(InputStream model, String modelFileName) throws PublicEnemyException {

		logger.info("Generating PDF from " + modelFileName);
		final String XSLT_FILE = "xsl/barcode-specific-treatment.xsl";

		String outputFile = null;
		try {
			// Step 2: Set up output stream.
			// Note: Using BufferedOutputStream for performance reasons
			//File dir = new File("D://FileIO");
			File outFile = File.createTempFile("temp", "out.xml");
			// new File(String.format("%s/out.xml", Constants.TEMP_FOLDER_PDF_PATH));
			OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));

			// Step 3: Construct fop with desired xml output format :
			// application/X-fop-areatree
			Fop fop = fopFactory.newFop(MimeConstants.MIME_FOP_AREA_TREE, out);

			// Step 4: Setup JAXP using identity transformer
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(); // identity
																// transformer

			// Step 5: Setup input and output for XSLT transformation
			// Setup input stream
			Source src = new StreamSource(model);
			// Resulting SAX events (the generated FO) must be piped through
			// to FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Step 6: Start XSLT transformation and FOP processing
			transformer.transform(src, res);

			// Clean-up
			out.close();

			InputStream isIn = new FileInputStream(outFile);

			// Set up output stream.
			// Note: Using BufferedOutputStream for performance reasons
			File outXMLFile = File.createTempFile("temp", "out-update.xml");
			// new File(String.format("%s/out-update.xml", Constants.TEMP_FOLDER_PDF_PATH));
			OutputStream osOut = new BufferedOutputStream(new FileOutputStream(outXMLFile));

			// Use XSLT pipeline implementation
			XSLTransform t = new XSLTransform();
			t.transform(isIn, osOut, XSLT_FILE);

			// Clean-up
			osOut.close();

			// Set up output stream.
			// Note: Using BufferedOutputStream for performance reasons
			File outPDFFile = File.createTempFile("temp", "out.pdf");
			// new File(String.format("%s/out.pdf", Constants.TEMP_FOLDER_PDF_PATH));
			OutputStream outPDF = new BufferedOutputStream(new FileOutputStream(outPDFFile));

			// Construct fop with desired output format
			FOUserAgent userAgent = fopFactory.newFOUserAgent();

			// Setup fonts and user agent
			FontInfo fontInfo = new FontInfo();

			// Construct the AreaTreeModel that will received the individual pages
			AreaTreeModel treeModel = new RenderPagesModel(userAgent, MimeConstants.MIME_PDF, fontInfo, outPDF);

			// Parse the area tree file into the area tree
			AreaTreeParser parser = new AreaTreeParser();
			Source srcPDF = new StreamSource(outXMLFile);
			parser.parse(srcPDF, treeModel, userAgent);

			// Signal the end of the processing. The renderer can finalize the target
			// document.
			treeModel.endDocument();

			// Clean-up
			outPDF.close();
			outputFile = outPDFFile.getAbsolutePath();

		} catch (TransformerConfigurationException e) {
			throw new PublicEnemyException("Problem while fo trasnformation", e);
		} catch (FOPException e) {
			throw new PublicEnemyException("Problem while fo trasnformation", e);
		} catch (FileNotFoundException e) {
			throw new PublicEnemyException("Problem while fo trasnformation", e);
		} catch (TransformerException e) {
			throw new PublicEnemyException("Problem while fo trasnformation", e);
		} catch (IOException e) {
			throw new PublicEnemyException("Problem while fo trasnformation", e);
		} catch (Exception e) {
			throw new PublicEnemyException("Problem while fo trasnformation", e);
		}
		return outputFile;
	}

}
