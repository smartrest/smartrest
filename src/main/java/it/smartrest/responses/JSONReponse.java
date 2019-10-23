package it.smartrest.responses;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

public class JSONReponse {

	private final static String _METHOD_PREFIX = "get";
	private final static String _STRING_END = ",";
	private final static char _VALUE_SEPARATOR = ':';
	private final static char _OPEN_BRACE = '{';
	private final static char _CLOSE_BRACE = '}';
	private final static char _STRING_MARKS = '"';
	private final static char _OPEN_BRACKETS = '[';
	private final static char _CLOSE_BRACKETS = ']';

	private Object obj;

	public JSONReponse(Object obj) {
		this.obj = obj;
	}

	public void getResponse() {
		StringBuilder json = new StringBuilder();
		json.append(_OPEN_BRACE);
		json.append(buildJsonString(this.obj));
		json.append(_CLOSE_BRACE);
		System.out.println(json.toString());
	}

	private StringBuilder buildJsonString(Object aObject) {
		StringBuilder json = new StringBuilder();
		// From the object get the map of fields
		HashMap<String, Field> fieldsMap = new HashMap<String, Field>();
		Arrays.asList(aObject.getClass().getDeclaredFields()).forEach(f -> {
			fieldsMap.put(f.getName().toUpperCase(), f);
		});

		/// for every object method
		for (Method m : aObject.getClass().getMethods()) {
			try {
				// check for only method get of field
				if (fieldsMap.containsKey(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase())) {
					Object o = m.invoke(aObject);
					if (o != null) {
						if (fieldsMap.get(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase()).getType()
								.equals(String.class)) {
							json.append(_STRING_MARKS
									+ fieldsMap.get(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase())
											.getName()
									+ _STRING_MARKS + _VALUE_SEPARATOR + _STRING_MARKS + o.toString() + _STRING_MARKS
									+ _STRING_END);
						} else if (fieldsMap.get(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase()).getType()
								.equals(BigDecimal.class)) {
							json.append(_STRING_MARKS
									+ fieldsMap.get(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase())
											.getName()
									+ _STRING_MARKS + _VALUE_SEPARATOR + (((BigDecimal) o).toPlainString())
									+ _STRING_END);
						} else if (fieldsMap.get(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase()).getType()
								.equals(Double.class)) {
							json.append(_STRING_MARKS
									+ fieldsMap.get(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase())
											.getName()
									+ _STRING_MARKS + _VALUE_SEPARATOR + (((Double) o).doubleValue()) + _STRING_END);
						} else {
							json.append(_STRING_MARKS
									+ fieldsMap.get(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase())
											.getName()
									+ _STRING_MARKS + _VALUE_SEPARATOR + _OPEN_BRACE + buildJsonString(o) + _CLOSE_BRACE
									+ _STRING_END);
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json.replace(json.lastIndexOf(_STRING_END), json.lastIndexOf(_STRING_END) + 1, "");
	}
}
