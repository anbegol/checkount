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
	 * Get the categories with their children
	 * @param idCategory
	 * @return List 
	 */
	public List<CategoryData> getCategoriesWithChild(String idCategory) {
		
		// Get categories from bbdd and put the result in a map
		List<CategoryData> allChildrenbyIdType = categoryDao.getAllChildrenbyIdCategory(idCategory);
		Map<String, CategoryData> map = new HashMap<>();
		for(CategoryData category: allChildrenbyIdType){
			map.put(category.getIdCategory(), category);
		}
		
		// Check the sons and assign the sons with their father
		for (CategoryData category : allChildrenbyIdType) {
			if(category.getIdCategoryFather() != null) {
				CategoryData categoryFather = map.get(category.getIdCategoryFather());
				List<CategoryData> childrenListF = categoryFather.getChildrenList();
				if(childrenListF == null) {
					childrenListF = new ArrayList<>();
				}
				childrenListF.add(category);
				categoryFather.setChildrenList(childrenListF);
				map.put(category.getIdCategoryFather(), categoryFather);
			}
		}
		
		for (CategoryData categoryData : allChildrenbyIdType) {
			CategoryData categoryDataMap = map.get(categoryData.getIdCategory());
			categoryData.setChildrenList(categoryDataMap.getChildrenList());
		}
		return allChildrenbyIdType;
	}
}
