package com.ipfaffen.ovenbird.spring.context;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

/**
 * @author Isaias Pfaffenseller
 */
@Component
public class Messages implements MessageSourceAware {

	private static MessageSource messageSource;

	/**
	 * @param key
	 * @param args - can be i18n keys or not.
	 * @return
	 */
	public static String get(String key, Object... args) {
		if(key == null) {
			return null;
		}
		try {
			return messageSource.getMessage(String.valueOf(key), args, Locale.US);
		}
		catch(Exception e) {
			return String.format(key, args);
		}
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		Messages.messageSource = messageSource;
	}
}