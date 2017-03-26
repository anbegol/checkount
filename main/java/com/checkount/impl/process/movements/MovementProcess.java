package com.checkount.impl.process.movements;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.checkount.DaoProcess;
import com.checkount.data.category.CategoryData;
import com.checkount.data.movement.MovementData;
import com.checkount.impl.dao.category.CategoryDao;
import com.checkount.impl.dao.csv.ReadCSV;
import com.checkount.impl.dao.movements.MovementDao;
import com.checkount.impl.process.category.CategoryProcess;

/**
 * 
 * Singleton class.
 * 
 * This class contains methods to manage movements.
 *
 */
public class MovementProcess {

	/** Logger */
	private static Logger LOGGER = LogManager.getLogger(MovementProcess.class);
	/** Message Log */
	private static final String EMPTY_CATEGORY_LIST = "Not is possible calculate the totalizations. The category's list is empty.";
	
	/** Dao Movement */
	protected MovementDao movementDao;

	/** Dao Category */
	private CategoryDao categoryDao;
	
	/** Singleton */
	private static MovementProcess uniqueInstance = new MovementProcess();
	
	/**
	 * Constructor
	 * @param movimentDao
	 */
	private MovementProcess() {
		DaoProcess daoProcess = DaoProcess.getInstance();
		this.movementDao = daoProcess.<MovementDao>getDao(MovementDao.class);
		this.categoryDao = daoProcess.<CategoryDao>getDao(CategoryDao.class);
	}

	/**
	 * Get a unique instance of MovementProcess
	 * @return MovementProcess
	 */
	public static MovementProcess getInstance() {
		return uniqueInstance;
	}

	/**
	 * Load movements
	 * @param path Path where is the moviment's file
	 */
	public void loadMovements(String path) {
		// 1. Read movements from csv document
		List<MovementData> cvsreader = ReadCSV.csvreader(path);

		// 2. Load categories form bbdd
		List<CategoryData> catMovimentsList = categoryDao.getRegulationExpCatMoviments();

		// 3. Assign type to movement
		calculateMoviments(cvsreader, catMovimentsList);

		// 4. Save movements into bbdd
		movementDao.insertMoviments(cvsreader);
	}

	/**
	 * Calculate totalizations between two dates(iniData - endData)
	 * @param iniData initial data
	 * @param endData end data
	 */
	public void getTotalizations(Date iniData, Date endData) {
		// 1. Get the movements from bbdd by date
		List<MovementData> movementsByDate = getMovementsByDate(iniData, endData);
	
		// 2. Calculate totalizations
		Map<CategoryData, Double> totMap = calculateTotalizations(movementsByDate);
	
		// 3. Get categories with their children
		List<CategoryData> categoryList = CategoryProcess.getInstance().getCategoriesWithChild("CATEGORY");
	
		// 4. Calculate sum and print
		if (categoryList != null && !categoryList.isEmpty()) {
			sumAndshowSum(categoryList.get(0), totMap);
		} else {
			LOGGER.info(EMPTY_CATEGORY_LIST);
		}
	}

	/**
	 * Method to delete movements
	 */
	public void deleteMovements(){
		movementDao.deleteMovements();
	}

	/**
	 * Method to assign the type to movement
	 * @param movimentsList List of movements
	 * @param catMovimentsList List of types
	 */
	private void calculateMoviments(List<MovementData> movimentsList, List<CategoryData> catMovimentsList) {
		
		for (MovementData moviments : movimentsList) {
			String descripction = moviments.getDescripction();
			if(descripction != null) {
				String type = getAssignCategory(descripction, catMovimentsList);
				moviments.setCategory(type);
			}
		}
	}
	
	/**
	 * Method to get the movements between a init date and end date
	 * @param initDate init date
	 * @param endDate end date
	 */
	private List<MovementData> getMovementsByDate(Date initDate, Date endDate){
		List<MovementData> movimentsList = movementDao.getMoviments(initDate, endDate);
		return movimentsList;
	}
	
	/**
	 * This method is to get the catefory's id from a description
	 * @param descripction description of movement
	 * @param catMovimentsList category list
	 * @return
	 */
	private String getAssignCategory(String descripction, List<CategoryData> catMovimentsList) {
		String result = null;
		for (CategoryData catMovimentData : catMovimentsList) {
			boolean con = catMovimentData.contains(descripction);
			if(con){
				result = catMovimentData.getIdCategory();
				break;
			}
		}
		return result;
	}

	/**
	 * Calculate totalizations
	 * @param movementsByDate Movements
	 * @return Map
	 */
	private Map<CategoryData, Double> calculateTotalizations(List<MovementData> movementsByDate) {
		Map<CategoryData, Double> totMap = new HashMap<>();
		for (MovementData movement : movementsByDate) {
			// Get the movement's type and add the import
			CategoryData category = new CategoryData();
			category.setIdCategory(movement.getCategory());

			Double total = totMap.get(category);
			if (total != null) {
				total += movement.getImport_m();
			} else {
				totMap.put(category, movement.getImport_m());
			}
		}
		return totMap;
	}

	/**
	 * Sum and print the result in Console
	 * @param directChildrenbyTypeList
	 * @param totMap
	 */
	private double sumAndshowSum(CategoryData typeData, Map<CategoryData, Double> totMap) {
		double sumTot = 0;
		double only = 0;
		if (typeData != null && totMap != null) {
			// Get children from typeData
			List<CategoryData> childrenList = typeData.getChildrenList();
			if (childrenList == null) {
				Double total = totMap.get(typeData);
				if (total == null) {
					total = 0D;
				}
				sumTot = total;
			} else {
				for (CategoryData children : childrenList) {
					only = sumAndshowSum(children, totMap);
					System.out.println("* " + children.getDescription() + " -> " + only);
					sumTot += only;
				}
				System.out.println(typeData.getDescription() + " -> " + sumTot);
			}
		}
		return sumTot;
	}
}
