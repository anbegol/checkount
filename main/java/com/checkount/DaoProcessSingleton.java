package com.checkount;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.checkount.impl.dao.Dao;
import com.checkount.impl.dao.category.CategoryDao;
import com.checkount.impl.dao.movements.MovementDao;

/**
 * Singleton class
 * 
 * This class management the dao's and the sessionFactory
 *
 */
public class DaoProcessSingleton {
	
	/***/
	private SessionFactory sessionFactory;
	
	/** Map where save the DAO's */
	private Map<Class<?>, Dao> daoMap;
	
	/** Singleton */
	private static DaoProcessSingleton uniqueInstance = new DaoProcessSingleton();
	
	/**
	 * Constructor
	 */
	private DaoProcessSingleton() {
		super();
		this.daoMap = new HashMap<>();
		daoMap.put(CategoryDao.class, new CategoryDao());
		daoMap.put(MovementDao.class, new MovementDao());
		
	}

	/**
	 * Get the session factory
	 * @return SessionFactory
	 */
	public SessionFactory getSessionFactory(){
		if(sessionFactory == null) {
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();			
		}
		return sessionFactory;
	}
	
	/**
	 * Get the dao from daoMap with the parameter
	 * @param dao String of dao
	 * @return 
	 * @return class of dao
	 */
	@SuppressWarnings("unchecked")
	public <T> T getDao(Class<T> dao){
		return (T) daoMap.get(dao);
	}

	/**
	 * Get unique instance from dao process
	 * @return instance of DaoProcess
	 */
	public static DaoProcessSingleton getInstance() {
		return uniqueInstance;
	}
}
