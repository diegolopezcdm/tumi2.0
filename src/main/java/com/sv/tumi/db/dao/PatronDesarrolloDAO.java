package com.sv.tumi.db.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.entity.Patrondesarrollo;


/**
 * @author Hector Santos
 */
public class PatronDesarrolloDAO extends AbstractDAO<Patrondesarrollo, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(PatronDesarrolloDAO.class);

	public PatronDesarrolloDAO() {
		super(Patrondesarrollo.class);
	}

}
