package com.sv.tumi.db.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.entity.Tiempodesarrollo;


/**
 * @author Hector Santos
 */
public class TiempoDesarrolloDAO extends AbstractDAO<Tiempodesarrollo, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TiempoDesarrolloDAO.class);

	public TiempoDesarrolloDAO() {
		super(Tiempodesarrollo.class);
	}

}
