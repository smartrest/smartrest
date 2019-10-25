package it.smartrest.test.responses;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TestObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6451971873006445010L;
	private String nome;
	private String cognome;
	private BigDecimal eta;
	private City luogoNascita;
	private Double peso;
	private Date dataNascita;
	private int primitiva;
	private boolean padre;
	private String[] interests;
	private BigDecimal[] assets;
	private City[] livedCity;
	
	private List<String> skills;
	private List<City> lovedCity;
	
	
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
	public City getLuogoNascita() {
		return luogoNascita;
	}
	public void setLuogoNascita(City luogoNascita) {
		this.luogoNascita = luogoNascita;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public int getPrimitiva() {
		return primitiva;
	}
	public void setPrimitiva(int primitiva) {
		this.primitiva = primitiva;
	}
	public Date getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
	public boolean getPadre() {
		return padre;
	}
	public void setPadre(boolean padre) {
		this.padre = padre;
	}
	public String[] getInterests() {
		return interests;
	}
	public void setInterests(String[] interests) {
		this.interests = interests;
	}
	public BigDecimal[] getAssets() {
		return assets;
	}
	public void setAssets(BigDecimal[] assets) {
		this.assets = assets;
	}
	public City[] getLivedCity() {
		return livedCity;
	}
	public void setLivedCity(City[] livedCity) {
		this.livedCity = livedCity;
	}
	public List<String> getSkills() {
		return skills;
	}
	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
	public List<City> getLovedCity() {
		return lovedCity;
	}
	public void setLovedCity(List<City> lovedCity) {
		this.lovedCity = lovedCity;
	}
	
	
}
