package com.ipfaffen.ovenbird.spring.bean;

/**
 * @author Isaias Pfaffenseller
 */
public class ResponseField {

	private String identifier;
	private String message;

	/**
	 * @param identifier
	 * @param message
	 */
	public ResponseField(String identifier, String message) {
		this.identifier = identifier;
		this.message = message;
	}

	/**
	 * @return identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}