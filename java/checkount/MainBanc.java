package checkount;

import java.awt.EventQueue;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import checkount.data.category.CategoryData;
import checkount.data.movement.MovementData;
import checkount.impl.dao.DaoProcess;
import checkount.impl.dao.category.CategoryDao;
import checkount.impl.dao.csv.ReadCVS;
import checkount.impl.dao.movements.MovementDao;
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

					// Create of scan
					Scanner scan = new Scanner(System.in);
					// Print of menu
					getMenu();
					// Get the option selected
					int pos = scan.nextInt();
					// Close scanner
					scan.close();

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

					} else if(pos == 2) {
						System.exit(0);
					} else {
						System.out.println("The option selected not is correct.");
					}

				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, ERROR_PROCESS, e);
				}
			}

			/**
			 * Method to create the main menu
			 */
			private void getMenu() {
				System.out.println("MENU");
				System.out.println("1. Load Movements.");
				System.out.println("2. Exit program.");
			}
		});
	}
}
