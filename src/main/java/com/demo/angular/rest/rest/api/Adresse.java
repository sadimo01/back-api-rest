package com.demo.angular.rest.rest.api;

public class Adresse {
	private String rue;
	private Long numero;
	private String commune;
	
	public Adresse(String rue, Long numero, String commune) {
		super();
		this.rue = rue;
		this.numero = numero;
		this.commune = commune;
	}
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getCommune() {
		return commune;
	}
	public void setCommune(String commune) {
		this.commune = commune;
	}
	
	

}
