package com.fourcamp.linkbank.utils;

import com.fourcamp.linkbank.model.Client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {

	public static boolean isRandomKey(String key) {
		if (key.length() != 36 || key == null || key.equals("")) {
			return false;
		}

		String regex = "^([a-zA-Z0-9'\\\\-])+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(key);
		return matcher.matches();
	}

	public static boolean isAccountNumber(String number) {
		String regex = "^([0-9]{1,8})-([0-9]{1})$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}
}
