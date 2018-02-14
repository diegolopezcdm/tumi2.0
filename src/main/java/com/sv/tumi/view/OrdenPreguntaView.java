package com.sv.tumi.view;

import java.io.Serializable;

public class OrdenPreguntaView implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String curso;
	private String tema;
	private String subtema;
	private String pregunta;
	private Integer orden;
	
	public OrdenPreguntaView(String curso, String tema, String subtema, String pregunta, Integer orden) {
		super();
		this.curso = curso;
		this.tema = tema;
		this.subtema = subtema;
		this.orden = orden;
		this.pregunta = pregunta;
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

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	
}
