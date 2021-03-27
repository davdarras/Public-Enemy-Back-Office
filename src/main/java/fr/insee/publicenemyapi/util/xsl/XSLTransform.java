package fr.insee.publicenemyapi.util.xsl;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.springframework.stereotype.Component;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.ValidationMode;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

@Component
public class XSLTransform {
	public XSLTransform() {
		
	}

	public void transform(InputStream input, OutputStream output,String xsltFile) throws Exception {
		if (null == input) {
			throw new NullPointerException("Null input");
		}
		if (null == output) {
			throw new NullPointerException("Null output");
		}
		try {
			Processor processor = new Processor(false);
			Source source = new StreamSource(input);
			XsltTransformer t = createPipeline(source, output, processor, xsltFile);
			t.transform();
		} catch (SaxonApiException e) {
			throw new Exception(String.format("%s:%s, Line: %d, Error Code: %s", getClass().getName(), e.getMessage(),
					e.getLineNumber(), e.getErrorCode()));
		}
	}



	private XsltTransformer createPipeline(Source input, OutputStream output, Processor processor , String xsltFile)
			throws Exception {
		DocumentBuilder builder = processor.newDocumentBuilder();
		XdmNode source = builder.build(input);
		XsltTransformer t0 = createTransformer(processor, xsltFile  );
		Serializer out = createSerializer(processor, output);
		t0.setInitialContextNode(source);
		t0.setDestination(out);
		return t0;
	}

	private XsltTransformer createTransformer(Processor processor, String path) throws SaxonApiException {
		XsltCompiler compiler = processor.newXsltCompiler();
		InputStream xslResource = getClass().getClassLoader().getResourceAsStream(path);
		if (null == xslResource) {
			throw new NullPointerException("NULL XSLT Resource");
		}
		XsltExecutable xsl = compiler.compile(new StreamSource(xslResource));
		XsltTransformer transformer = xsl.load();
		transformer.setSchemaValidationMode(ValidationMode.LAX);
		return transformer;
	}

	private Serializer createSerializer(Processor processor, OutputStream output) {
		Serializer out = processor.newSerializer(output);
		out.setOutputProperty(Serializer.Property.SAXON_STYLESHEET_VERSION, "2.0");
		out.setOutputProperty(Serializer.Property.METHOD, "xml");
		out.setOutputProperty(Serializer.Property.INDENT, "yes");
		out.setOutputProperty(Serializer.Property.OMIT_XML_DECLARATION, "yes");
		out.setOutputProperty(Serializer.Property.ENCODING, "utf-8");
		return out;
	}

}
