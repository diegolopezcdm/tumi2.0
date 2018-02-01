package com.sv.tumi.db.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sv.tumi.db.connection.SessionFactoryTumi;
import com.sv.tumi.db.entity.Evaluacion;


/**
 * @author Hector Santos
 */
public class EvaluacionDAO extends AbstractDAO<Evaluacion, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(EvaluacionDAO.class);

	public EvaluacionDAO() {
		super(Evaluacion.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Evaluacion> obtenerPorFechas(Date startDate, Date endDate){
		
		SessionFactory sf = SessionFactoryTumi.getSessionAnnotationFactory();
		Session session = sf.openSession();
		List<Evaluacion> list = null;
		try {
			session.getTransaction().begin();
			list = session
					.createQuery("FROM Evaluacion AS c WHERE c.fechaModificacion BETWEEN :stDate AND :edDate ")
					.setTimestamp("stDate", startDate)
					.setTimestamp("edDate", endDate)
					.list();
			session.getTransaction().commit();
		} catch (Throwable ex) {
			logger.error("Error al obtener registro : ", ex);
			if (session != null && session.isOpen()) {
				if (session.getTransaction().isActive()) {
					try {
						session.getTransaction().rollback();
					} catch (HibernateException he) {
						logger.error("Error al realizar rollback a la base de datos : ", he);
					}
				}
			}
		} finally {
			session.flush();
			session.clear();
			session.close();
		}
		return list;
		
	}

}
