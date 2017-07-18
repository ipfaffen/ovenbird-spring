package com.ipfaffen.ovenbird.spring.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Isaias Pfaffenseller
 */
public class Log {
	
	private static Logger logger = LoggerFactory.getLogger(Log.class);
	
	/**
	 * @param message
	 */
	public static void info(String message) {
		logger.info(message);
	}
	
	/**
	 * @param message
	 */
	public static void warn(String message) {
		logger.warn(message);
	}

	/**
	 * @param message
	 */
	public static void error(String message) {
		logger.error(message);
	}
	
	/**
	 * @param message
	 * @param throwable
	 */
	public static void error(String message, Throwable throwable) {
		logger.error(message, throwable);
	}
}
