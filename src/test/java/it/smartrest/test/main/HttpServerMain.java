package it.smartrest.test.main;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import it.smartrest.test.services.TestService;

public class HttpServerMain {
	public static void main(String[] args) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
			server.createContext("/rest", new TestService());
			server.setExecutor(Executors.newSingleThreadExecutor()); // creates a default executor
			server.start();
			System.out.println("Server start on port 8080...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
