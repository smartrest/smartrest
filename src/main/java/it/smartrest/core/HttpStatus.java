package it.smartrest.core;

/**
 * Enumeration of usable http status
 * @author davidbertini
 *
 */
public enum HttpStatus {

	NOT_FOUND(404),
	BAD_REQUEST(400),
	METHOD_NOT_ALLOWED(405),
	OK(200);
	protected int status;
	
	private HttpStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return this.status;
	}
	
}
