package com.sv.tumi.view;

import java.io.Serializable;

import org.primefaces.model.chart.MeterGaugeChartModel;

public class RangosTiempoPreguntasView implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String curso;
	private String tema;
	private String subtema;
	private String pregunta;
	private MeterGaugeChartModel rangos;
	
	
	public RangosTiempoPreguntasView(String curso, String tema, String subtema, String pregunta,
			MeterGaugeChartModel rangos) {
		super();
		this.curso = curso;
		this.tema = tema;
		this.subtema = subtema;
		this.rangos = rangos;
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
	public MeterGaugeChartModel getRangos() {
		return rangos;
	}
	public void setRangos(MeterGaugeChartModel rangos) {
		this.rangos = rangos;
	}
	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	
}
