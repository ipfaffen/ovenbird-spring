package com.ipfaffen.ovenbird.spring.controller.advice;

import java.io.IOException;
import java.net.SocketException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ipfaffen.ovenbird.commons.HttpUtil;
import com.ipfaffen.ovenbird.spring.exception.JsonException;
import com.ipfaffen.ovenbird.spring.logging.Log;

/**
 * @author Isaias Pfaffenseller
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public String defaultExceptionHandler(Exception exception, HttpServletRequest request) {
		boolean printStackTrace = !(exception.getCause() instanceof SocketException || exception.getCause() instanceof IOException);
		logException(exception, request, printStackTrace);
		if(HttpUtil.isAjax(request)) {
			// If it is an Ajax request then just return null.
			// Keep in mind that the Ajax verification is based on request header "X-Requested-With".
			return null;
		}
		// If it is a regular request then redirect to 500 error page.
		return "redirect:/error/500.do";
	}

	/**
	 * @param exception
	 * @param request
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(JsonException.class)
	@ResponseBody
	public Object jsonExceptionHandler(JsonException exception, HttpServletRequest request) {
		if(exception.getCause() != null) {
			// Only log exception if there is a cause, otherwise it is not necessary.
			logException(exception.getCause(), request, true);
		}
		return exception.getHelper();
	}

	/**
	 * @param throwable
	 * @param request
	 * @param printStrackTrace
	 */
	private void logException(Throwable throwable, HttpServletRequest request, boolean printStrackTrace) {
		Log.error(String.format("%s: %s: %s", request.getRemoteAddr(), request.getRequestURL().toString(), throwable.getMessage()));
		if(printStrackTrace) {
			throwable.printStackTrace();
		}
	}
}