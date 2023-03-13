package peval1acda2223;

import java.io.*;

/**
 * @version 1.0
 * @author Alejandro Lopez Aguilar
 */

public class EJ3 {

	static String type;

	/**
	 * Metodo que escribe en formato txt a formato .dat, haciendo uso de objetos
	 * 
	 * @throws Exception
	 * 
	 */
	public void escribirObjeto() throws Exception {

		File lectura = new File("C:\\Users\\usuario\\Desktop\\AcdaFicheros\\practicaEvaluable\\Companies.txt");

		if (lectura.exists()) {
			// Lectura
			BufferedReader leer = new BufferedReader(new FileReader(lectura));

			// Escritura
			FileOutputStream fOut = new FileOutputStream(
					new File("C:\\Users\\usuario\\Desktop\\AcdaFicheros\\practicaEvaluable\\investor.dat"));

			ObjectOutputStream escribir = new ObjectOutputStream(fOut);

			String linea;

			while ((linea = leer.readLine()) != null) {

				String[] lin = linea.split(";");

				// Obtenemos el codigo en forma de char
				char codigo = lin[1].charAt(lin[1].length() - 1);

				// Obetenemos el tipo y vamos comprobando si tiene punto final, si es owner ...
				String palabra = lin[4];

				if (palabra.equals("Owner")) {

					type = "";

				} else {

					// Si la ultima palabra tiene punto final
					if (palabra.substring(palabra.length() - 1).equals(".")) {
						type = palabra.substring(palabra.length() - 4, palabra.length() - 1);

					} else {

						type = palabra.substring(palabra.length() - 3, palabra.length());

					}

				}

				// codigo - codigo compañia - nombre - apellido - calidad - tipo

				escribir.writeObject(new Investor(Integer.parseInt(lin[0]), codigo, lin[2], lin[3], lin[4], type));

			}
			System.out.println();
			System.out.println("Archivo creado");
		} else {
			System.out.println("No existe el archivo o esta incorrectamente escrito");
		}

	}

}