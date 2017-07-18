package com.ipfaffen.ovenbird.spring.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Isaias Pfaffenseller
 */
public class ResponseHelper {

	private String code;
	private String message;
	private String detail;
	private Object object;
	private List<ResponseField> fields;

	/**
	 * @param code
	 * @param message
	 * @param object
	 */
	public ResponseHelper(String code, String message, Object object) {
		this.code = code;
		this.message = message;
		this.object = object;
	}

	/**
	 * @param message
	 */
	public ResponseHelper(String message) {
		this(null, message, null);
	}

	/**
	 * @param object
	 */
	public ResponseHelper(Object object) {
		this(null, null, object);
	}
		
	/**
	 * @param message
	 * @param object
	 */
	public ResponseHelper(String message, Object object) {
		this(null, message, object);
	}

	public ResponseHelper() {
	}

	/**
	 * @return
	 */
	private List<ResponseField> fields() {
		if(fields == null) {
			fields = new ArrayList<ResponseField>();
		}
		return fields;
	}

	/**
	 * @param identifier
	 * @param message
	 */
	public void addField(String identifier, String message) {
		fields().add(new ResponseField(identifier, message));
	}

	/**
	 * @param identifier
	 * @param message
	 */
	public void addField(String identifier) {
		fields().add(new ResponseField(identifier, null));
	}

	/**
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
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

	/**
	 * @return
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return object
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

	/**
	 * @return fields
	 */
	public List<ResponseField> getFields() {
		return fields;
	}

	/**
	 * @param fields
	 */
	public void setFields(List<ResponseField> fields) {
		this.fields = fields;
	}
}