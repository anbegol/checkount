package com.checkount.impl.dao.category;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.checkount.DaoProcessSingleton;
import com.checkount.data.category.CategoryData;
import com.checkount.impl.dao.Dao;

/**
 * Dao Category.
 */
public class CategoryDao extends Dao{
	
	/** Logger */
	private static final Logger LOGGER = LogManager.getLogger(CategoryDao.class.getName());
	
	/** Messages log */
	private static final String ERROR_GET_EXPTYPES = "Error getting the types which the expression regular is Null.";
	private static final String ERROR_GET_CHILDRENS = "Error getting the children from category {0}";
	
	/**
	 * Constructor
	 */
	public CategoryDao() {
		super();
	}
	
	/**
	 * Load categories that have the {@link CategoryData.regularExp} attribute
	 * different than null. Using HQL (Hibernate Query Language)
	 * 
	 * @return List category's list
	 */
	@SuppressWarnings("unchecked")
	public List<CategoryData> getRegulationExpCatMovements() {
		String get = "FROM CategoryData C WHERE C.regularExp is not Null";
		Session session = DaoProcessSingleton.getInstance().getSessionFactory().openSession();
		List<CategoryData> result = Collections.emptyList();
		Transaction tx = session.beginTransaction();
		try {
			Query<?> query = session.createQuery(get);
			result = (List<CategoryData>) query.getResultList();
			tx.commit();
		} catch (Exception e) {
			LOGGER.error(ERROR_GET_EXPTYPES, e);
			tx.rollback();
		} finally{
			session.close();
		}
		return result;
	}

	/**
	 * Load all children of idCategory
	 * Using Native SQL
	 * 
	 * @return List category's list
	 */
	@SuppressWarnings("unchecked")
	public List<CategoryData> getAllChildrenbyIdCategory(String idCategory) {
		List<CategoryData> result = Collections.emptyList();
		String query = "SELECT *" + " FROM CATEGORY T" + " START WITH T.DESCRIPTION = :idCategory"
				+ " CONNECT BY PRIOR T.ID_CATEGORY = T.ID_CATEGORY_FATHER";
		
		Session session = DaoProcessSingleton.getInstance().getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query<?> criteria = session.createNativeQuery(query, CategoryData.class);
			criteria.setParameter("idCategory", idCategory);
			result = (List<CategoryData>) criteria.getResultList();
			tx.commit();
		} catch (Exception e) {
			LOGGER.error(MessageFormat.format(ERROR_GET_CHILDRENS, idCategory), e);
			tx.rollback();
		} finally{
			session.close();
		}
		return result;
	}
}
