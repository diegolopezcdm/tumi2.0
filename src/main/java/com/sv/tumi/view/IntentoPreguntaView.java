package com.sv.tumi.view;

import java.io.Serializable;

public class IntentoPreguntaView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String curso;
	private String tema;
	private String subtema;
	private String pregunta;
	private Integer intento;
	private Integer resultadoIntento;
	private String esCorrecto;
	private String tipoPregunta;
	private Integer numeroRespuestaCorrecta;

	public IntentoPreguntaView(String curso, String tema, String subtema, String pregunta,
			Integer intento, Integer resultadoIntento, String esCorrecto,
			String tipoPregunta, Integer numeroRespuestaCorrecta) {
		super();
		this.curso = curso;
		this.tema = tema;
		this.subtema = subtema;
		this.intento = intento;
		this.resultadoIntento = resultadoIntento;
		this.esCorrecto = esCorrecto;
		this.tipoPregunta = tipoPregunta;
		this.numeroRespuestaCorrecta = numeroRespuestaCorrecta;
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

	public Integer getIntento() {
		return intento;
	}

	public void setIntento(Integer intento) {
		this.intento = intento;
	}

	public String getEsCorrecto() {
		return esCorrecto;
	}

	public void setEsCorrecto(String esCorrecto) {
		this.esCorrecto = esCorrecto;
	}

	public Integer getResultadoIntento() {
		return resultadoIntento;
	}

	public void setResultadoIntento(Integer resultadoIntento) {
		this.resultadoIntento = resultadoIntento;
	}

	public String getTipoPregunta() {
		return tipoPregunta;
	}

	public void setTipoPregunta(String tipoPregunta) {
		this.tipoPregunta = tipoPregunta;
	}

	public Integer getNumeroRespuestaCorrecta() {
		return numeroRespuestaCorrecta;
	}

	public void setNumeroRespuestaCorrecta(Integer numeroRespuestaCorrecta) {
		this.numeroRespuestaCorrecta = numeroRespuestaCorrecta;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

}
