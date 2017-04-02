package com.checkount.impl.dao.movements;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import com.checkount.DaoProcess;
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
	
	/**
	 * Constructor
	 */
	public MovementDao() {
		super();
	}
	
	/**
	 * Method to insert movements into bbdd
	 * @param movimentsList
	 */
	public void insertMoviments(List<MovementData> movimentsList) {
		Session session = DaoProcess.getInstance().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			for (MovementData movimentData : movimentsList) {
				session.save(movimentData);
				session.flush();
			}
			tx.commit();
		} catch (HibernateException e) {
			LOGGER.error(ERROR_SAVE_MOVEMENTS, e);
			tx.rollback();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Method to get movements from bbdd by dates
	 * @param iniDate init date
	 * @param endDate end date
	 * @return List of movements
	 */
	public List<MovementData> getMoviments(Date iniDate, Date endDate){
		
		Session session = DaoProcess.getInstance().getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(MovementData.class);
		System.out.println("Ini: " + iniDate + " / end: " + endDate);
		criteria.add(Restrictions.ge("date", iniDate));
		criteria.add(Restrictions.lt("date", endDate));
		List<MovementData> movList = criteria.list();
		session.close();
		
		return movList;
	}

	/**
	 * Method to delete movements from bbdd
	 */
	public void deleteMovements() {
		String delete = "DELETE FROM MovementData";
		Session session = DaoProcess.getInstance().getSessionFactory().openSession();
		int result = 0;
		Transaction tx = session.beginTransaction();
		try {
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery(delete);
			result = query.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			LOGGER.error(ERROR_DELETE_MOVEMENTS, e);
			tx.rollback();
		} finally{
			session.close();
		}
		LOGGER.debug(MessageFormat.format(DELETE_MOVEMENTS, result));
	}
}
