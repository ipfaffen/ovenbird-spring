package com.ipfaffen.ovenbird.spring.context;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Isaias Pfaffenseller
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Validations {

	/**
	 * <field_name, field_error>
	 */
	private Map<String, String> fieldErrors;

	/**
	 * <field_name, field_value>
	 */
	private Map<String, Object> fieldValues;

	private String currentFieldName;
	private Object currentFieldValue;

	public Validations() {
		fieldErrors = new LinkedHashMap<String, String>();
		fieldValues = new LinkedHashMap<String, Object>();
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public Validations field(String name, Object value) {
		currentFieldName = name;
		currentFieldValue = value;

		fieldValues.put(currentFieldName, currentFieldValue);

		return this;
	}

	/**
	 * @return
	 */
	public Validations required() {
		return required(Messages.get("error.field.required"));
	}

	/**
	 * @param message
	 * @return
	 */
	public Validations required(String message) {
		if(!hasError(currentFieldName) && (currentFieldValue == null || StringUtils.isBlank(currentFieldValue.toString()))) {
			fieldErrors.put(currentFieldName, Messages.get(message));
		}
		return this;
	}

	/**
	 * @param minSize
	 * @return
	 */
	public Validations minSize(int minSize) {
		return minSize(minSize, Messages.get("error.field.minSize", minSize));
	}

	/**
	 * @param minSize
	 * @param message
	 * @return
	 */
	public Validations minSize(int minSize, String message) {
		if(!hasError(currentFieldName) && currentFieldValue != null && StringUtils.isNotBlank(currentFieldValue.toString()) && currentFieldValue.toString().length() < minSize) {
			fieldErrors.put(currentFieldName, Messages.get(message));
		}
		return this;
	}

	/**
	 * @param maxSize
	 * @return
	 */
	public Validations maxSize(int maxSize) {
		return maxSize(maxSize, Messages.get("error.field.maxSize", maxSize));
	}

	/**
	 * @param maxSize
	 * @param message
	 * @return
	 */
	public Validations maxSize(int maxSize, String message) {
		if(!hasError(currentFieldName)  && currentFieldValue != null && StringUtils.isNotBlank(currentFieldValue.toString()) && currentFieldValue.toString().length() > maxSize) {
			fieldErrors.put(currentFieldName, Messages.get(message));
		}
		return this;
	}

	/**
	 * @param pattern
	 * @return
	 */
	public Validations match(String pattern) {
		return match(pattern, Messages.get("error.field.match"));
	}

	/**
	 * @param pattern
	 * @param message
	 * @return
	 */
	public Validations match(String pattern, String message) {
		if(!hasError(currentFieldName)  && currentFieldValue != null && StringUtils.isNotBlank(currentFieldValue.toString()) && !currentFieldValue.toString().matches(pattern)) {
			fieldErrors.put(currentFieldName, Messages.get(message));
		}
		return this;
	}

	/**
	 * @param valueToCompare
	 * @return
	 */
	public Validations equalsTo(Object valueToCompare) {
		return equalsTo(valueToCompare, Messages.get("error.field.equals"));
	}

	/**
	 * @param valueToCompare
	 * @param message
	 * @return
	 */
	public Validations equalsTo(Object valueToCompare, String message) {
		if(!hasError(currentFieldName) && !currentFieldValue.toString().equals(valueToCompare.toString())) {
			fieldErrors.put(currentFieldName, Messages.get(message));
		}
		return this;
	}

	/**
	 * @param nameToCompare
	 * @param valueToCompare
	 * @return
	 */
	public Validations equalsTo(String nameToCompare, Object valueToCompare) {
		return equalsTo(nameToCompare, valueToCompare, Messages.get("error.field.equals"));
	}

	/**
	 * @param nameToCompare
	 * @param valueToCompare
	 * @param message
	 * @return
	 */
	public Validations equalsTo(String nameToCompare, Object valueToCompare, String message) {
		if(!hasError(currentFieldName) && !hasError(nameToCompare) && !currentFieldValue.toString().equals(valueToCompare.toString())) {
			fieldErrors.put(nameToCompare, "");
			fieldErrors.put(currentFieldName, Messages.get(message));
		}
		return this;
	}

	/**
	 * @param error
	 * @return
	 */
	public Validations add(String error) {
		return add(currentFieldName, error);
	}

	/**
	 * @param name
	 * @param error
	 * @return
	 */
	public Validations add(String name, String error) {
		if(!hasError(name)) {
			fieldErrors.put(name, error);
		}
		return this;
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public boolean hasError(String fieldName) {
		return fieldErrors.containsKey(fieldName);
	}

	/**
	 * @return
	 */
	public boolean hasErrors() {
		return !fieldErrors.isEmpty();
	}

	/**
	 * @return
	 */
	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

	/**
	 * @return
	 */
	public Map<String, Object> getFieldValues() {
		return fieldValues;
	}
}