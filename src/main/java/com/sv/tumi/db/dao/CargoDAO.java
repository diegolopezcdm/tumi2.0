package com.sv.tumi.db.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.entity.Cargo;
import com.sv.tumi.db.entity.Curso;


/**
 * @author Hector Santos
 */
public class CargoDAO extends AbstractDAO<Cargo, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CargoDAO.class);

	public CargoDAO() {
		super(Cargo.class);
	}

}
