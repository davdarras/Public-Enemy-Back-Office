package fr.insee.publicenemyapi.exceptions;

import java.io.IOException;

@SuppressWarnings("serial")
public class PublicEnemyException extends IOException {

	private int status;
	private String details;

	/**
	 *
	 * @param message
	 * @param exception
	 */
	public PublicEnemyException(String message, Exception e) {
		super(message);
		e.printStackTrace();
		this.details = e.getLocalizedMessage();
	}

	/**
	 *
	 * @param status
	 * @param message
	 * @param details
	 */
	public PublicEnemyException(int status, String message, String details) {
		super(message);
		this.status = status;
		this.details = details;
	}

	public RestMessage toRestMessage() {
		return new RestMessage(this.status, this.getMessage(), this.details);
	}
}
