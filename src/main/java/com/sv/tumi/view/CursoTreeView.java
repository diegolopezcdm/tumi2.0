package com.sv.tumi.view;

import java.io.Serializable;

public class CursoTreeView implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String alcance;
	private boolean enable;
	private Integer codigoNivel;
	private Integer codigoSubtema;
	
	
	public CursoTreeView(String nombre, String alcance, boolean enable) {
		super();
		this.nombre = nombre;
		this.alcance = alcance;
		this.enable = enable;
	}
	
	
	public CursoTreeView(String nombre, String alcance, boolean enable,
			int codigoNivel, int codigoSubtema) {
		super();
		this.nombre = nombre;
		this.alcance = alcance;
		this.enable = enable;
		this.codigoNivel = codigoNivel;
		this.codigoSubtema = codigoSubtema;
	}


	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}	
	
	public boolean isEnable() {
		return enable;
	}
	
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Integer getCodigoNivel() {
		return codigoNivel;
	}

	public void setCodigoNivel(Integer codigoNivel) {
		this.codigoNivel = codigoNivel;
	}

	public Integer getCodigoSubtema() {
		return codigoSubtema;
	}

	public void setCodigoSubtema(Integer codigoSubtema) {
		this.codigoSubtema = codigoSubtema;
	}

}
