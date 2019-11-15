package it.smartrest.test.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.smartrest.annotations.Body;
import it.smartrest.annotations.GET;
import it.smartrest.annotations.PUT;
import it.smartrest.annotations.Path;
import it.smartrest.annotations.PathParam;
import it.smartrest.annotations.NotNull;
import it.smartrest.annotations.POST;
import it.smartrest.annotations.Query;
import it.smartrest.handlers.RestHandler;
import it.smartrest.responses.JSONReponse;
import it.smartrest.test.responses.City;
import it.smartrest.test.responses.TestObject;

public class TestService extends RestHandler {

	@GET
	@Path("rest/{idUtente}/{codFiscale}")
	public String method(@PathParam("idUtente") BigInteger idUtente,
						 @PathParam("codFiscale") String codFiscale) {
		return "Invocato il metodo 1 con idUtente: " + idUtente + " " + codFiscale;
	}
	@POST
	public String method2(@Query("name") @NotNull String aName, 
						  @Query("secondname") String aSecondName,
						  @Body("bodyparm") String bodyparm
						) {
		System.out.println("Invocato il metodo 2");
		System.out.println("Parametro passato: " + aName);
		System.out.println("Parametro passato: " + aSecondName);
		System.out.println("Parametro passato: " + bodyparm);
		
		
		
		
		return "Hello " + aName + " " + aSecondName;
	}
	
	@PUT
	public JSONReponse method3(@Query("id") BigDecimal id,@Query("datanascita") Date datanascita) {
		TestObject o = new TestObject();
		o.setCognome("Bertini");
		o.setNome("David");
		o.setEta(id);
		
		
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
		o.setDataNascita(datanascita);
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
		return r;
	}
	
}

