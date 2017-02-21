package checkount.impl.dao.category;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import checkount.data.category.CategoryData;
import checkount.impl.dao.DaoProcess;

/**
 * Dao Category
 */
public class CategoryDao {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(CategoryDao.class.getName());
	/** Messages log */
	private static final String ERRORGETEXPTYPES = "Error getting the types which the expression regular is Null.";
	
	/** Dao Process*/
	private DaoProcess daoProcess;
	
	/**
	 * Constructor
	 * 
	 * @param daoProcess
	 */
	public CategoryDao(DaoProcess daoProcess) {
		this.daoProcess = daoProcess;
	}
	
	/**
	 * Get only the types have regulation Expression
	 * @return List
	 */
	public List<CategoryData> getRegulationExpCatMoviments() {
		try {
			Session session = daoProcess.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(CategoryData.class);
			criteria.add(Restrictions.isNotNull("regularExp"));
			return criteria.list();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, ERRORGETEXPTYPES, e);
			return null;
		}
	}

	/**
	 * 
	 * @return List
	 */
	public List<CategoryData> getAllChildrenbyIdType(String idType) {
		String query= "SELECT t.ID_TYPE, t.ID_TYPE_FATHER "
						+ " FROM REGISTER_TYPE t "
						+ " START WITH t.DESCRIPTION_T= '" + idType + "'"
						+ " CONNECT BY PRIOR t.ID_TYPE = t.ID_TYPE_FATHER;";
		
		try {
			Session session = daoProcess.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(CategoryData.class, query);
			return criteria.list();
		} catch (Exception e) {
		 System.out.println("ERROR AL CARGAR LOS TIPOS DE MOVIMIENTOS" + e);
		 return null;
		}
	}

	/**
	 * 
	 * @return List
	 */
	public List<CategoryData> getDirectChildrenbyTypeList(List<CategoryData> typeList) {
		if (typeList != null && !typeList.isEmpty()) {
			for (CategoryData typeData : typeList) {
				String query = "SELECT t.ID_TYPE" 
								+ " FROM REGISTER_TYPE t"
								+ " WHERE LEVEL = 2"
								+ " START WITH t.DESCRIPTION_T= '" + typeData.getDescription() + "'"
								+ " CONNECT BY PRIOR t.ID_TYPE = t.ID_TYPE_FATHER;";

				try {
					Session session = daoProcess.getSessionFactory().openSession();
					Criteria criteria = session.createCriteria(CategoryData.class, query);
					typeData.setChildrenList(criteria.list());
				} catch (Exception e) {
					System.out.println("ERROR AL CARGAR LOS TIPOS DE MOVIMIENTOS" + e);
					return null;
				}
			}
		}
		return typeList;
	}
}
