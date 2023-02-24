package com.customer.management.util;

import java.util.regex.Pattern;

public class CustomerManagementValidator {
	
	public static final String EMAIL_REGEX = "^(.+)@(\\S+)$";
	
	public static boolean validateMail(String emailId) {
		return patternMatches(emailId,EMAIL_REGEX);
	}
	
	public static boolean patternMatches(String validationString, String regexPattern) {
	    return Pattern.compile(regexPattern)
	      .matcher(validationString)
	      .matches();
	}

}
