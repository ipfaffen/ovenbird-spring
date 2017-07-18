package com.ipfaffen.ovenbird.spring.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.ipfaffen.ovenbird.commons.exception.ValidationException;
import com.ipfaffen.ovenbird.spring.bean.ResponseHelper;
import com.ipfaffen.ovenbird.spring.bean.ResponseObject;
import com.ipfaffen.ovenbird.spring.context.Messages;
import com.ipfaffen.ovenbird.spring.context.Validations;
import com.ipfaffen.ovenbird.spring.exception.JsonException;
import com.ipfaffen.ovenbird.spring.logging.Log;

/**
 * @author Isaias Pfaffenseller
 */
public class GlobalController {

	@Autowired
	protected ApplicationContext applicationContext;

	@Autowired
	protected ServletContext servletContext;

	/**
	 * @param key
	 * @param args
	 * @return
	 */
	public String message(String key, Object... args) {
		return Messages.get(key, args);
	}

	/**
	 * @return
	 */
	public Validations validation() {
		return applicationContext.getBean(Validations.class);
	}

	/**
	 * @param identifier
	 * @param value
	 * @return
	 */
	public Validations field(String identifier, Object value) {
		return validation().field(identifier, value);
	}

	/**
	 * @param identifier
	 * @param message
	 * @param args
	 * @return
	 */
	public Validations addError(String identifier, String message, Object... args) {
		return validation().add(identifier, Messages.get(message, args));
	}

	/**
	 * @param identifier
	 * @return
	 */
	public boolean hasError(String identifier) {
		return validation().hasError(identifier);
	}

	/**
	 * @return
	 */
	public boolean hasErrors() {
		return validation().hasErrors();
	}

	/**
	 * @return
	 */
	public ResponseHelper success() {
		return success(false);
	}

	/**
	 * @param genericMessage
	 * @return
	 */
	public ResponseHelper success(Boolean genericMessage) {
		if(BooleanUtils.isTrue(genericMessage)) {
			return success("success.generic");
		}
		return new ResponseHelper();
	}

	/**
	 * @param object
	 * @return
	 */
	public ResponseHelper success(ResponseObject object) {
		return success(object, false);
	}

	/**
	 * @param object
	 * @param genericMessage
	 * @return
	 */
	public ResponseHelper success(ResponseObject object, Boolean genericMessage) {
		if(BooleanUtils.isTrue(genericMessage)) {
			return success(object, "success.generic");
		}
		return new ResponseHelper(object.getObject());
	}

	/**
	 * @param message
	 * @param args
	 * @return
	 */
	public ResponseHelper success(String message, Object... args) {
		return new ResponseHelper(Messages.get(message, args));
	}

	/**
	 * @param object
	 * @param message
	 * @param args
	 * @return
	 */
	public ResponseHelper success(ResponseObject object, String message, Object... args) {
		return new ResponseHelper(Messages.get(message, args), object.getObject());
	}

	/**
	 * @param exception
	 * @return
	 */
	public Object fail(Exception exception) {
		return fail(exception, true);
	}

	/**
	 * @param exception
	 * @param translate
	 * @return
	 */
	public Object fail(Exception exception, boolean translate) {
		ResponseHelper helper;

		// Validation exception:
		if(exception instanceof ValidationException) {
			ValidationException e = (ValidationException) exception;
			helper = new ResponseHelper(translate ? Messages.get(e.getMessage(), e.getArgs()) : e.getMessage());
			helper.setDetail(translate ? Messages.get(e.getDetail()) : e.getDetail());
			helper.setObject(e.getObject());

			for(String fieldIdentifier: validation().getFieldErrors().keySet()) {
				helper.addField(fieldIdentifier, validation().getFieldErrors().get(fieldIdentifier));
			}
			throwJson(helper);
		}

		// Rest client exception:
		if(exception instanceof RestClientException) {
			try {
				helper = new ObjectMapper().readValue(exception.getMessage(), ResponseHelper.class);
				if(translate) {
					helper.setMessage(message(helper.getMessage()));
				}
			}
			catch(Exception e) {
				helper = new ResponseHelper(translate ? Messages.get("error.generic") : "error.generic");
			}
			throwJson(helper);
		}
		
		// Log unexpected exception.
		log(exception.getMessage(), exception);

		// Another exception:
		helper = new ResponseHelper(translate ? Messages.get("error.generic") : "error.generic");
		throwJson(helper, exception);

		// Unreachable return.
		return null;
	}

