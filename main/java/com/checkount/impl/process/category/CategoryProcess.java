package com.checkount.impl.process.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.checkount.DaoProcess;
import com.checkount.data.category.CategoryData;
import com.checkount.impl.dao.category.CategoryDao;

/**
 * Singleton class.
 * 
 * This class contains methods to manage categories.
 *
 */
public class CategoryProcess {

	/** Dao Category */
	private CategoryDao categoryDao;
	
	/** Singleton */
	private static CategoryProcess uniqueInstance = new CategoryProcess();
	
	/**
	 * Constructor
	 * @param categoryDao
	 */
	private CategoryProcess() {
		DaoProcess daoProcess = DaoProcess.getInstance();
		this.categoryDao = daoProcess.<CategoryDao>getDao(CategoryDao.class);
	}

	/**
	 * Get a unique instance of CategoryProcess
	 * @return CategoryProcess
	 */
	public static CategoryProcess getInstance() {
		return uniqueInstance;
	}

	/**
	 * @param idCategory id Category
	 */

		CategoryData result = null;
		// Get categories from bbdd and put the result in a map

			Map<String, List<CategoryData>> mapAux = new HashMap<>();

			// map
				if (category.getIdCategoryFather() != null) {
					List<CategoryData> categoryFather = mapAux.get(category.getIdCategoryFather());
					if (categoryFather == null) {
						categoryFather = new ArrayList<>();
					}
					categoryFather.add(category);
					mapAux.put(category.getIdCategoryFather(), categoryFather);
				}
			}

				List<CategoryData> categoryDataMap = mapAux.get(categoryData.getIdCategory());
				if (categoryDataMap != null) {
					categoryData.setChildrenList(categoryDataMap);
				}
			}
		}

		return result;
	}
}
