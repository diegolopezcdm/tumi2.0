/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sv.tumi.db.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Hector Santos
 */
@Entity
@Table(name = "respuestaEncuesta")
@NamedQueries({
    @NamedQuery(name = "RespuestaEncuesta.findAll", query = "SELECT s FROM RespuestaEncuesta s")})
public class RespuestaEncuesta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "valor")
    private Integer valor;
    @Column(name = "usuarioRegistro")
    private String usuarioRegistro;
    @Column(name = "fechaRegistro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "fechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuarioModificacion")
    private String usuarioModificacion;
   
    @JoinColumn(name = "codigoCriterioEncuesta", referencedColumnName = "codigo")
    @ManyToOne
    private CriterioEncuesta codigoCriterioEncuesta;
    @JoinColumn(name = "CodigoEncuesta", referencedColumnName = "codigo")
    @ManyToOne
    private Encuesta CodigoEncuesta;
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public Integer getValor() {
		return valor;
	}
	public void setValor(Integer valor) {
		this.valor = valor;
	}
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}
	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	public Date getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	public CriterioEncuesta getCodigoCriterioEncuesta() {
		return codigoCriterioEncuesta;
	}
	public void setCodigoCriterioEncuesta(CriterioEncuesta codigoCriterioEncuesta) {
		this.codigoCriterioEncuesta = codigoCriterioEncuesta;
	}
	public Encuesta getCodigoEncuesta() {
		return CodigoEncuesta;
	}
	public void setCodigoEncuesta(Encuesta codigoEncuesta) {
		CodigoEncuesta = codigoEncuesta;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}  
	
}