	/**
	 * @param helper
	 * @throws JsonException
	 */
	public void throwJson(ResponseHelper helper) throws JsonException {
		throwJson(helper, null);
	}

	/**
	 * @param helper
	 * @param throwable
	 * @throws JsonException
	 */
	public void throwJson(ResponseHelper helper, Throwable throwable) throws JsonException {
		throw new JsonException(helper, throwable);
	}

	/**
	 * @throws ValidationException
	 */
	public void throwValidation() throws ValidationException {
		throwValidation("error.correctErrors");
	}

	/**
	 * @param message
	 * @param args
	 * @throws ValidationException
	 */
	public void throwValidation(String message, Object... args) throws ValidationException {
		throw new ValidationException(message, args);
	}

	/**
	 * @param message
	 * @throws RuntimeException
	 */
	public void throwRuntime(String message) throws RuntimeException {
		throw new RuntimeException(message);
	}

	/**
	 * @param throwable
	 * @throws RuntimeException
	 */
	public void throwRuntime(Throwable throwable) throws RuntimeException {
		throw new RuntimeException(throwable);
	}
	
	/**
	 * @param clazz
	 * @throws RuntimeException
	 */
	public void throwIllegal(Class<?> clazz) throws RuntimeException {
		throw new IllegalAccessError(clazz.getName());
	}

	/**
	 * @param filePath
	 * @return
	 * @throws RuntimeException
	 */
	public String loadResourceFile(String filePath) throws RuntimeException {
		try {
			return IOUtils.toString(servletContext.getResourceAsStream(filePath), StandardCharsets.UTF_8);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param message
	 */
	public void log(String message) {
		Log.info(message);
	}

	/**
	 * @param message
	 * @param throwable
	 */
	public void log(String message, Throwable throwable) {
		Log.error(message, throwable);
	}

	/**
	 * @param ra
	 */
	public void flashFields(RedirectAttributes ra) {
		if(validation().hasErrors()) {
			// Add field errors if has one.
			ra.addFlashAttribute("fieldError", validation().getFieldErrors());
		}

		// Add all field values.
		for(String fieldName: validation().getFieldValues().keySet()) {
			ra.addFlashAttribute(fieldName, validation().getFieldValues().get(fieldName));
		}
	}

	/**
	 * @param target - Object to be serialized.
	 * @return
	 */
	public String toJson(Object target) {
		try {
			return new ObjectMapper().writeValueAsString(target);
		} 
		catch(JsonProcessingException e) {
			return null;
		}
	}
	
	/**
	 * @param json
	 * @param clazz
	 * @return
	 */
	public <T> T fromJson(String json, Class<T> clazz) {
		if(StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return new ObjectMapper().readValue(json, clazz);
		} 
		catch(IOException e) {
			log(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * @param json
	 * @param clazz
	 * @return
	 */
	public <T> List<T> fromJsonAsList(String json, Class<T> clazz) {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeFactory typeFactory = objectMapper.getTypeFactory();			
		try {
			return objectMapper.readValue(json, typeFactory.constructCollectionType(List.class, clazz));
		} 
		catch(IOException e) {
			log(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * @param value
	 * @return
	 */
	public String handleBlank(String value) {
		return (StringUtils.isBlank(value) ? null : value.trim());
	}
	
	/**
	 * @param value
	 * @return
	 */
	public Integer parseInteger(String value) {
		return (StringUtils.isBlank(value) ? null : Integer.valueOf(value));
	}

	/**
	 * @param value
	 * @return
	 */
	public Long parseLong(String value) {
		return (StringUtils.isBlank(value) ? null : Long.valueOf(value));
	}
}