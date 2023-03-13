package peval1acda2223;

import java.io.*;
import java.util.Scanner;

/**
 * @version 1.0
 * @author Alejandro Lopez Aguilar
 */

public class EJ2 {

	static Busqueda bus = new Busqueda();

	/**
	 * Metodo que renombra aquellas lineas que contengan "Purchasing" por "Investor"
	 * 
	 * @throws IOException
	 */
	public void renombrarArchivo() throws IOException {

		Scanner sc = new Scanner(System.in);

		System.out.println("Introduzca una ruta: ");
		String ruta = sc.next();

		File f = new File(ruta);

		boolean existe = bus.busquedaArchivo(f);

		if (existe) {

			String rutaArchivo = bus.devolverRuta();

			// Archivo de lectura
			File f1 = new File(rutaArchivo);

			// Archivos de lectura
			BufferedReader leer = new BufferedReader(new FileReader(rutaArchivo));

			// Archivos de escritura
			File f2 = new File(ruta + File.separator + "Companiestmp.txt");

			BufferedWriter escribir = new BufferedWriter(new FileWriter(f2));
			System.out.println();

			// Leer de un fichero y escribir en el otro
			String linea;

			while ((linea = leer.readLine()) != null) {
				if (linea.contains("Purchasing")) {

					escribir.write(linea.replace("Purchasing", "Investor"));
				} else {
					escribir.write(linea);
				}
				escribir.newLine();

			}

			leer.close();
			escribir.close();

			if (!f1.delete()) {
				System.out.println("fallo al borrar");
			}
			if (!f2.renameTo(f1)) {
				System.out.println("fallo al renombrar");
			}

		} else {
			System.out.println("No existe el archivo");
		}

	}

}