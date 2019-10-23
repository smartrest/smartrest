package it.smartrest.test.responses;

import java.io.Serializable;
import java.math.BigDecimal;

public class TestObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6451971873006445010L;
	private String nome;
	private String cognome;
	private BigDecimal eta;
	private TestObject2 luogoNascita;
	private Double peso;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public BigDecimal getEta() {
		return eta;
	}
	public void setEta(BigDecimal eta) {
		this.eta = eta;
	}
	public TestObject2 getLuogoNascita() {
		return luogoNascita;
	}
	public void setLuogoNascita(TestObject2 luogoNascita) {
		this.luogoNascita = luogoNascita;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	
}