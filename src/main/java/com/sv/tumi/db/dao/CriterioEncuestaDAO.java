package com.sv.tumi.db.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.entity.CriterioEncuesta;


/**
 * @author Hector Santos
 */
public class CriterioEncuestaDAO extends AbstractDAO<CriterioEncuesta, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CriterioEncuestaDAO.class);

	public CriterioEncuestaDAO() {
		super(CriterioEncuesta.class);
	}

}
