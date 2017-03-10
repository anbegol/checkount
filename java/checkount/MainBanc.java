package checkount;

import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import checkount.data.category.CategoryData;
import checkount.data.movement.MovementData;
import checkount.impl.dao.DaoProcess;
import checkount.impl.dao.category.CategoryDao;
import checkount.impl.dao.csv.ReadCVS;
import checkount.impl.dao.movements.MovementDao;
import checkount.impl.process.category.CategoryProcess;
import checkount.impl.process.movements.MovementProcess;

/**
 * Main class
 */
public class MainBanc {
	
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(MainBanc.class.getName());
	/** Messages log */
	private static final String ERROR_PROCESS = "Error in the process";
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Create Dao's
					DaoProcess daoProcess = new DaoProcess();
					CategoryDao categoryDao = new CategoryDao(daoProcess);
					MovementDao movimentDao = new MovementDao(daoProcess);
					// Create Process
					MovementProcess movimentProcess = new MovementProcess(movimentDao);
					CategoryProcess categoryProcess = new CategoryProcess(categoryDao);
					
					int pos = 0;
					while (pos != 3) {
						// Create of scan
						Scanner scan = new Scanner(System.in);
						// Print of menu
						getMenu();
						// Get the option selected
						pos = scan.nextInt();

						if (pos == 1) {
							// 1. Read movements from cvs
							List<MovementData> cvsreader = ReadCVS.cvsreader();

							// 2. Load categories form bbdd
							List<CategoryData> catMovimentsList = categoryDao.getRegulationExpCatMoviments();

							// 3. Assign type to movement
							movimentProcess.calculateMoviments(cvsreader, catMovimentsList);

							// 4. Save movements into bbdd
							movimentDao.insertMoviments(cvsreader);

							// Show result
							System.out.println(cvsreader);

						} else if (pos == 2) {
							// 1. Get the movements from bbdd by date
							SimpleDateFormat formatData = new SimpleDateFormat("dd.MM.yy");
							Date iniData = formatData.parse("01.01.16");
							Date endData = formatData.parse("31.01.16");
							List<MovementData> movementsByDate = movimentProcess.getMovementsByDate(iniData, endData);

							// 2. Calculate totalizations
							Map<CategoryData, Double> totMap = calculateTotalizations(movementsByDate);

							// 3. Get categories with their children
							List<CategoryData> categoryList = categoryProcess.getCategoriesWithChild("CATEGORY");

							// 4. Calculate sum and print
							if (categoryList != null && !categoryList.isEmpty()) {
								sumAndshowSum(categoryList.get(0), totMap);
							} else {
								System.out.println("Nothing to do.");
							}
						} else if (pos == 3) {
							// Close scanner
							scan.close();
							System.exit(0);
						} else {
							System.out.println("The option selected not is correct.");
						}
					}

				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, ERROR_PROCESS, e);
				}
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

			/**
			 * Method to create the main menu
			 */
			private void getMenu() {
				System.out.println("****************************************");
				System.out.println("MENU");
				System.out.println("1. Load Movements.");
				System.out.println("2. Process Movements by date.");
				System.out.println("3. Exit program.");
				System.out.println("****************************************");
			}
		});
	}
}
