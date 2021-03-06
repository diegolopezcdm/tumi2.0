package com.sv.tumi.db.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.entity.Curso;
import com.sv.tumi.db.entity.Tipoevaluacion;


/**
 * @author Hector Santos
 */
public class TipoevaluacionDAO extends AbstractDAO<Tipoevaluacion, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TipoevaluacionDAO.class);

	public TipoevaluacionDAO() {
		super(Tipoevaluacion.class);
	}

}
