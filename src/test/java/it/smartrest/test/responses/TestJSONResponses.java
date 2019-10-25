package it.smartrest.test.responses;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.smartrest.responses.JSONReponse;

public class TestJSONResponses {
	public static void main(String[] args) {
		TestObject o = new TestObject();
		o.setCognome("Bertini");
		o.setNome("David");
		o.setEta(new BigDecimal("37"));
		
		
		City firenze = new City();
		firenze.setCode("D612");
		firenze.setDescription("Firenze");
		firenze.setAttivo('S');
		
		City barcellona = new City();
		barcellona.setCode("Z888");
		barcellona.setDescription("Barcellona");
		barcellona.setAttivo('S');
		
		
		
		City pistoia = new City();
		pistoia.setCode("G713");
		pistoia.setDescription("Pistoia");
		pistoia.setAttivo('S');
		
		o.setLuogoNascita(firenze);
		o.setPeso(new Double(123.4));
		o.setPrimitiva(10);
		o.setDataNascita(new Date());
		o.setPadre(false);
		
		String interessi[]= {"football","tecnology"};
		o.setInterests(interessi);
		
		BigDecimal assets[] = {
				new BigDecimal(44.10),new BigDecimal(118.435)
		};
		o.setAssets(assets);
		
		City lived[] = {pistoia,firenze};
		o.setLivedCity(lived);
		
		
		List<String> skills = new ArrayList<String>();
		skills.add("JAVA");
		skills.add("ORACLE");
		o.setSkills(skills);
		
		List<City> lovedCity = new ArrayList<City>();
		lovedCity.add(firenze);
		lovedCity.add(barcellona);
		lovedCity.add(pistoia);
		o.setLovedCity(lovedCity);
		
		JSONReponse r = new JSONReponse(o);
		r.getResponse();
	}
}
