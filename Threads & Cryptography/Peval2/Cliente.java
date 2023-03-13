package peval2prsp2223;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Clase Cliente - Crea un cliente y lo conecta al servidor por el puerto 5200
 * solo podra conectarse si el servidor esta abierto.
 * 
 * @author Alejandro Lopez Aguilar
 * @Date 21/11/2022
 * 
 */

public class Cliente {

	private static DataInputStream leer;
	private static DataOutputStream escribir;
	private static Scanner sc = new Scanner(System.in);
	private final static int PUERTO = 5200;
	private static Servidor s = new Servidor();

	public static void main(String[] args) {
		try {
			// Creamos el socket del cliente y lo conectamos al servidor
			Socket cliente = new Socket("localhost", PUERTO);

			// Abrimos canal de lectura & escritura
			leer = new DataInputStream(cliente.getInputStream());
			escribir = new DataOutputStream(cliente.getOutputStream());

			// Leemos el numero de conexiones
			int numeroConexiones = leer.readInt();

			// Si el numero de conexiones es menor que 3
			if (numeroConexiones < 4) {

				// Leera las opciones que existen enviadas por el servidor
				String opciones = leer.readUTF();

				// Imprimimos las opciones
				System.out.println(opciones);

				// Enviamos la opcion escogidas de entre las 3 disponibles
				int opcion = sc.nextInt();
				escribir.writeInt(opcion);

				switch (opcion) {

				case 1:
					// Obtenemos el mensaje del nombre
					System.out.println(leer.readUTF());

					// Enviamos el nombre al servidor
					String nombre = sc.next();
					escribir.writeUTF(nombre);

					// Recibimos el mensaje enviado por el servidor
					System.out.println(leer.readUTF());

					int mensajeSalida = 0;
					while (mensajeSalida != 9) {

						// Obtenemos las cartas leidas y la opcion de salir
						System.out.println(leer.readUTF());

						// Enviamos nuestra opcion al servidor
						mensajeSalida = sc.nextInt();
						escribir.writeInt(mensajeSalida);

					}

					break;
				case 2:
					// Recibimos el primer mensaje
					System.out.println(leer.readUTF());

					String nombreMeeting = sc.next();
					escribir.writeUTF(nombreMeeting);

					// Recibimos la bienvenida
					String bienvenida = leer.readUTF();
					System.out.println(bienvenida);

					String respuesta = "";

					while (!respuesta.contains(".")) {
						// Recibimos la pregunta realizada
						System.out.println(leer.readUTF());
						// Enviamos la respuesta
						respuesta = sc.next();
						escribir.writeUTF(respuesta);
					}

					break;
				case 3:

					// Leemos el mensaje de bienvenida
					System.out.println(leer.readUTF());

					for (int i = 0; i < 4; i++) {

						// Leemos la pregunta recibida
						System.out.println(leer.readUTF());

						// Escribimos nuestra respuesta
						String respuestaCompra = sc.next();
						escribir.writeUTF(respuestaCompra);
					}

					// Obtenemos la cantidad de articulos comprados
					System.out.println("Has comprado un total de " + leer.readInt() + " articulos");
					break;
				}

				// Obtenemos el mensaje de desconexion
				System.out.println("\n" + leer.readUTF());

				// Recibimos el dinero a pagar
				System.out.println("Tienes que pagar un total de " + String.format("%.2f", leer.readDouble()) + " €");

				// Cerramos los canales
				cliente.close();
				leer.close();

			} else {
				
				// Mostramos el error y reducimos las conexiones
				System.out.println("Actualmente ya existen 3 usuarios conectados");
				s.setConexiones(numeroConexiones - 1);
				
				// Cerramos los canales
				cliente.close();
				leer.close();
				escribir.close();

			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
