package peval2prsp2223;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase Servidor - Inicia un servidor en el puerto 5200, está a la escucha de
 * nuevas peticiones siempre el número de conexiones sea menor que 3.
 * 
 * @author Alejandro Lopez Aguilar
 * @Date 21/11/2022
 * 
 */

public class Servidor {

	private static int conexiones = 0;

	public static void main(String[] args) throws IOException {
		final int PUERTO = 5200;
		ServerSocket servidor;
		Socket sc;
		DataOutputStream escribir;

		servidor = new ServerSocket(PUERTO);
		System.out.println("Servidor iniciado");

		// Siempre va a estar a la escucha de nuevas peticiones
		while (!servidor.isClosed()) {

			// Este va a ser el cliente
			sc = new Socket();

			// Se acepta el cliente
			sc = servidor.accept();
			conexiones++;
			if (conexiones < 4) {
				System.out.println("Usuario Conectado");
			}

			// Enviamos las conexiones que existen al cliente
			escribir = new DataOutputStream(sc.getOutputStream());
			escribir.writeInt(conexiones);

			// Creamos el hilo de enlace entre el cliente y servidor
			HiloMultiServidor h = new HiloMultiServidor(sc, conexiones);
			Thread t = new Thread(h);
			t.start();

		}
	}

	public static int getConexiones() {
		return conexiones;
	}

	public static void setConexiones(int conexiones) {
		Servidor.conexiones = conexiones;
	}

}
