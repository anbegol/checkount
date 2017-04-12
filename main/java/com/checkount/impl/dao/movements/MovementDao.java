package com.checkount.impl.dao.movements;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.checkount.DaoProcessSingleton;
import com.checkount.data.movement.MovementData;
import com.checkount.impl.dao.Dao;

/**
 * Dao Movements
 */
public class MovementDao extends Dao {
	
	/** Logger */
	private static final Logger LOGGER = LogManager.getLogger(MovementDao.class);
	
	/** Messages log */
	private static final String ERROR_SAVE_MOVEMENTS = "Error to save movement in the database.";
	private static final String ERROR_DELETE_MOVEMENTS = "Error to delete movement in the database.";
	private static final String DELETE_MOVEMENTS = "Delete {0} movements from database";
	private static final String ERROR_GET_MOVEMENTS = "Error to get movement from the database.";
	
	/**
	 * Constructor
	 */
	public MovementDao() {
		super();
	}
	
	/**
	 * Method to insert movements into data base
	 * 
	 * @param movimentsList
	 */
	public void insertMoviments(List<MovementData> movimentsList) {
		Session session = DaoProcessSingleton.getInstance().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			for (MovementData movimentData : movimentsList) {
				session.save(movimentData);
				session.flush();
			}
			tx.commit();
		} catch (Exception e) {
			LOGGER.error(ERROR_SAVE_MOVEMENTS, e);
			tx.rollback();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Method to get movements from data base by dates
	 * Using HQL (Hibernate Query Language)
	 * @param iniDate init date
	 * @param endDate end date
	 * @return List of movements
	 */
	@SuppressWarnings("unchecked")
	public List<MovementData> getMovements(Date iniDate, Date endDate){
		String getMovements = "FROM MovementData M WHERE M.date >= :iniDate AND M.date <= :endDate";
		Session session = DaoProcessSingleton.getInstance().getSessionFactory().openSession();
		List<MovementData> result = Collections.emptyList();
		Transaction tx = session.beginTransaction();
		try {
			Query<MovementData> query = session.createQuery(getMovements);
			query.setParameter("iniDate", iniDate);
			query.setParameter("endDate", endDate);
			result = query.getResultList();
			tx.commit();
		} catch (Exception e) {
			LOGGER.error(ERROR_GET_MOVEMENTS, e);
			tx.rollback();
		} finally{
			session.close();
		}
		return result;
	}

	/**
	 * Method to delete movements from data base
	 * Using HQL (Hibernate Query Language)
	 */
	public void deleteMovements() {
		String delete = "DELETE FROM MovementData";
		Session session = DaoProcessSingleton.getInstance().getSessionFactory().openSession();
		int result = 0;
		Transaction tx = session.beginTransaction();
		try {
			Query<?> query = session.createQuery(delete);
			result = query.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			LOGGER.error(ERROR_DELETE_MOVEMENTS, e);
			tx.rollback();
		} finally{
			session.close();
		}
		LOGGER.debug(MessageFormat.format(DELETE_MOVEMENTS, result));
	}
}
