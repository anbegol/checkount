package checkount.impl.dao.movements;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import checkount.data.movement.MovementData;
import checkount.impl.dao.DaoProcess;

/**
 * Dao Moviments
 */
public class MovementDao {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MovementDao.class.getName());
	/** Messages log */
	private static final String ERROR_SAVE_MOVEMENTS = "Error to save moviment in the bbdd.";
	
	/** Dao Process*/
	private DaoProcess daoProcess;
	
	public MovementDao(DaoProcess daoProcess) {
		this.daoProcess = daoProcess;
	}
	
	/**
	 * Method to insert movements into bbdd
	 * @param movimentsList
	 */
	public void insertMoviments(List<MovementData> movimentsList) {
		try {
			Session session = daoProcess.getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			for (MovementData movimentData : movimentsList) {
				session.save(movimentData);
				session.flush();
			}
			tx.commit();
			session.close();
		} catch (HibernateException e) {
			LOGGER.log(Level.SEVERE, ERROR_SAVE_MOVEMENTS, e);
		}
	}
	
	/**
	 * Method to get movements from bbdd by dates
	 * @param iniDate init date
	 * @param endDate end date
	 * @return List
	 */
	public List<MovementData> getMoviments(Date iniDate, Date endDate){
		
		Session session = daoProcess.getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(MovementData.class);
		System.out.println("Ini: " + iniDate + " / end: " + endDate);
		criteria.add(Restrictions.ge("date", iniDate));
		criteria.add(Restrictions.lt("date", endDate));
		List<MovementData> movList = criteria.list();
		session.close();
		
		return movList;
	}
}
