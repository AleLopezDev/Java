package peval3;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.ObjectValues;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Values;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.And;
import org.neodatis.odb.core.query.criteria.ICriterion;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

/**
 * Clase la cual contiene todos los métodos necesarios para la ejecucion del
 * programa
 * 
 * @author Alejandro Lopez - 2ºDAM
 * @date 16/11/2022
 * 
 */
public class Tareas {

	private static Scanner sc = new Scanner(System.in);
	// Sentencia para trabajar sobre la bd sql
	private Statement sentencia;

	/**
	 * Constructor de Tareas
	 * 
	 * @param sentencia - tipo Statement - Contiene el statement proveniente del
	 *                  Menu
	 */
	public Tareas(Statement sentencia) {
		this.sentencia = sentencia;
	}

	/**
	 * Ejercicio 1 Método que permite transformar una base de datos sql a neodatis
	 */
	public void transformarNeodatis() {

		// Archivo donde se va guardar la biblioteca.neo
		// File f = new File(ruta + File.separator + "biblioteca.neo");
		File f = new File("D:\\peval3acda2223\\biblioteca.neo");
		if (f.exists()) {
			f.delete();
		}

		ODB odb = ODBFactory.open(f.getAbsolutePath()); // Abrir BD

		try {
			// Obtenemos todos los libros
			ResultSet libros = sentencia.executeQuery("Select * from libros");

			// Guardamos en la bd neodatis los libros
			while (libros.next()) {
				odb.store(new Libros(libros.getInt(1), libros.getString(2), libros.getString(3), libros.getString(4),
						libros.getString(5), libros.getString(6), libros.getInt(7), libros.getInt(8),
						libros.getString(9)));
			}

			// Obtenemos todos los usuarios
			ResultSet usuario = sentencia.executeQuery("select * from usuario");

			// Guardamos en la bd neodatis los usuarios
			while (usuario.next()) {
				Usuario u = new Usuario(usuario.getInt(1), usuario.getString(2), usuario.getString(3),
						usuario.getString(4), usuario.getString(5), usuario.getString(6), usuario.getString(7),
						usuario.getString(8));
				odb.store(u);
			}

			// Obtenemos todos los prestamos
			ResultSet prestamos = sentencia.executeQuery("select * from prestamos");

			while (prestamos.next()) {

				// Consulta para buscar el codigo de libro y recuperamos el libro con dicho
				// codigo para
				// posteriormente meterlo en la bd neodatis
				CriteriaQuery consultaCodigo = new CriteriaQuery(Libros.class,
						Where.equal("codigo", prestamos.getInt(2)));
				Objects<Libros> libro = odb.getObjects(consultaCodigo);
				Libros l = (Libros) libro.getFirst();

				// Consulta para buscar el codigo de usuario y recuperamos el usuario con digo
				// codigo para meterlo en la bd neodatis
				CriteriaQuery consultaUsuario = new CriteriaQuery(Usuario.class,
						Where.equal("codigoUsuario", prestamos.getInt(3)));
				Objects<Usuario> user = odb.getObjects(consultaUsuario);
				Usuario u = (Usuario) user.getFirst();

				// Guardamos los datos
				odb.store(new Prestamos(prestamos.getInt(1), l, u, prestamos.getString(4), prestamos.getString(5),
						prestamos.getString(6)));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Cerramos la bd neodatis
		odb.close();
	}

	/*
	 * Ejercicio 2 método el cual permite dar de alta un libro, el codigo se
	 * autoincrementa automaticamente
	 */
	public void altaLibros() throws SQLException {

		ODB odb = ODBFactory.open("D:\\peval3acda2223\\biblioteca.neo");

		// Obtenemos el valor del ultimo código
		Values val3 = odb.getValues(new ValuesCriteriaQuery(Libros.class).max("codigo"));
		ObjectValues v = val3.next();
		BigDecimal codLibro = (BigDecimal) (v.getByIndex(0));

		// Solicitamos todos los parametros necesarios para dar de alta el libro
		System.out.println("Introduce el nombre de libro:");
		String nombre = sc.nextLine();

		System.out.println("Introduce la editorial: ");
		String editorial = sc.nextLine();

		System.out.println("Nombre Autor: ");
		String nAutor = sc.nextLine();

		System.out.println("Pais Autor: ");
		String pais = sc.nextLine();

		System.out.println("Número de paginas");
		int nPaginas = sc.nextInt();

		System.out.println("Año de edicion");
		int year = sc.nextInt();

		// Limpiamos el buffer
		sc.nextLine();

		System.out.println("Precio Libro");
		String precioLibro = sc.nextLine();

		// Guardamos el libro
		odb.store(new Libros(codLibro.intValue() + 1, nombre, editorial, nAutor, pais, nAutor, nPaginas, year,
				precioLibro));
		System.out.println("\nLibro insertado con exito");

		// Cerramos la base de datos
		odb.close();
	}

	/**
	 * Ejercicio 3 - Método para dar de baja un usuario, al cual le pasamos el
	 * nombre
	 */

	public void bajaUsuarios() {

		ODB odb = ODBFactory.open("D:\\peval3acda2223\\biblioteca.neo");

		// Imprime una lista de usuarios
		CriteriaQuery consultaVerLista = new CriteriaQuery(Usuario.class);
		Objects<Usuario> listaUsuario = odb.getObjects(consultaVerLista);

		while (listaUsuario.hasNext()) {
			Usuario u = listaUsuario.next();
			System.out.println("Nombre: " + u.getNombre());
		}

		// Solicitamos el nombre
		System.out.println("Introduce el nombre a borrar");
		String name = sc.nextLine();

		// Consulta para recuperar el usuario
		CriteriaQuery comprobarNombre = new CriteriaQuery(Usuario.class, Where.equal("nombre", name));
		Objects<Usuario> nombre = odb.getObjects(comprobarNombre);

		// Si existe
		if (nombre.hasNext()) {
			// Consulta para obtener todos los prestamos que coincidan con el nombre de
			// usuario
			CriteriaQuery borradoPrestamo = new CriteriaQuery(Prestamos.class, Where.equal("Usuario.nombre", name));
			Objects<Prestamos> listaPrestamos = odb.getObjects(borradoPrestamo);
			// Recorremos toda la lista obtenida y procedemos a borrar los prestamos
			while (listaPrestamos.hasNext()) {
				Prestamos p = listaPrestamos.next();
				odb.delete(p);
			}

			// Consulta para obtener todos los Usuarios que coincidan con el nombre
			CriteriaQuery borrado = new CriteriaQuery(Usuario.class, Where.equal("nombre", name));
			Objects<Usuario> listaUsuarios = odb.getObjects(borrado);

			// Recorremos la lista obtenida y borramos
			while (listaUsuarios.hasNext()) {
				Usuario u = listaUsuarios.next();
				odb.delete(u);
			}

			System.out.println("Usuario y sus prestamos borrados con éxito\n");

		} else {
			System.out.println("No existe\n");
		}

		// Cerramos la base de datos
		odb.close();
	}

	/**
	 * Ejercicio 4 - Método el cual permite modificar un prestamo, al usuario se le
	 * otorgará una lista de opciones para que elija
	 */
	public void modificarPrestamo() {
		Prestamos p = null;
		ODB odb = ODBFactory.open("D:\\peval3acda2223\\biblioteca.neo");

		// Imprimimos la lista de prestamos
		CriteriaQuery consultaPrestamos = new CriteriaQuery(Prestamos.class);
		Objects<Prestamos> listaPrestamos = odb.getObjects(consultaPrestamos);
		while (listaPrestamos.hasNext()) {
			p = (Prestamos) listaPrestamos.next();
			System.out.println(p);
		}

		// Obtenemos el prestamo seleccionado
		System.out.println("Elija  prestamo: ");
		int nPrestamo = sc.nextInt();
		CriteriaQuery nombrePrestamo = new CriteriaQuery(Prestamos.class, Where.equal("numeroPedido", nPrestamo));
		Objects<Prestamos> prestamoSeleccionado = odb.getObjects(nombrePrestamo);

		// Seleccionamos dicho prestamo
		if (prestamoSeleccionado.hasNext()) {
			System.out.println("Prestamo seleccionado actualmente: " + nPrestamo);
			p = prestamoSeleccionado.getFirst();

			// Mostramos un menu con las opciones para cambiar
			System.out.println(
					"1 - Cambiar Usuario\n2 - Cambiar Libro\n3 - Fecha Salida\n4 - Fecha Max Devolucion\n5 - Fecha Devolucion");
			int opcionSeleccionada = sc.nextInt();

			sc.nextLine();
			switch (opcionSeleccionada) {

			case 1:
				// Imprimimos una lista con todos los nombres de usuarios
				CriteriaQuery listaDisponibleUsuario = new CriteriaQuery(Usuario.class);
				Objects<Usuario> listaDispoUsuario = odb.getObjects(listaDisponibleUsuario);
				System.out.println("Lista de Nombres: ");
				while (listaDispoUsuario.hasNext()) {
					Usuario u1 = listaDispoUsuario.next();
					System.out.println(u1.getNombre());
				}

				// Solicitamos el nombre de usuario
				System.out.println("Introduce el nombre de Usuario: ");
				String nombreUsuario = sc.nextLine();
				// Realizamos consultas correspondientes
				CriteriaQuery consultaNombre = new CriteriaQuery(Usuario.class, Where.equal("nombre", nombreUsuario));
				Objects<Usuario> listaUsuarios = odb.getObjects(consultaNombre);

				// Recorremos toda la lista de usuario que tenga ese nombre
				while (listaUsuarios.hasNext()) {
					Usuario u = (Usuario) listaUsuarios.next();
					System.out.println(u);

					// Con el prestamo seleccionado anteriormente, realizamos un set para cambiar el
					// usuario
					p.setUsuario(u);
					odb.store(p); // Guardamos de nuevo el usuario
					System.out.println("Modificacion realizada con exito");
				}
				break;
			case 2:

				// Imprimimos una lista con los nombres disponilbes
				CriteriaQuery listaDisponible = new CriteriaQuery(Libros.class);
				Objects<Libros> listaDispo = odb.getObjects(listaDisponible);
				System.out.println("Lista de Nombres: ");
				while (listaDispo.hasNext()) {
					Libros l1 = listaDispo.next();
					System.out.println(l1.getNombre());
				}

				// Solicitamos el nombre del libro
				System.out.print("\nIntroduce el nombre del Libro: ");
				String nombreLibro = sc.nextLine();

				// Realizamos las consultas correspondientes
				CriteriaQuery consultaLibro = new CriteriaQuery(Libros.class, Where.equal("nombre", nombreLibro));
				Objects<Libros> listaLibros = odb.getObjects(consultaLibro);

				// Recorremos la lista de libros con dicho nombre
				while (listaLibros.hasNext()) {
					Libros l = (Libros) listaLibros.next();
					System.out.println(l);

					// Con el prestamo seleccionado anteriormente, realizamos un set para cambiar el
					// libro
					p.setLibro(l);
					odb.store(p); // Guardamos de nuevo el libro
					System.out.println("Modificacion realizada con exito");
				}
				break;
			case 3:
				// Solicitamos la fecha de salida nueva
				System.out.println("Introduce la fecha de salida nueva");
				String fechaSalida = sc.nextLine();

				// Guardamos en la base de datos la nueva fecha
				p.setFechaSalida(fechaSalida);
				odb.store(p);
				break;

			case 4:
				// Solicitamos la fecha maxima de devolucion nueva
				System.out.println("Introduce la fecha maxima de devolucion: ");
				String fechaMaximaDevolucion = sc.nextLine();

				// Guardamos en la base de datos la nueva fecha
				p.setFechaMaxDevolucion(fechaMaximaDevolucion);
				odb.store(p);
				break;
			case 5:
				// Solicitamos la fecha de devolucion nueva
				System.out.println("Introduce la fecha de devolucion");
				String fechaDevol = sc.nextLine();

				// Guardamos en la base de datos la nueva fecha
				p.setFechaDevolucion(fechaDevol);
				odb.store(p);
				break;
			}

		} else {
			System.out.println("No existe ese prestamo");
		}

		// Cerramos la conexion con la base de datos
		odb.close();
	}

	/**
	 * Ejercicio 5 - Método el cual muestra todos aquellos prestamos realizados con
	 * retraso, introduciendo el codigo de usuario
	 * 
	 * @throws ParseException
	 */
	public static void prestamosRetraso() throws ParseException {

		ODB odb = ODBFactory.open("D:\\peval3acda2223\\biblioteca.neo");

		// Imprimir lista usuarios
		System.out.println("Lista de usuarios: ");
		CriteriaQuery consultaUsuario = new CriteriaQuery(Usuario.class);
		Objects<Usuario> listaUsuarios = odb.getObjects(consultaUsuario);
		while (listaUsuarios.hasNext()) {
			Usuario u = listaUsuarios.next();
			System.out.println(u.getCodigoUsuario() + " - " + u.getNombre());
		}

		// Solicitamos el codigo de usuario
		System.out.println("Introduce el codigo usuario: ");
		int codUsuario = sc.nextInt();

		// Consulta que devuelte todos los prestamos
		CriteriaQuery consultaPrestamo = new CriteriaQuery(Prestamos.class,
				Where.equal("Usuario.codigoUsuario", codUsuario));
		Objects<Prestamos> listaPrestamos = odb.getObjects(consultaPrestamo);

		// Recorremos todos los prestamos obtenidos
		while (listaPrestamos.hasNext()) {
			Prestamos p = (Prestamos) listaPrestamos.next();
			Date fechaMax = new SimpleDateFormat("dd/MM/yyyy").parse(p.getFechaMaxDevolucion());
			Date fechaDev = new SimpleDateFormat("dd/MM/yyyy").parse(p.getFechaDevolucion());

			// Comparamos si la fecha de Devolucion viene despues de la fecha maxima
			// permitida
			if (fechaDev.after(fechaMax)) {
				System.out.println("El prestamo con id: " + p.getNumeroPedido() + " esta entregado con retraso ");
			}
		}
		System.out.println();
		// Limpiamos buffer
		sc.nextLine();
		odb.close(); // Cerramos conexion
	}

	/**
	 * Ejercicio 6 - Método el cual solicitamos el género de un libro y un precio
	 * tope y comparamos si ese libro tiene un precio inferior al tope dado
	 * 
	 * @throws ParseException
	 */
	public static void libroGenero() throws ParseException {

		ODB odb = ODBFactory.open("D:\\peval3acda2223\\biblioteca.neo");

		CriteriaQuery consultaListo = new CriteriaQuery(Libros.class);
		Objects<Libros> listaGeneros = odb.getObjects(consultaListo);

		System.out.println("Generos disponibles");
		while (listaGeneros.hasNext()) {

			Libros l = (Libros) listaGeneros.next();
			System.out.println(l.getGenero());

		}

		// Solicitamos el genero del libro
		System.out.println("Introduce el genero del libro: ");
		String genero = sc.nextLine();

		// Solicitamos el precio tope
		System.out.println("Introduce el precio tope: ");
		double precioTope = sc.nextInt();

		// Realizamos una consulta y obtenemos todos los libros del genero pedido
		// anteriormente
		CriteriaQuery consultaLibro = new CriteriaQuery(Libros.class, Where.equal("genero", genero));
		Objects<Libros> listaLibros = odb.getObjects(consultaLibro);

		// Recorremos todos los libros obtenidos
		while (listaLibros.hasNext()) {
			Libros l = listaLibros.next();
			String nuevoPrecio = "";

			// Si tiene un precio mayor a 1000, reemplazamos el punto por un espacio en
			// blanco y posteriormente la coma por un punto
			if (l.getPrecioLibro().contains(".")) {
				nuevoPrecio = l.getPrecioLibro().replace(".", "");
				if (nuevoPrecio.contains(",")) {
					nuevoPrecio = nuevoPrecio.replace(",", ".");
				}
				// Si su precio es inferior, cambiamos la coma por un punto
			} else {
				nuevoPrecio = l.getPrecioLibro().replace(",", ".");
			}

			if (Double.parseDouble(nuevoPrecio) < precioTope) {
				System.out.println("Precio del libro: " + l.getPrecioLibro());
				System.out.println("El libro con id " + l.getCodigo() + " tiene un precio inferior al tope "
						+ "Nombre - " + l.getNombre());
			}

		}
		System.out.println("\n");
		sc.nextLine(); // Limpiamos el buffer
		odb.close(); // Cerramos la base de datos
	}

	/**
	 * Ejercicio 7 - Muestra los prestamos realizados entre dos fechas
	 * 
	 * @throws ParseException
	 */
	public static void prestamosRealizados() throws ParseException {

		Date inicioPedida = null, finPedida = null;
		ODB odb = ODBFactory.open("D:\\peval3acda2223\\biblioteca.neo");

		// Solicitamos la provincia
		System.out.println("Introduce la provicia: ");
		String provincia = sc.nextLine();

		// Solicitamos la fecha de inicio
		System.out.println("Introduce la fecha de inicio: ");
		String fechaInicio = sc.nextLine();

		// Solicitamos la fecha de fin
		System.out.println("Introduce la fecha de fin: ");
		String fechaFin = sc.nextLine();

		// Consulta que retorna la lista de prestamos cuyo usuario coincida con la
		// provincia solicitada previemente
		ICriterion consulta = new And().add(Where.equal("Usuario.provincia", provincia));
		IQuery query = new CriteriaQuery(Prestamos.class, consulta);
		Objects<Prestamos> lista = odb.getObjects(query);

		// Parseamos a date las fechas pedidas
		try {
			inicioPedida = new SimpleDateFormat("dd/MM/yyyy").parse(fechaInicio);
			finPedida = new SimpleDateFormat("dd/MM/yyyy").parse(fechaFin);
		} catch (ParseException e) {
			System.out.println("Introduce un fornmato de fecha correcto");
		}

		// Recorremos la lista de prestamos obtenido en la consutla
		while (lista.hasNext()) {
			Prestamos p = lista.next();

			// Parseamos las fechas del prestamo
			Date fechaSalida = new SimpleDateFormat("dd/MM/yyyy").parse(p.getFechaSalida());
			Date fechaDevol = new SimpleDateFormat("dd/MM/yyyy").parse(p.getFechaDevolucion());

			// Si esta en medio de las fechas solicitadas imprime el prestamo completo
			if (fechaSalida.after(inicioPedida) && fechaDevol.before(finPedida)) {
				System.out.println(p);
			}
		}

		// Cerramos la base de datos
		odb.close();
	}
}