package peval2prsp2223;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Clase HiloMultiServidor - Puente entre el Servidor y el Cliente, muestra una
 * lista de opciones a las cuales el usuario se puede conectar
 * 
 * @author Alejandro Lopez Aguilar
 * @Date 21/11/2022
 * 
 */

public class HiloMultiServidor implements Runnable {

	String[] futurologia = { "Vas a tener 2 hijos", "Adoptaras 2 perros", "Tu equipo favorito ganará",
			"Este verano te va a regalar un yate", "Vas a tener mucha salud", "Seras feliz",
			"Conoceras a una persona famosa", "Tendras mucho dinero", "Seras lider de un proyecto importante" };

	String[] meeting = { "¿Cual es tu deporte favorito?", "¿Te gustan los animales?",
			"¿Prefieres el futbol o baloncesto?", "¿Cual es tu color favorito?",
			"¿Qué genero de musica te apasiona mas?", "¿Sueles entrenar mas de 2 veces por semana?",
			"¿Eres mas de perros o gatos?", "¿Qué te gusta hacer al despertar?" };

	String[] compras = { "¿Desea comprar el shampoo?", "¿Desea comprar el portatil?", "¿Desea Comprar el lapicero?",
			"¿Desea comprar la goma?" };

	Socket cliente;
	DataInputStream leer;
	DataOutputStream escribir;
	long tiempoInicio = 0, tiempoConectado;
	Servidor server = new Servidor();
	int conexiones;

	/**
	 * @param cliente    - tipo Socket - contiene el cliente que se ha conectado
	 *                   previamente
	 * @param conexiones - tipo Int - numero de conexiones existentes
	 */
	public HiloMultiServidor(Socket sc, int conexiones) {
		this.cliente = sc;
		this.conexiones = conexiones;
		try {
			// Abrimos los canales de lectura y escritura relacionado con el cliente
			escribir = new DataOutputStream(sc.getOutputStream());
			leer = new DataInputStream(sc.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		if (conexiones < 4) {
			try {
				// Mostramos al usuario todas las opciones disponibles
				String mensaje = "GESTION DEL CALL CENTER\n1 - Futurología\n2 - Meeting\n3 - Compras\nTeclee opcion: ";

				// Enviamos el mensaje
				escribir.writeUTF(mensaje);

				// Recogemos la opcion seleccionada por el usuario
				int opcionRecogida = leer.readInt();

				// Procedemos a hacer la funcion dependiendo de la opcion recibida
				switch (opcionRecogida) {

				case 1:

					// Iniciamos el tiempo
					tiempoInicio = System.currentTimeMillis();

					// Solicitamos el nombre al usuario
					escribir.writeUTF("Escribe tu nombre: ");

					// Recogemos el nombre
					String nombre = leer.readUTF();

					// Enviamos
					escribir.writeUTF("Bienvenido " + nombre + " voy a leerte las cartas");

					int opcionSalida = 0;
					while (opcionSalida != 9) {

						int opcionRandom = (int) (Math.random() * futurologia.length) + 0;

						// Envia un mensaje random del array
						escribir.writeUTF(futurologia[opcionRandom]
								+ "\nPulsa cualquier numero para continuar | Pulsa 9 - Para Salir");

						opcionSalida = leer.readInt();
					}

					break;
				case 2:
					// Iniciamos el tiempo
					tiempoInicio = System.currentTimeMillis();

					escribir.writeUTF("¿Hola como te llamas?");

					// Recibimos el nombre
					String nombreRecibido = leer.readUTF();

					escribir.writeUTF("De acuerdo " + nombreRecibido
							+ " te voy a hacer un par de preguntas\nRecuerda pulsar '.' para salir de la llamada");

					String mensajeMeeting = "";
					while (!mensajeMeeting.contains(".")) {
						// Escoge una pregunta aleatoria
						int opcionRandom = (int) (Math.random() * meeting.length) + 0;

						// Envia esa pregunta al cliente
						escribir.writeUTF(meeting[opcionRandom]);

						// Recoge la respuesta del cliente
						mensajeMeeting = leer.readUTF();

					}
					break;
				case 3:
					// Iniciamos el tiempo
					tiempoInicio = System.currentTimeMillis();

					int numeroArticulos = 0;

					escribir.writeUTF("Responda con 'si' o 'no' ");

					// Realizamos las 4 preguntas en orden
					for (int i = 0; i < compras.length; i++) {

						// Enviamos al cliente las preguntas
						escribir.writeUTF(compras[i]);

						// Recibimos la respuesta por parte del cliente
						String respuesta = leer.readUTF();

						if (respuesta.equalsIgnoreCase("si")) {
							numeroArticulos++;
						}
					}

					// Mostramos la cantidad de articulos comprados
					escribir.writeInt(numeroArticulos);
					break;

				}

				// Obtenemos el tiempo conectado con una formula
				tiempoConectado = (System.currentTimeMillis() - tiempoInicio) / 1000;

				// Enviamos al usuario los datos de conexion
				escribir.writeUTF("Has sido desconectado del servidor");

				// Enviamos al usuario el tiempo a pagar
				escribir.writeDouble((tiempoConectado * 1.20) / 60);

				// El servidor recibe que el cliente ha sido desconectado
				System.out.println("\nCliente desconectado");

				System.out.println("\tTiempo conectado: " + tiempoConectado + " sg ");
				System.out.println("\tIp del usuario: " + cliente.getInetAddress());
				System.out.println("\tPuerto local conectado: " + cliente.getLocalPort());
				System.out.println("\tPuerto conectado: " + cliente.getPort());
				System.out.println("\t" + cliente.toString() + "\n");

				// Reducimos las conexiones
				server.setConexiones(server.getConexiones() - 1);

				// Cerramos los canales
				cliente.close();
				leer.close();
				escribir.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			try {
				// Reducimos en -1 las conexiones
				server.setConexiones(server.getConexiones() - 1);

				// Cerramos los canales
				cliente.close();
				leer.close();
				escribir.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
