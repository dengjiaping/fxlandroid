package com.aalife.android.net;

import java.text.DecimalFormat;

public class MyFormat {

	public MyFormat() {		
	}
	
	public static String formatDouble(String num, String sty, Boolean sym) {
		String result = "";
		DecimalFormat df = new DecimalFormat(sty);
		if(sym) {
			result = "￥ " + df.format(Double.parseDouble(num));
		} else {
			result = df.format(Double.parseDouble(num));
		}
		return result;
	}
	
	public static String removeSymbol(String num) {
		return formatDouble(num.substring(2, num.length()), "0.#", false);
	}
}
