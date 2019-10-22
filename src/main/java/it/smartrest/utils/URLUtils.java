package it.smartrest.utils;

import java.util.Arrays;
import java.util.HashMap;

public class URLUtils {

	public static HashMap<String, Object> findParameters(byte body[]) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		try {

			String str = new String(body);

			Arrays.asList(str.split("&")).forEach(p -> {
				if (p != null && !p.trim().equalsIgnoreCase("") && p.split("=").length >= 1)
					params.put(p.split("=")[0], p.split("=")[1]);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	public static HashMap<String, Object> findParameters(String aQuery) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if(aQuery !=null) {
			try {
				Arrays.asList(aQuery.split("&")).forEach(p -> {
					params.put(p.split("=")[0], p.split("=")[1]);
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return params;
	}
}
