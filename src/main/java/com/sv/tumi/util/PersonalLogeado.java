package com.sv.tumi.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.sv.tumi.db.dao.PersonalDAO;
import com.sv.tumi.db.entity.Personal;

@ManagedBean(name = "personalLogeado")
@SessionScoped
public class PersonalLogeado {

	private String usuario;
	private String pwd;
	private static Personal personal;
	Map<String, Object> filter = new HashMap<String, Object>();
	private PersonalDAO personalDAO = new PersonalDAO();

	// analistadecapacitacion
	// jeferrhh
	// personalacapacitar

	public void login() throws IOException {
		filter.clear();
		filter.put("usuario", usuario);
		filter.put("contraseña", pwd);
		List<Personal> personalList = personalDAO.findByProperty(filter);

		if (personalList.size() == 1) {
			personal = personalList.get(0);
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/sistema-capacitaciones-tumi/app/index.xhtml");
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"usuario o contraseña incorrecta", null));
			return;
		}

	}

	public void logout() throws IOException {
		personal = null;
		FacesContext.getCurrentInstance().getExternalContext()
				.redirect("/sistema-capacitaciones-tumi/app/login.xhtml");
	}
	
	public static Integer getCodigoPersonal(){
		return personal.getCodigo();
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Personal getPersonal() {
		return personal;
	}

	public void setPersonal(Personal personal) {
		this.personal = personal;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

}
