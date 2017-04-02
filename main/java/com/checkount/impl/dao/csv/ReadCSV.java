package com.checkount.impl.dao.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.checkount.data.movement.MovementData;
import com.opencsv.CSVReader;

public class ReadCSV {

	/** Logger */
	private static Logger logger = LogManager.getLogger(ReadCSV.class);

	/** Log messages */
	private static final String ERROR_READ_FILE = "Error reading the movements";
	private static final String ERROR_OPEN_FILE = "Error opening the movement's file";

	public static final char SEPARATOR = ';';
	public static final char QUOTE = '"';
	private static final String DEFAULT_FILE = "main/resources/movements/MOV20160101_201603030.csv";

	/**
	 * Get the movements from csv file
	 * 
	 * @return List of movements
	 */
	public List<MovementData> csvreader(String path) {

		if (path == null) {
			path = DEFAULT_FILE;
		}

		File archivo = new File(path);
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(archivo);
		} catch (FileNotFoundException e) {
			logger.error(ERROR_OPEN_FILE, e);
		}

		List<MovementData> movements = new LinkedList<MovementData>();
		CSVReader csvReader = new CSVReader(fileReader, SEPARATOR, QUOTE);

		try {
			String[] nextLine = null;
			while ((nextLine = csvReader.readNext()) != null) {
				String fechaEjecucion = nextLine[0];
				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
				String descripcion = nextLine[1];
				String importe = nextLine[2];
				String idMoviment = fechaEjecucion + "#" + descripcion;

				DecimalFormat df = new DecimalFormat("#,###.#");
				String importFormat = df.parse(importe).toString();

				// Create movement
				MovementData primerLectura = new MovementData(idMoviment, format.parse(fechaEjecucion), descripcion,
						Double.valueOf(importFormat));
				movements.add(primerLectura);
			}

		} catch (IOException e) {
			logger.error(ERROR_READ_FILE, e);
		} catch (ParseException e) {
			logger.error(ERROR_READ_FILE, e);
		}
		return movements;
	}
}
