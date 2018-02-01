package com.sv.tumi.view;


public class SubtemaView implements Comparable<SubtemaView> {
	
	private String nombre;
	private int orden;
	private String nivel;
	private String alcance;
	private double porcentajeAprobacion;
	
	private Integer codigoNivel;
	private Integer codigoSubtema;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getOrden() {
		return orden;
	}
	public void setOrder(int orden) {
		this.orden = orden;
	}
	
	public void setOrden(int orden) {
		this.orden = orden;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public double getPorcentajeAprobacion() {
		return porcentajeAprobacion;
	}
	public void setPorcentajeAprobacion(double porcentajeAprobacion) {
		this.porcentajeAprobacion = porcentajeAprobacion;
	}
	/*@Override
	public int compare(SubtemaView o1, SubtemaView o2) {
		return o1.orden > o2.orden ? 1 : (o1.orden < o2.orden ? -1 : 0);
	}*/
	@Override
	public int compareTo(SubtemaView o2) {
		return orden > o2.orden ? 1 : (orden < o2.orden ? -1 : 0);
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
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}

}
