package com.sv.tumi.db.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.entity.Especialidad;


/**
 * @author Hector Santos
 */
public class EspecialidadDAO extends AbstractDAO<Especialidad, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Especialidad.class);

	public EspecialidadDAO() {
		super(Especialidad.class);
	}

}
