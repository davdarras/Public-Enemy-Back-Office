package fr.insee.publicenemyapi.vtl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.util.ReaderInputStream;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import fr.insee.publicenemyapi.exceptions.PublicEnemyException;
import fr.insee.publicenemyapi.model.Variables;

public class FOPublispostGenerator implements PublipostGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(FOPublispostGenerator.class);

	private Template foVelocityTemplate;
	
	@Override
	public void initializeGenerator(InputStream model, String modelFileName) throws PublicEnemyException {
		
		//File foFile = new File(String.format("%s/%s", Constants.TEMP_FOLDER_VTL_PATH,foDetails.getFileName()));
		File foFile = null;
	    try {
	    	foFile = File.createTempFile("temp", modelFileName);
			FileUtils.copyInputStreamToFile(model, foFile);
		} catch (IOException e) {
			throw new PublicEnemyException("Problem while coying template on File", e);
		}
		
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, foFile.getParent());
		
		ve.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		ve.setProperty("runtime.log.logsystem.log4j.category", "velocity");
		ve.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
		ve.setProperty("input.encoding", "UTF-8");
		ve.setProperty("output.encoding", "UTF-8");
		ve.init();

		this.foVelocityTemplate = ve.getTemplate(foFile.getName(), "UTF-8");
		
	}


	@Override
	public InputStream generateDocument(InputStream data, String dataFileName) throws PublicEnemyException {

	    Reader reader ;
        JAXBContext jaxbContext;
        Unmarshaller unmarshaller;
        Variables variables;
		try {
			reader = new InputStreamReader(data, "UTF-8");
			jaxbContext = JAXBContext.newInstance(Variables.class);
			
			unmarshaller = jaxbContext.createUnmarshaller();

	        variables = (Variables) unmarshaller.unmarshal(reader);

		} catch (JAXBException e) {
			throw new PublicEnemyException("Problem while XML parsing", e);
		} catch (UnsupportedEncodingException e) {
			throw new PublicEnemyException("Problem while XML parsing", e);
		}

		VelocityContext context = new VelocityContext();

        for(Object element : variables.getVariables()) {
        	
        	if(element instanceof Element) {
        		String cle = ((Element)element).getTagName();
        		String valeur = ((Element)element).getTextContent();
        		context.put(cle, valeur);
        	}
        }
	     
		StringWriter writer = new StringWriter();
		foVelocityTemplate.merge(context, writer);
		InputStream is = new ReaderInputStream(new
				StringReader(writer.toString()), "UTF-8");
		return is;
		
}
	
	
	@Override
	public void stopDocPubliPost() throws PublicEnemyException {
		// TODO Auto-generated method stub
		
	}

}
