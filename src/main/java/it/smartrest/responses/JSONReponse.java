package it.smartrest.responses;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

public class JSONReponse {

	private Object obj;

	public JSONReponse(Object obj) {
		this.obj = obj;
	}

	public void getResponse() {
		HashMap<String, Field> map = new HashMap<String, Field>();
		Arrays.asList(this.obj.getClass().getDeclaredFields()).forEach(f -> {
			map.put(f.getName().toUpperCase(), f);
		});
		System.out.println("{");
		for (Method m : this.obj.getClass().getMethods()) {
			try {
				if (map.containsKey(m.getName().replaceFirst("get", "").toUpperCase())) {
					Object o = m.invoke(this.obj);
					if(o!=null) {
						if( map.get(m.getName().replaceFirst("get", "").toUpperCase()).getType().equals(String.class)) {
							System.out.println("\""+map.get(m.getName().replaceFirst("get", "").toUpperCase()).getName()+"\":\""+ o.toString()+"\",");
							
						}
						if( map.get(m.getName().replaceFirst("get", "").toUpperCase()).getType().equals(BigDecimal.class)) {
							System.out.println("\""+map.get(m.getName().replaceFirst("get", "").toUpperCase()).getName()+"\":"+ (((BigDecimal)o).toPlainString())+",");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("}");
		

	}
}
