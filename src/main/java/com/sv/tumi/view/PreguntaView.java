package com.sv.tumi.view;

import java.io.Serializable;

public class PreguntaView implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigoPregunta;
	private String pregunta;
	private Double minutos;
	private String curso;
	private String tema;
	private String subtema;
	
	public PreguntaView(Integer codigoPregunta, String pregunta, Double minutos, String curso, String tema, String subtema) {
		super();
		this.codigoPregunta = codigoPregunta;
		this.pregunta = pregunta;
		this.minutos = minutos;
		this.curso = curso;
		this.tema = tema;
		this.subtema = subtema;
	}
	public Integer getCodigoPregunta() {
		return codigoPregunta;
	}
	public void setCodigoPregunta(Integer codigoPregunta) {
		this.codigoPregunta = codigoPregunta;
	}
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	public Double getMinutos() {
		return minutos;
	}
	public void setMinutos(Double minutos) {
		this.minutos = minutos;
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
		
}
