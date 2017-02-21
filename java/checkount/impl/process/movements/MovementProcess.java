package checkount.impl.process.movements;

import java.util.Date;
import java.util.List;

import checkount.data.category.CategoryData;
import checkount.data.movement.MovementData;
import checkount.impl.dao.movements.MovementDao;

/**
 * Process movements
 *
 */
public class MovementProcess {
	
	/** Dao Movement */
	protected MovementDao movimentDao;
	
	/**
	 * Constructor
	 * @param movimentDao
	 */
	public MovementProcess(MovementDao movimentDao) {
		this.movimentDao = movimentDao;
	}

	/**
	 * Method to assign the type to movement
	 * @param movimentsList List of movements
	 * @param catMovimentsList List of types
	 */
	public void calculateMoviments(List<MovementData> movimentsList, List<CategoryData> catMovimentsList) {
		
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
	public List<MovementData> getMovementsByDate(Date initDate, Date endDate){
		List<MovementData> movimentsList = movimentDao.getMoviments(initDate, endDate);
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
}
