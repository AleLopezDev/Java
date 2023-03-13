package peval1acda2223;

import java.io.File;

/**
 * @version 1.0
 * @author Alejandro Lopez Aguilar
 */

public class Busqueda {
	boolean existe = false;
	String ruta;

	/**
	 * Metodo a través del cual pasamos un fichero para saber si existe
	 * 
	 * @param tipo File
	 * @return false, cuando no existe
	 * @return true cuando existe
	 */
	public boolean busquedaArchivo(File f) {
		File[] listaArchivos = f.listFiles();

		if (listaArchivos != null) {
			for (File i : listaArchivos) {
				if (i.getName().equalsIgnoreCase("Companies.txt")) {
					System.out.println();
					System.out.println("Companies existe y esta en la ruta: " + i);
					existe = true;
					ruta = i.getPath();
				}
				busquedaArchivo(i);
			}

		}

		
		
		return existe;

	}

	/**
	 * Metodo que devuelve la ruta del fichero
	 * 
	 * @return ruta
	 */
	public String devolverRuta() {
		return ruta;
	}

}