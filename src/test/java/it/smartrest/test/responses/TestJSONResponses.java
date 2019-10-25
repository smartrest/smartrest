package it.smartrest.test.responses;

import java.math.BigDecimal;
import java.util.Date;

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
		firenze.setAttivo('S');
		
		o.setLuogoNascita(firenze);
		o.setPeso(new Double(123.4));
		o.setPrimitiva(10);
		o.setDataNascita(new Date());
		o.setPadre(true);
		
		JSONReponse r = new JSONReponse(o);
		r.getResponse();
	}
}
