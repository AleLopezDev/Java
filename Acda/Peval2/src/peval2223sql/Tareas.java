package peval2223sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * @author alex Clase tareas, la cual contiene todos los métodos que se
 *         necesitan para la realizacion del ejercicio recibe por parametro en
 *         su constructor la conexion
 */
public class Tareas {

	private static ResultSet ultimoNumeroEmpleado, nombreJugador;
	private static Statement sentencia;
	private static int numeroMaxEmpleado;
	private static Scanner sc = new Scanner(System.in);

	public Tareas(Statement sentencia) {
		this.sentencia = sentencia;

	}

	// Ejercicio 1
	/**
	 * Permite la insercción de nuevos fichajes a traves de un txt, el cual le
	 * pasamos la ruta por parametro
	 */
	public static void insertarNuevosFichajes() {

		System.out.println("Introduce la ruta que contiene a fichajes.txt");
		String ruta = sc.next();
		File f = new File(ruta + File.separator + "fichajes.txt");

		if (!f.exists()) {
			System.out.println("No existe\n");
		} else {

			try {
				BufferedReader leer = new BufferedReader(new FileReader(f));

				String linea = "";
				while ((linea = leer.readLine()) != null) {

					String[] lin = linea.split(";");

					try {

						nombreJugador = sentencia
								.executeQuery("Select nombre from jugadores where nombre = '" + lin[0] + "'");

						if (nombreJugador.next()) {
							System.out.println("\nYa existe el jugador " + lin[0]);
						} else {
							ultimoNumeroEmpleado = sentencia.executeQuery("SELECT MAX(codigo) FROM jugadores");
							ultimoNumeroEmpleado.next();
							numeroMaxEmpleado = ultimoNumeroEmpleado.getInt(1) + 1;
							sentencia.executeUpdate(
									"insert into jugadores values(" + numeroMaxEmpleado + ",'" + lin[0] + "','" + lin[1]
											+ "','" + lin[2] + "'," + lin[3] + ",'" + lin[4] + "','" + lin[5] + "')");
							ultimoNumeroEmpleado.close();
						}

						nombreJugador.close();
					} catch (SQLIntegrityConstraintViolationException e1) {
						// TODO Auto-generated catch block
						System.out.println("El equipo " + lin[5] + " no existe en la tabla equipos\n");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		sc.nextLine();

	}

	// Ejercicio 2
	/**
	 * Permite insertar partidos en el que enviamos a sus caracterisiticas
	 */
	public static void insertarPartidos() {

		ResultSet ultimoCodigo, equipoLocal, equipoVisitante;

		int pLocal = 0, pVisitante = 0;
		String eLocal, eVisitante;

		try {

			ultimoCodigo = sentencia.executeQuery("SELECT MAX(codigo) FROM partidos");
			ultimoCodigo.next();
			int codigo = ultimoCodigo.getInt(1) + 1;

			System.out.println("Introduce el equipo local");
			eLocal = sc.nextLine();

			// Ambos equipos tienen que existir
			equipoLocal = sentencia.executeQuery("select nombre from equipos where nombre like '" + eLocal + "'");

			while (!equipoLocal.next()) {
				System.out.println("No existe el equipo");
				System.out.println("Introduce el equipo local");
				eLocal = sc.nextLine();
				equipoLocal = sentencia.executeQuery("select nombre from equipos where nombre like '" + eLocal + "'");
			}

			System.out.println("Introduce el equipo visitante");
			eVisitante = sc.nextLine();
			equipoVisitante = sentencia
					.executeQuery("select nombre from equipos where nombre like '" + eVisitante + "'");

			while (!equipoVisitante.next()) {
				System.out.println("Introduce el equipo visitante");
				eVisitante = sc.next();
				equipoVisitante = sentencia
						.executeQuery("select nombre from equipos where nombre like '" + eVisitante + "'");
			}

			while (pLocal <= 0) {
				System.out.println("Introduce los puntos del local");
				pLocal = sc.nextInt();
			}

			sc.nextLine();

			while (pVisitante <= 0) {
				System.out.println("Introduce los puntos del visitante");
				pVisitante = sc.nextInt();
			}

			sc.nextLine();

			System.out.println("Introduce la temporada");
			String temporada = sc.next();

			sentencia.executeUpdate("insert into partidos values(" + codigo + ",'" + eLocal + "','" + eVisitante + "',"
					+ pLocal + "," + pVisitante + ",'" + temporada + "')");
			
			System.out.println("Partido insertado con exito");

			equipoLocal.close();
			equipoVisitante.close();
			ultimoCodigo.close();

		} catch (InputMismatchException e) {
			System.out.println("Introduce correctamente los valores");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.nextLine();

	}

	// Ejercicio 3
	/**
	 * Permite mostrar datos técnicos de los jugadores de una ciudad
	 */
	public static void mostrarDatosJugadores() {
		ResultSet existeCiudad, datosJugadores;

		System.out.println("\nIntroduce la ciudad a mostrar");
		String ciudad = sc.next();

		try {
			existeCiudad = sentencia.executeQuery("Select ciudad from equipos where ciudad like '" + ciudad + "'");

			if (existeCiudad.next()) {
				datosJugadores = sentencia.executeQuery(
						"SELECT jugadores.Nombre,Altura,Peso,Posicion,Nombre_equipo from jugadores,equipos where jugadores.Nombre_equipo = equipos.Nombre and ciudad like'"
								+ ciudad + "'");
				while (datosJugadores.next()) {
					System.out.println(datosJugadores.getString(1) + " - " + datosJugadores.getString(2) + " - "
							+ datosJugadores.getInt(3) + " - " + datosJugadores.getString(4) + " - "
							+ datosJugadores.getString(5));
				}
				System.out.println();
				datosJugadores.close();
			} else {
				System.out.println("\nNo existe la ciudad\n");
			}

			existeCiudad.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sc.nextLine();

	}

	// Ejercicio 4
	/**
	 * Permite visualizar los partidos jugados tanto como de local como de visitante
	 * del jugador que le paseos por scanner
	 */
	public static void visualizarNumeroPartidos() {
		ResultSet jugador, partidosLocal, partidosVisitantes;
		System.out.println("Introduce el nombre del jugador");
		String nombreJugador = sc.nextLine();

		try {
			jugador = sentencia.executeQuery("select Nombre from jugadores WHERE Nombre like '" + nombreJugador + "'");

			// Si existe el nombre del jugador
			if (jugador.next()) {

				partidosLocal = sentencia.executeQuery(
						"select equipo_local, count(temporada),temporada from jugadores,equipos,partidos WHERE partidos.equipo_local = equipos.Nombre and equipos.Nombre = jugadores.Nombre_equipo and jugadores.Nombre = '"
								+ nombreJugador + "' group by temporada");

				while (partidosLocal.next()) {
					System.out.println("Equipo Local:" + partidosLocal.getString(1) + " - " + " Temporada:"
							+ partidosLocal.getString(3) + " - " + " Num partidos:" + partidosLocal.getInt(2));
				}

				partidosLocal.close();

				partidosVisitantes = sentencia.executeQuery(
						"select equipo_visitante, count(temporada),temporada from jugadores,equipos,partidos WHERE partidos.equipo_visitante = equipos.Nombre and equipos.Nombre = jugadores.Nombre_equipo and jugadores.Nombre = '"
								+ nombreJugador + "' group by temporada");

				System.out.println("\n");

				while (partidosVisitantes.next()) {
					System.out.println("Equipo Visitante:" + partidosVisitantes.getString(1) + " - " + " Temporada:"
							+ partidosVisitantes.getString(3) + " - " + " Num partidos:"
							+ partidosVisitantes.getInt(2));
				}

				partidosVisitantes.close();

			} else {
				System.out.println("NO existe el jugador\nPresiona Enter para continuar");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.nextLine();

	}

	// Ejercicio 5
	/**
	 * Actualiza automaticamente la posicion de aquellas jugadores que cumplan
	 * ciertos requisitios
	 */
	public static void actualizarPosicion() {
		try {
			sentencia.executeUpdate(
					"update jugadores,equipos set jugadores.posicion = 'pivote' where jugadores.Posicion like 'pivot' and jugadores.Nombre_equipo = equipos.Nombre and equipos.Division like 'Pacific' and equipos.Conferencia like 'West'");
			System.out.println("Posicion cambiada con exito");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Ejercicio 6
	/**
	 * Permite borrar todos los datos de los equipos así como de los jugadores que
	 * pertenezcan a ese equipo
	 */
	public static void eliminarDatosEquipo() {
		ResultSet equipo;
		System.out.println("Introduce el nombre del equipo a borrar");
		String nombreEquipo = sc.nextLine();

		try {
			// Los jugadores pertenecientes a ese equipo tambien se borran
			equipo = sentencia.executeQuery("select Nombre from equipos where Nombre like '" + nombreEquipo + "'");

			if (equipo.next()) {
				sentencia.executeUpdate("delete from equipos where Nombre like '" + nombreEquipo + "'");
				System.out.println("Equipo borrado con éxito\nPresiona enter para continuar");
			} else {
				System.out.println("No existe el equipo");
			}
			equipo.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sc.nextLine();
	}

}
