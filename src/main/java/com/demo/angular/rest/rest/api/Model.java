package com.demo.angular.rest.rest.api;

public class Model {
	private String code;
	private String libelle;
	
	public Model(String code, String libelle) {
		super();
		this.code = code;
		this.libelle = libelle;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	
	

}
