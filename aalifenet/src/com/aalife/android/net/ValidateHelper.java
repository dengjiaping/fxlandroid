package com.aalife.android.net;


public class ValidateHelper {
	
	public static boolean validateText(String str) {
		if("".equals(str)) {			
			return true;
		}
		
		return false;
	}
	
	public static boolean validateLength(String str, int len) {
		if(str.length() < len) {
			return true;
		}
		
		return false;
	}
}
