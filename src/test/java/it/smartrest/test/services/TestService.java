package it.smartrest.test.services;

import it.smartrest.annotations.Body;
import it.smartrest.annotations.GET;
import it.smartrest.annotations.NotNull;
import it.smartrest.annotations.POST;
import it.smartrest.annotations.Query;
import it.smartrest.handlers.RestHandler;

public class TestService extends RestHandler {

	@GET
	public String method() {
		return "Invocato il metodo 1";
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
	
}
