package com.sv.tumi.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "personalLogeado")
@SessionScoped
public class PersonalLogeado {
	
	private static Integer codigoPersonal = 1;
	private String tipoPerfil = "analistadecapacitacion";//analistadecapacitacion jeferrhh personalacapacitar
	
	public static Integer getCodigoPersonal() {
		return codigoPersonal;
	}
	
	public static void setCodigoPersonal(Integer codigoPersonal1) {
		codigoPersonal = codigoPersonal1;
	}

	public String getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(String tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}
	
	

}
