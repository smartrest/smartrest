package it.smartrest.test.responses;

import java.math.BigDecimal;
import java.math.RoundingMode;

import it.smartrest.responses.JSONReponse;

public class TestJSONResponses {
	public static void main(String[] args) {
		TestObject o = new TestObject();
		o.setCognome(null);
		o.setNome("David");
		o.setEta(new BigDecimal("36"));
		
		JSONReponse r = new JSONReponse(o);
		r.getResponse();
	}
}
