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
	 * Get the categories father with their children
	 * @param idCategory id Category
	 * @return List List of children of id Category param
	 */
	public CategoryData getFatherCategoriesWithChildren(String idCategory) {

		CategoryData result = null;
		// Get categories from bbdd and put the result in a map
		List<CategoryData> allChildrenbyIdCategory = categoryDao.getAllChildrenbyIdCategory(idCategory);

		if (allChildrenbyIdCategory != null && !allChildrenbyIdCategory.isEmpty()) {
			// Create a new aux map to save the children
			Map<String, List<CategoryData>> mapAux = new HashMap<>();

			// Check the children and assign the children with their father in the aux
			// map
			for (CategoryData category : allChildrenbyIdCategory) {
				if (category.getIdCategoryFather() != null) {
					List<CategoryData> categoryFather = mapAux.get(category.getIdCategoryFather());
					if (categoryFather == null) {
						categoryFather = new ArrayList<>();
					}
					categoryFather.add(category);
					mapAux.put(category.getIdCategoryFather(), categoryFather);
				}
			}

			for (CategoryData categoryData : allChildrenbyIdCategory) {
				List<CategoryData> categoryDataMap = mapAux.get(categoryData.getIdCategory());
				if (categoryDataMap != null) {
					categoryData.setChildrenList(categoryDataMap);
				}
			}
			result = allChildrenbyIdCategory.get(0);
		}

		return result;
	}
}
