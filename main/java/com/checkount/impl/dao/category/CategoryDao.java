package com.checkount.impl.dao.category;

import java.text.MessageFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.checkount.DaoProcess;
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
	 * Get only the types have regulation Expression
	 * @return List category's list
	 */
	public List<CategoryData> getRegulationExpCatMoviments() {
		try {
			Session session = DaoProcess.getInstance().getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(CategoryData.class);
			criteria.add(Restrictions.isNotNull("regularExp"));
			return criteria.list();
		} catch (Exception e) {
			LOGGER.error(ERROR_GET_EXPTYPES, e);
			return null;
		}
	}

	/**
	 * Get all children of idType
	 * @return List category's list
	 */
	public List<CategoryData> getAllChildrenbyIdCategory(String idCategory) {
		String query= "SELECT t.ID_TYPE, t.ID_TYPE_FATHER "
						+ " FROM REGISTER_TYPE t "
						+ " START WITH t.DESCRIPTION_T= '" + idCategory + "'"
						+ " CONNECT BY PRIOR t.ID_TYPE = t.ID_TYPE_FATHER;";
		
		try {
			Session session = DaoProcess.getInstance().getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(CategoryData.class, query);
			return criteria.list();
		} catch (Exception e) {
			LOGGER.error(MessageFormat.format(ERROR_GET_CHILDRENS, idCategory), e);
			return null;
		}
	}
}
