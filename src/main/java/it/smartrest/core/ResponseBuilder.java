package it.smartrest.core;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;

import it.smartrest.responses.JSONReponse;
/**
 * Class of preset response
 * @author davidbertini
 *
 */
public class ResponseBuilder {
	/**
	 * Response for the ok status
	 * @param aResponse
	 * @param aExchange
	 * @throws IOException
	 */
	public static void ok(String aResponse, HttpExchange aExchange)throws IOException{
		buildRespose(HttpStatus.OK, aResponse, aExchange);
	}
	
	/**
	 * Method for generic responses
	 * @param aResponseCode
	 * @param aResponse
	 * @param aExchange
	 * @throws IOException
	 */
	public static void buildRespose(HttpStatus aResponseCode, String aResponse, HttpExchange aExchange) throws IOException {
		aExchange.sendResponseHeaders(aResponseCode.status, aResponse.length());
		OutputStream os = aExchange.getResponseBody();
		os.write(aResponse.getBytes());
		os.close();
	}

	/**
	 * Method for the not implemente method error
	 * @param aResponse
	 * @param aExchange
	 * @param aAllowedMethod
	 * @throws IOException
	 */
	public static void notImplementedMethod(String aResponse, HttpExchange aExchange, String aAllowedMethod)
			throws IOException {
		aExchange.getResponseHeaders().add("Access-Control-Allow-Methods", aAllowedMethod);
		buildRespose(HttpStatus.METHOD_NOT_ALLOWED, aResponse, aExchange);
	}
	
	public static void okJSON(JSONReponse aResponse, HttpExchange aExchange) throws IOException {
		aExchange.getResponseHeaders().add("Content-Type", "application/json");
		buildRespose(HttpStatus.OK, aResponse.buildResponse(), aExchange);
	}
}
