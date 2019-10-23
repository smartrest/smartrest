package it.smartrest.test.responses;

import java.math.BigDecimal;

import it.smartrest.responses.JSONReponse;

public class TestJSONResponses {
	public static void main(String[] args) {
		TestObject o = new TestObject();
		o.setCognome(null);
		o.setNome("David");
		o.setEta(new BigDecimal("37"));
		TestObject2 firenze = new TestObject2();
		firenze.setCode("D612");
		firenze.setDescription("Firenze");
		o.setLuogoNascita(firenze);
		o.setPeso(new Double(123.4));
		JSONReponse r = new JSONReponse(o);
		r.getResponse();
	}
}
