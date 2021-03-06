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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Hector Santos
 */
@Entity
@Table(name = "cursoEvaluacion")
@NamedQueries({
    @NamedQuery(name = "Cursoevaluacion.findAll", query = "SELECT c FROM Cursoevaluacion c")})
public class Cursoevaluacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "fechaRegistro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Column(name = "usuarioRegistro")
    private String usuarioRegistro;
    @Column(name = "fechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "usuarioModificacion")
    private String usuarioModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoCursoEvaluacion")
    private List<Cursoevaluacionpregunta> cursoevaluacionpreguntaList;
    @OneToOne(mappedBy = "codigoCursoEvaluacion")
    private Resultadoevaluacion resultadoevaluacion;
    @JoinColumn(name = "codigoCursoNivel", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Cursonivel codigoCursoNivel;
    @JoinColumn(name = "codigoEvaluacion", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Evaluacion codigoEvaluacion;

    public Cursoevaluacion() {
    }

    public Cursoevaluacion(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public List<Cursoevaluacionpregunta> getCursoevaluacionpreguntaList() {
        return cursoevaluacionpreguntaList;
    }

    public void setCursoevaluacionpreguntaList(List<Cursoevaluacionpregunta> cursoevaluacionpreguntaList) {
        this.cursoevaluacionpreguntaList = cursoevaluacionpreguntaList;
    }

	public Resultadoevaluacion getResultadoevaluacion() {
		return resultadoevaluacion;
	}

	public void setResultadoevaluacion(Resultadoevaluacion resultadoevaluacion) {
		this.resultadoevaluacion = resultadoevaluacion;
	}

	public Cursonivel getCodigoCursoNivel() {
        return codigoCursoNivel;
    }

    public void setCodigoCursoNivel(Cursonivel codigoCursoNivel) {
        this.codigoCursoNivel = codigoCursoNivel;
    }

    public Evaluacion getCodigoEvaluacion() {
        return codigoEvaluacion;
    }

    public void setCodigoEvaluacion(Evaluacion codigoEvaluacion) {
        this.codigoEvaluacion = codigoEvaluacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cursoevaluacion)) {
            return false;
        }
        Cursoevaluacion other = (Cursoevaluacion) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sv.tumi.db.entity.Cursoevaluacion[ codigo=" + codigo + " ]";
    }
    
}
