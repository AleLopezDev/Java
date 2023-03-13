package peval1acda2223;

import java.util.Scanner;
import java.io.*;

/**
 * @version 1.0
 * @author Alejandro Lopez Aguilar
 */

public class EJ1 {

	static Busqueda bus = new Busqueda();

	/**
	 * Metodo para detectar si existe Companies.txt en un directorio
	 */
	public void detetectarArchivo() {

		Scanner sc = new Scanner(System.in);

		System.out.println("Introduzca una ruta: ");
		String ruta = sc.next();

		File f = new File(ruta);
		bus.busquedaArchivo(f);
	}

}