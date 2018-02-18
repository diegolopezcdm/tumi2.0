package com.sv.tumi.db.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.entity.RespuestaEncuesta;


/**
 * @author Hector Santos
 */
public class RespuestaEncuestaDAO extends AbstractDAO<RespuestaEncuesta, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(RespuestaEncuestaDAO.class);

	public RespuestaEncuestaDAO() {
		super(RespuestaEncuesta.class);
	}

}
