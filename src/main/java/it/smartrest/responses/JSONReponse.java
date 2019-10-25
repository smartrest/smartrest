package it.smartrest.responses;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

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

//		for (Iterator<String> iterator = fieldsMap.keySet().iterator(); iterator.hasNext();) {
//			String type = iterator.next();
//			System.out.println(type);
//		}

		/// for every object method
		for (Method m : aObject.getClass().getMethods()) {
			try {
				// check for only method get of field
				if (fieldsMap.containsKey(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase())) {
//					System.out.println(m.getName());
					Object o = m.invoke(aObject);
//					System.out.println(o.getClass());
					if (o != null) {
						appendInit(json,
								fieldsMap.get(m.getName().replaceFirst(_METHOD_PREFIX, "").toUpperCase()).getName());
						
						if(!writeBasicTypeData(json,o)) {
							// Arrays of know types
							if (o instanceof java.lang.Object[]) {
								json.append(_OPEN_BRACKETS);
								boolean found = false;
								for (int i = 0; i < ((java.lang.Object[]) o).length; i++) {
									if (((java.lang.Object[]) o)[i] != null) {
										found = writeBasicTypeData(json,((java.lang.Object[]) o)[i]);
										if (i + 1 < ((java.lang.Object[]) o).length&&found)
											json.append(_STRING_END);
									}
								}  
								if(!found) {
									for (int i = 0; i < ((java.lang.Object[]) o).length; i++) {
										if (((java.lang.Object[]) o)[i] != null) {
											json.append(_OPEN_BRACE).append(buildJsonString(((java.lang.Object[]) o)[i])).append(_CLOSE_BRACE);
											if (i + 1 < ((java.lang.Object[]) o).length)
												json.append(_STRING_END);
										}
									}
								}
								
								json.append(_CLOSE_BRACKETS);
							} else if (o instanceof java.util.List) {
								
								json.append(_OPEN_BRACKETS);
								boolean found = false;
								for (int i = 0; i < ((java.util.List) o).size(); i++) {
									if ( ((java.util.List) o).get(i) != null) {
										found = writeBasicTypeData(json,((java.util.List) o).get(i));
										if (i + 1 < ((java.util.List) o).size()&&found)
											json.append(_STRING_END);
									}
								}  
								if(!found) {
									for (int i = 0; i < ((java.util.List) o).size(); i++) {
										if (((java.util.List) o).get(i) != null) {
											json.append(_OPEN_BRACE).append(buildJsonString(((java.util.List) o).get(i))).append(_CLOSE_BRACE);
											if (i + 1 < ((java.util.List) o).size())
												json.append(_STRING_END);
										}
									}
								}
								
								json.append(_CLOSE_BRACKETS);
								
							}
							// other object
							else {
								json.append(_OPEN_BRACE).append(buildJsonString(o)).append(_CLOSE_BRACE);
							}
						}
						appendEnd(json);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json.replace(json.lastIndexOf(_STRING_END), json.lastIndexOf(_STRING_END) + 1, "");
	}

	private synchronized void appendInit(StringBuilder buffer, String aName) {
		buffer.append(_STRING_MARKS + aName + _STRING_MARKS + _VALUE_SEPARATOR);
	}

	private synchronized void appendEnd(StringBuilder buffer) {
		buffer.append(_STRING_END);
	}

	private synchronized void writeStringData(StringBuilder json, String data) {
		json.append(_STRING_MARKS + data + _STRING_MARKS);
	}

	private synchronized void writeCharData(StringBuilder json, Character data) {
		json.append(_STRING_MARKS + data.toString() + _STRING_MARKS);
	}

	private synchronized void writeDateData(StringBuilder json, java.util.Date data) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm a z");
		writeStringData(json, df.format((data)));
	}
	
	private synchronized boolean writeBasicTypeData(StringBuilder json, java.lang.Object o) {
		boolean found=false;
		if (o instanceof java.lang.String) {
			writeStringData(json, o.toString());
			found=true;
		} else if (o instanceof java.math.BigDecimal) {
			json.append((((java.math.BigDecimal) o).toPlainString()));
			found=true;
		} else if (o instanceof java.lang.Double) {
			json.append((((java.lang.Double) o).doubleValue()));
			found=true;
		} else if (o instanceof java.lang.Integer) {
			json.append((((java.lang.Integer) o).intValue()));
			found=true;
		} else if (o instanceof java.util.Date) {
			writeDateData(json, (java.util.Date) o);
			found=true;
		} else if (o instanceof java.lang.Character) {
			writeCharData(json, (((java.lang.Character) o).charValue()));
			found=true;
		} else if (o instanceof java.lang.Boolean) {
			json.append((((java.lang.Boolean) o).toString()));
			found=true;
		} else if (o instanceof java.lang.Float) {
			json.append((((java.lang.Float) o).floatValue()));
			found=true;
		} else if (o instanceof java.lang.Long) {
			json.append((((java.lang.Long) o).floatValue()));
			found=true;
		}
		return found;
	}
	
	
	
	
	
	
}
