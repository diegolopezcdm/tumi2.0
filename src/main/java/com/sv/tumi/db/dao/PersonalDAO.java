package com.sv.tumi.db.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.entity.Cargo;
import com.sv.tumi.db.entity.Curso;
import com.sv.tumi.db.entity.Personal;


/**
 * @author Hector Santos
 */
public class PersonalDAO extends AbstractDAO<Personal, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(PersonalDAO.class);

	public PersonalDAO() {
		super(Personal.class);
	}

}
