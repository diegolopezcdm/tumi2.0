package com.sv.tumi.db.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.entity.Encuesta;


/**
 * @author Hector Santos
 */
public class EncuestaDAO extends AbstractDAO<Encuesta, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(EncuestaDAO.class);

	public EncuestaDAO() {
		super(Encuesta.class);
	}

}
