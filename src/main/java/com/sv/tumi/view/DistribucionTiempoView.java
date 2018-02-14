package com.sv.tumi.view;

import java.io.Serializable;

public class DistribucionTiempoView implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String curso;
	private String tema;
	private String subtema;
	private Double porcentaje;
	
	public DistribucionTiempoView(String curso, String tema, String subtema, Double porcentaje) {
		super();
		this.curso = curso;
		this.tema = tema;
		this.subtema = subtema;
		this.porcentaje = porcentaje;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public String getSubtema() {
		return subtema;
	}

	public void setSubtema(String subtema) {
		this.subtema = subtema;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
	
}
