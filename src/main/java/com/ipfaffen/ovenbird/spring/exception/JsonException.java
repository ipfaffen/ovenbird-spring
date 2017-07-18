package com.ipfaffen.ovenbird.spring.exception;

import com.ipfaffen.ovenbird.spring.bean.ResponseHelper;

/**
 * @author Isaias Pfaffenseller
 */
@SuppressWarnings("serial")
public class JsonException extends RuntimeException {

	private ResponseHelper helper;

	/**
	 * @param helper
	 * @param throwable
	 */
	public JsonException(ResponseHelper helper, Throwable throwable) {
		super(helper.getMessage(), throwable);
		this.helper = helper;
	}

	/**
	 * @param helper
	 */
	public JsonException(ResponseHelper helper) {
		this(helper, null);
	}

	/**
	 * @return
	 */
	public ResponseHelper getHelper() {
		return helper;
	}
}