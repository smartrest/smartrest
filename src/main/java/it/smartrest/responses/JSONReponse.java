package it.smartrest.responses;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;

import it.smartrest.utils.Constants;
/**
 * Object to automate the operation
 * for building a well formatted json
 * @author davidbertini
 *
 */
public class JSONReponse {

	private Object obj;

	public JSONReponse(Object obj) {
		this.obj = obj;
	}

	/**
	 * Method to buil the json string start from the 
	 * object pass in the constructor
	 * @return
	 */
	public String buildResponse() {
		StringBuilder json = new StringBuilder();
		json.append(Constants._OPEN_BRACE);
		json.append(buildJsonString(this.obj));
		json.append(Constants._CLOSE_BRACE);
		return json.toString();
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
				if (fieldsMap.containsKey(m.getName().replaceFirst(Constants._METHOD_PREFIX, "").toUpperCase())) {
//					System.out.println(m.getName());
					Object o = m.invoke(aObject);
//					System.out.println(o.getClass());
					if (o != null) {
						appendInit(json,
								fieldsMap.get(m.getName().replaceFirst(Constants._METHOD_PREFIX, "").toUpperCase()).getName());

						//Check and eventually write to the buffer
						//the basic known type of object
						if (!writeBasicTypeData(json, o)) {
							//If is not aa know basic type 
							//it be check from other composed types
							// Arrays of know types
							if (o instanceof java.lang.Object[]) {
								json.append(Constants._OPEN_BRACKETS);
								boolean found = false;
								for (int i = 0; i < ((java.lang.Object[]) o).length; i++) {
									if (((java.lang.Object[]) o)[i] != null) {
										found = writeBasicTypeData(json, ((java.lang.Object[]) o)[i]);
										if (i + 1 < ((java.lang.Object[]) o).length && found)
											json.append(Constants._STRING_END);
									}
								}
								if (!found) {
									for (int i = 0; i < ((java.lang.Object[]) o).length; i++) {
										if (((java.lang.Object[]) o)[i] != null) {
											json.append(Constants._OPEN_BRACE)
													.append(buildJsonString(((java.lang.Object[]) o)[i]))
													.append(Constants._CLOSE_BRACE);
											if (i + 1 < ((java.lang.Object[]) o).length)
												json.append(Constants._STRING_END);
										}
									}
								}

								json.append(Constants._CLOSE_BRACKETS);
							} else if (o instanceof java.util.List) {

								json.append(Constants._OPEN_BRACKETS);
								boolean found = false;
								for (int i = 0; i < ((java.util.List) o).size(); i++) {
									if (((java.util.List) o).get(i) != null) {
										found = writeBasicTypeData(json, ((java.util.List) o).get(i));
										if (i + 1 < ((java.util.List) o).size() && found)
											json.append(Constants._STRING_END);
									}
								}
								if (!found) {
									for (int i = 0; i < ((java.util.List) o).size(); i++) {
										if (((java.util.List) o).get(i) != null) {
											json.append(Constants._OPEN_BRACE)
													.append(buildJsonString(((java.util.List) o).get(i)))
													.append(Constants._CLOSE_BRACE);
											if (i + 1 < ((java.util.List) o).size())
												json.append(Constants._STRING_END);
										}
									}
								}

								json.append(Constants._CLOSE_BRACKETS);

							}
							// other object
							else {
								json.append(Constants._OPEN_BRACE).append(buildJsonString(o)).append(Constants._CLOSE_BRACE);
							}
						}
						appendEnd(json);

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json.replace(json.lastIndexOf(Constants._STRING_END), json.lastIndexOf(Constants._STRING_END) + 1, "");
	}

	/**
	 * Method to attach the init of every json parameter
	 * @param buffer
	 * @param aName
	 */
	private void appendInit(StringBuilder buffer, String aName) {
		buffer.append(Constants._STRING_MARKS + aName + Constants._STRING_MARKS + Constants._VALUE_SEPARATOR);
	}

	/**
	 * Method to attach the end of every json value
	 * @param buffer
	 */
	private void appendEnd(StringBuilder buffer) {
		buffer.append(Constants._STRING_END);
	}

	/**
	 * Method to format in well-formatted mode the generic String value
	 * @param json
	 * @param data
	 */
	private void writeStringData(StringBuilder json, String data) {
		json.append(Constants._STRING_MARKS + data + Constants._STRING_MARKS);
	}

	/**
	 * Method to format in well-formatted mode the generic Char value
	 * @param json
	 * @param data
	 */
	private void writeCharData(StringBuilder json, Character data) {
		json.append(Constants._STRING_MARKS + data.toString() + Constants._STRING_MARKS);
	}

	/**
	 * Method to format in well-formatted mode the java.util.Date parameter using conventional format
	 * "yyyy-MM-dd HH:mm a z"
	 * @param json
	 * @param data
	 */
	private void writeDateData(StringBuilder json, java.util.Date data) {
		DateFormat df = new SimpleDateFormat(Constants.JSON_DATE_FORMAT);
		writeStringData(json, df.format((data)));
	}

	private boolean writeBasicTypeData(StringBuilder json, java.lang.Object o) {
		boolean found = false;
		if (o instanceof java.lang.String) {
			writeStringData(json, o.toString());
			found = true;
		} else if (o instanceof java.math.BigDecimal) {
			json.append((((java.math.BigDecimal) o).toPlainString()));
			found = true;
		} else if (o instanceof java.math.BigInteger) {
			json.append((((java.math.BigInteger) o).intValue()));
			found = true;
		} else if (o instanceof java.lang.Double) {
			json.append((((java.lang.Double) o).doubleValue()));
			found = true;
		} else if (o instanceof java.lang.Integer) {
			json.append((((java.lang.Integer) o).intValue()));
			found = true;
		} else if (o instanceof java.util.Date) {
			writeDateData(json, (java.util.Date) o);
			found = true;
		} else if (o instanceof java.lang.Character) {
			writeCharData(json, (((java.lang.Character) o).charValue()));
			found = true;
		} else if (o instanceof java.lang.Boolean) {
			json.append((((java.lang.Boolean) o).toString()));
			found = true;
		} else if (o instanceof java.lang.Float) {
			json.append((((java.lang.Float) o).floatValue()));
			found = true;
		} else if (o instanceof java.lang.Long) {
			json.append((((java.lang.Long) o).floatValue()));
			found = true;
		}
		return found;
	}

}
