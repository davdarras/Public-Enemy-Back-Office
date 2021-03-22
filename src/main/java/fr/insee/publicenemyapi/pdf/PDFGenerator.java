package fr.insee.publicenemyapi.pdf;

import java.io.File;
import java.io.InputStream;
import fr.insee.publicenemyapi.exceptions.PublicEnemyException;


/**
 * Interface for implementing a PDF file generator
 * This generator will be able to generate (or convert) a {@link File} to another {@link File} that will be a PDF file
 */
public interface PDFGenerator {

	/***
	 * Initialize the {@link PDFGenerator}
	 * 
	 * @throws PublicEnemyException if an error occurs during the {@link PDFGenerator} initialization
	 */
	void initializeGenerator() throws PublicEnemyException;

	/**
	 * Generate a PDF file from another {@link File}
	 * @param fo the {@link File} to convert
	 * @throws PublicEnemyException if an error occurs during the PDF file generation
	 */
	String generatePDF(InputStream model, String modelFileName) throws PublicEnemyException;
	



}
