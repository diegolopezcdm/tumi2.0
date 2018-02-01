package com.sv.tumi.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TemaView implements Comparable<TemaView>{
	
	private String nombre;
	private String alcance;
	private int orden;
	private List<SubtemaView> subtemas = new ArrayList<SubtemaView>();
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
	public int getOrden() {
		return orden;
	}
	public void setOrden(int order) {
		this.orden = order;
	}
	public List<SubtemaView> getSubtemas() {
		return subtemas;
	}
	public void setSubtemas(List<SubtemaView> subtemas) {
		this.subtemas = subtemas;
	}
	/*@Override
	public int compare(TemaView o1, TemaView o2) {
		return o1.orden > o2.orden ? 1 : (o1.orden < o2.orden ? -1 : 0);
	}*/
	@Override
	public int compareTo(TemaView o2) {
		return orden > o2.orden ? 1 : (orden < o2.orden ? -1 : 0);
	}
	
	/*public int getRows(){
		int count=0;
			for (SubtemaView subtema : subtemas) {
				count++;
			}
			
		
		return count;
	}*/

}
