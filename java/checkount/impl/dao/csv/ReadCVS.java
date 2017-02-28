package checkount.impl.dao.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.csvreader.CsvReader;

import checkount.data.movement.MovementData;

public class ReadCVS {

	private static final String ENCABEZADOS_LEIDOS_CORRECTAMENTE = "Encabezados leidos correctamente";
	private static final String ERROR_AL_LEER_VALORES_DEL_ARCHIVO_CVS = "Error al leer valores del archivo CVS";
	private static final String ERROR_AL_LEER_EL_ENCABEZADO_DEL_ARCHIVO_CVS = "Error al leer el encabezado del archivo CVS";
	private static final String ARCHIVO_CVS_NO_ENCONTRADO = "Ruta desconocida del archivo CVS: ";
	private static final String ARCHIVO_CVS_LEIDO = "Archivo CVS leido correctamente: ";

	/**
	 * Logger
	 */
	private static final Logger logger = Logger.getLogger(ReadCVS.class
			.getPackage().getName());
	

	public static List<MovementData> cvsreader() {
		
		/**
		 * Lista donde se almacenan los datos leidos del CVS.
		 */
		List<MovementData> datos = new LinkedList<MovementData>();

		File archivo = new File(
				"resources/MOV20160101_201603030.csv");
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(archivo);
			logger.log(Level.INFO, ARCHIVO_CVS_LEIDO + archivo.getPath());
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE,
					ARCHIVO_CVS_NO_ENCONTRADO + archivo.getPath());
			e.printStackTrace();
		}
		CsvReader csvReader = new CsvReader(fileReader, ';');

		// Leemos los encabezados
		String[] parametros = null;
		try {
			if (csvReader.readHeaders()) {
				parametros = csvReader.getHeaders(); 
			}
			System.out.println(parametros[0] + " \t " + parametros[1] + " \t " + parametros[2]);
			logger.log(Level.INFO, ENCABEZADOS_LEIDOS_CORRECTAMENTE);
		} catch (IOException e) {
			logger.log(Level.WARNING,
					ERROR_AL_LEER_EL_ENCABEZADO_DEL_ARCHIVO_CVS);
			e.printStackTrace();
		}

		try {
			while (csvReader.readRecord()) {
				
				try {
					String fechaEjecucion = csvReader.get("Fecha");
					SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
					String descripcion = csvReader.get("Concepto");
					String importe = csvReader.get("Importe");
					String idMoviment = fechaEjecucion + "#" + descripcion;
							
					DecimalFormat df = new DecimalFormat("#,###.#");
					String importFormat = df.parse(importe).toString();
					MovementData primerLectura = new MovementData(idMoviment, format.parse(fechaEjecucion), descripcion, Double.valueOf(importFormat));
					datos.add(primerLectura);
					System.out.println(fechaEjecucion + " \t " + descripcion + " \t " + importe);
				} catch(ParseException e) {
					System.out.println("Error to parse");
				}

			}
		} catch (IOException e) {
			logger.log(Level.WARNING, ERROR_AL_LEER_VALORES_DEL_ARCHIVO_CVS);
			e.printStackTrace();
		}
		return datos;
	}

}
