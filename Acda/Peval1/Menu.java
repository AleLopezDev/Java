package peval1acda2223;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @version 1.0
 * @author Alejandro Lopez Aguilar
 */

public class Menu {

	static int opcion = 0;
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

		while (opcion != 9) {

			System.out.println(
					"1 - Opcion 1 \n2 - Opcion 1 y 2\n3 - Opcion 1 y 3\n4 - Opcion 4\n5 - Opcion 5\nPulsa 9 para salir");
			try {

				opcion = sc.nextInt();

			} catch (InputMismatchException e) {
				System.out.println("Introduce una opcion correcta ");
				break;
			}

			switch (opcion) {

			case 1:

				EJ1 ej1 = new EJ1();
				ej1.detetectarArchivo();
				break;

			case 2:
				EJ2 ej2 = new EJ2();

				try {
					ej2.renombrarArchivo();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			case 3:
				EJ3 ej3 = new EJ3();
				try {
					ej3.escribirObjeto();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;

			case 4:

				EJ4 ej4 = new EJ4();
				try {
					ej4.escribirXML();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			case 5:
				EJ5 ej5 = new EJ5();
				try {
					ej5.lecturaXML();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			}

			System.out.println();

		}

	}

}
