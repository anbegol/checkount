package com.checkount;

import java.awt.EventQueue;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.checkount.data.category.CategoryData;
import com.checkount.data.movement.result.TotalizationResult;
import com.checkount.impl.process.movements.MovementProcessSingleton;

/**
 * Main class
 */
public class MainBanc {
	
	/** Logger */
	private static final Logger LOGGER = LogManager.getLogger(MainBanc.class);
	
	/** Messages log */
	private static final String ERROR_PROCESS = "Error in the process";
	private static final String LOAD_MOVEMENTS = "The movements are loaded correctly";
	private static final String EXIT = "The application is closed";
	private static final String OPTION_NO_AVAILABLE = "Option no available";
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Create of scan
				Scanner scan = new Scanner(System.in);
				// Print of menu
				getMenu(scan);
				// Close scan
				scan.close();
			}

			/**
			 * Method where activate the option selected
			 * 
			 * @param pos option selected
			 * @param scan Util to read from console
			 * @throws Exception
			 */
			private void getMenuOption(int pos, Scanner scan) throws Exception {
				boolean option = false;
				while (!option) {
					if (pos == 1) { // Load Movements
						// 1. Read movements from cvs
						System.out.println("Do you want to load the default file? (Y/N)");
						String load = scan.nextLine();
						String path = null;
						if (!load.equalsIgnoreCase("Y")) {
							path = scan.nextLine();
						}
						MovementProcessSingleton movementProcess = MovementProcessSingleton.getInstance();
						movementProcess.loadMovements(path);

						LOGGER.info(LOAD_MOVEMENTS);
						option = true;

					} else if (pos == 2) { // Get totalizations by date
						
						System.out.println("Which dates would you like to consult?");
						System.out.println("(dd.mm.yy-dd.mm.yy)");
						String dates = scan.nextLine();
						String[] split = dates.split("-");
						SimpleDateFormat formatData = new SimpleDateFormat("dd.MM.yy");
						Date iniData = formatData.parse(split[0]);
						Date endData = formatData.parse(split[1]);
						
						System.out.println("Which category would you like to consult?");
						String idCategory = scan.nextLine();

						MovementProcessSingleton movementProcess = MovementProcessSingleton.getInstance();
						TotalizationResult totalizations = movementProcess.getTotalizations(iniData, endData, idCategory);
						printResultPointTwo(totalizations);
						option = true;

					} else if (pos == 3) { // Delete all movements
						MovementProcessSingleton movementProcess = MovementProcessSingleton.getInstance();
						movementProcess.deleteMovements();
						option = true;
						
					} else if(pos == 0) {
						LOGGER.info(EXIT);
						System.exit(0);
						
					} else {
						LOGGER.info(OPTION_NO_AVAILABLE);
						option = true;
					}
				}
			}

			/**
			 * Print result to point 2
			 * 
			 * @param totalizations
			 */
			private void printResultPointTwo(TotalizationResult totalizations) {
				if (totalizations.getCategory() != null) {
					printRecursivePointTwo(totalizations.getCategory(), totalizations.getTotMap(), 0);
				}
			}

			/**
			 * Recursive method. Print result to point 2.
			 */
			private void printRecursivePointTwo(CategoryData category, Map<CategoryData, Double> totMap, int pos) {
				System.out.println(getTab(pos) + category.getDescription() + " >> " +  totMap.get(category));
				List<CategoryData> childrenList = category.getChildrenList();
				if (childrenList != null) {
					++pos;
					for (CategoryData children : childrenList) {
						printRecursivePointTwo(children, totMap, pos);
					}
				}
			}

			private String getTab(int pos) {
				String result = "";
				for(int i = 0; i < pos; i++){
					result += "\t";
				}
				return result;
			}

			/**
			 * Method to create the main menu
			 * @param scan 
			 */
			private void getMenu(Scanner scan) {
				System.out.println("****************************************");
				System.out.println("MENU");
				System.out.println("0. Exit program.");
				System.out.println("1. Load Movements in database.");
				System.out.println("2. Get totalizations by date.");
				System.out.println("3. Detele all movements from database.");
				System.out.println("****************************************");
				
				// Get the option selected
				int pos = Integer.valueOf(scan.nextLine());
				// Activate the option
				try {
					getMenuOption(pos, scan);
					// If no error, to show the menu
					getMenu(scan);
				} catch (Exception e) {
					LOGGER.error(ERROR_PROCESS, e);
					getMenu(scan);
				}
			}
		});
	}
}
