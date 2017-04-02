package com.checkount.data.movement.result;

import java.util.Map;

import com.checkount.data.category.CategoryData;

public class TotalizationResult {
	
	/** Category data */
	CategoryData category;
	
	/** Map where to save the totalizations */
	Map<CategoryData, Double> totMap;

	/**
	 * Constructor
	 * @param categoryList Category's list
	 * @param totMap Totalizations
	 */
	public TotalizationResult(CategoryData category, Map<CategoryData, Double> totMap) {
		super();
		this.category = category;
		this.totMap = totMap;
	}

	public CategoryData getCategory() {
		return category;
	}

	public Map<CategoryData, Double> getTotMap() {
		return totMap;
	}
}
