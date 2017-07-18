package com.ipfaffen.ovenbird.spring.bean;

/**
 * @author Isaias Pfaffenseller
 */
public class ResponseObject {

	private Object object;

	public ResponseObject() {
	}
	
	/**
	 * @param object
	 */
	public ResponseObject(Object object) {
		this.object = object;
	}

	/**
	 * @return
	 */
	public Object getObject() {
		return object;
	}
	
	/**
	 * @param object
	 */
	public void setObject(Object object) {
		this.object = object;
	}
}