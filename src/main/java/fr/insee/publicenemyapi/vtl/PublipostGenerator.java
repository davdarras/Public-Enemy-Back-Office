package fr.insee.publicenemyapi.vtl;

import java.io.File;
import java.io.InputStream;

import fr.insee.publicenemyapi.exceptions.PublicEnemyException;


/**
 * Interface for implementing a Publipost generator
 * This generator will be able a personalized  {@link File} from a model {@link File} 
 */
public interface PublipostGenerator {

	
	public void initializeGenerator(InputStream model, String modelFileName) throws PublicEnemyException;
	
	public InputStream generateDocument(InputStream data, String dataFileName) throws PublicEnemyException;
	
	public void stopDocPubliPost() throws PublicEnemyException;

}
