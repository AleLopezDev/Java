package net.codejava.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Scanner;
/**
 * 
 * @author Alex
 *
 */
public class Manager {

	protected static SessionFactory sessionFactory;
	static Scanner sc = new Scanner(System.in);

	protected void setup() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings
																									// from
																									// hibernate.cfg.xml
				.build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception ex) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}

	protected void exit() {
		sessionFactory.close();
	}

	protected void darAltaPasaje() {

		int codPasajero = -1;
		Session session = sessionFactory.openSession();

		Pasaje pasaje = new Pasaje();

		// Obtenemos el ultimo codigo de pasaje
		Query query = session.createQuery("SELECT MAX(cod) FROM Pasaje");
		List list = query.list();
		int cod = (int) list.get(0);
		pasaje.setCod(cod + 1);

		// Mostramos una lista con los codigos de los pasajeros
		query = session.createQuery("SELECT cod FROM Pasajero");
		list = query.list();
		System.out.println("Codigos de pasajeros disponibles:");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Codigo " + list.get(i));
		}

		// Insertamos el código del pasajero y comprobamos que exista
		while (codPasajero == -1) {
			System.out.println("\nIntroduzca el codigo del pasajero: ");
			codPasajero = sc.nextInt();
			Pasajero pasajero = session.get(Pasajero.class, codPasajero);
			if (pasajero == null) {
				System.out.println("El pasajero no existe");
				codPasajero = -1;
			} else {
				pasaje.setPasajeroCod(codPasajero);
			}
		}

		// Mostramos una lista con los identificadores de los vuelos
		query = session.createQuery("SELECT identificador FROM Vuelo");
		list = query.list();
		System.out.println("Identificadores de vuelos disponibles:");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Identificador " + list.get(i));
		}

		// Insertamos el identificador
		int identificador = -1;
		String identificadorVuelo = "";
		while (identificador == -1) {
			System.out.println("\nIntroduzca el identificador del vuelo: ");
			identificadorVuelo = sc.next();
			identificadorVuelo = identificadorVuelo.toUpperCase();
			Vuelo vuelo = session.get(Vuelo.class, identificadorVuelo);
			if (vuelo == null) {
				System.out.println("El vuelo no existe");
				identificador = -1;
			} else {
				pasaje.setIdentificador(identificadorVuelo);
				identificador = 0;
			}
		}

		// Insertamos el número de asiento
		int num = -1;
		while (num == -1) {
			System.out.println("Introduzca el numero de asiento");
			int numAsiento = sc.nextInt();

			// Obtenemos todos los asientos ocupados para el identificador de vuelo
			String hql = "FROM Pasaje P WHERE P.identificador = :identificadorVuelo";
			Query query2 = session.createQuery(hql);
			query2.setParameter("identificadorVuelo", identificadorVuelo);
			List<Pasaje> result = query2.list();

			// Verificamos si el asiento que el usuario quiere insertar ya está ocupado
			boolean ocupado = false;
			for (Pasaje p : result) {
				if (p.getNumAsiento() == numAsiento) {
					ocupado = true;
					break;
				}
			}
			if (!ocupado) {
				// El asiento no está ocupado
				pasaje.setNumAsiento(numAsiento);
				num = 0;
			} else {
				System.out.println("El asiento ya esta ocupado para este vuelo");
				num = -1;
			}
		}

		// Insertamos la clase
		System.out.println("Introduzca la clase");
		String clase = sc.next();
		pasaje.setClase(clase);

		// Insertamos el PVP
		System.out.println("Introduzca el PVP");
		int pvp = sc.nextInt();
		pasaje.setPvp(pvp);

		session.beginTransaction();
		session.save(pasaje);
		session.getTransaction().commit();
		session.close();
	}

	protected void consultaVuelo() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		String hqlIdentificador = "FROM Vuelo";
		Query<Vuelo> queryIdentificador = session.createQuery(hqlIdentificador, Vuelo.class);
		List<Vuelo> listaIdentificadores = queryIdentificador.list();

		System.out.println("Identificadores de vuelos disponibles:");
		for (Vuelo vuelo : listaIdentificadores) {
			System.out.println(vuelo.getIdentificador());
		}
		System.out.println("Introduce el identificador del vuelo: ");
		String identificador = sc.next();

		String hqlVuelo = "FROM Vuelo v WHERE v.identificador = :identificador";
		Query<Vuelo> queryVuelo = session.createQuery(hqlVuelo, Vuelo.class);
		queryVuelo.setParameter("identificador", identificador);
		List<Vuelo> listaVuelos = queryVuelo.list();

		if (listaVuelos.size() > 0) {
			Vuelo vuelo = listaVuelos.get(0);
			System.out.println("VUELO: " + vuelo.getIdentificador());
			System.out.println("ORIGEN: " + vuelo.getAeropuerto_origen() + " DESTINO: " + vuelo.getAeropuerto_destino()
					+ " FECHA: " + vuelo.getFecha());

			String hqlPasajes = "FROM Pasaje p WHERE p.identificador = :identificador order by clase";
			Query<Pasaje> queryPasajes = session.createQuery(hqlPasajes, Pasaje.class);
			queryPasajes.setParameter("identificador", identificador);
			List<Pasaje> listaPasajes = queryPasajes.list();

			if (listaPasajes.size() > 0) {
				System.out.println("Pasajes:");
				String clase = "";
				for (Pasaje pasaje : listaPasajes) {
					if (!clase.equals(pasaje.getClase())) {
						clase = pasaje.getClase();
						System.out.println("\tCLASE: " + pasaje.getClase());
					}

					int codPasajero = pasaje.getPasajeroCod();
					Pasajero pasajero = session.get(Pasajero.class, codPasajero);

					System.out.println("\t\tNombre pasajero: " + pasajero.getNombre() + " Codigo Pasaje: "
							+ pasaje.getCod() + " Numero de asiento: " + pasaje.getNumAsiento());
				}
			} else {
				System.out.println("No se han encontrado pasajes para ese vuelo");
			}

		} else {
			System.out.println("No se ha encontrado un vuelo con ese identificador");
		}

		session.getTransaction().commit();
		session.close();
	}

	protected void actualizarPasajero() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.createQuery("SELECT cod FROM Pasajero");
		List list = query.list();
		System.out.println("Codigos de pasajeros disponibles:");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Codigo " + list.get(i));
		}

		String nombre = "", telefono = "", direccion = "", pais = "";

		System.out.println("Introduce el codigo del pasajero");
		int codPasajero = sc.nextInt();

		Pasajero pasajero = session.get(Pasajero.class, codPasajero);

		sc.nextLine();

		if (pasajero == null) {
			System.out.println("El pasajero no existe");
		} else {
			while (nombre.equals("")) {
				System.out.println("Introduce el nuevo nombre");
				nombre = sc.nextLine();
			}
			pasajero.setNombre(nombre);

			while (telefono.equals("")) {
				System.out.println("Introduce el nuevo telefono");
				telefono = sc.next();
			}
			pasajero.setTelefono(telefono);

			sc.nextLine();

			while (direccion.equals("")) {
				System.out.println("Introduce la nueva direccion");
				direccion = sc.nextLine();
			}
			pasajero.setDireccion(direccion);

			try {
				while (pais.equals("")) {
					System.out.println("Introduce el nuevo pais");
					pais = sc.nextLine();
				}
				pasajero.setPais(pais);
				session.update(pasajero);
				session.getTransaction().commit();
			} catch (Exception ex) {
				System.out.println("El pais no puede tener más de 15 caracteres");
			}

		}
	}

	protected void darBajaPasajesDePasajero() {

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query queryCod = session.createQuery("SELECT cod FROM Pasajero");
		List list = queryCod.list();
		System.out.println("Codigos de pasajeros disponibles:");
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Codigo " + list.get(i));
		}

		System.out.println("Introduce el código del pasajero para dar de baja sus pasajes");
		int codPasajero = sc.nextInt();

		String hql = "FROM Pasaje WHERE pasajeroCod = :codPasajero";
		Query query = session.createQuery(hql);
		query.setParameter("codPasajero", codPasajero);
		List<Pasaje> pasajes = query.list();

		if (pasajes.isEmpty()) {
			System.out.println("No existen pasajes para este pasajero");
			return;
		}

		for (Pasaje pasaje : pasajes) {
			session.delete(pasaje);
		}

		System.out.println("Pasajes eliminados correctamente");
		session.getTransaction().commit();
		session.close();
	}

	public void visualizarImporteTotalVuelo(String identificadorVuelo) {
		int importeTotal = 0;
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Pasaje where identificador = :identificador");
		query.setParameter("identificador", identificadorVuelo);
		List<Pasaje> pasajes = query.getResultList();
		if (pasajes.size() > 0) {
			for (Pasaje pasaje : pasajes) {
				importeTotal += pasaje.getPvp();
			}
			System.out.println(
					"El importe total recaudado en el vuelo " + identificadorVuelo + " es: " + importeTotal + " euros");
		} else {
			System.out.println("No se han encontrado pasajes para ese vuelo");
		}
		session.close();
	}

	public static void main(String[] args) {
		Manager manager = new Manager();

		// do the CRUD operations
		manager.setup();

		int opcion = 0;
		while (opcion != 99) {
			System.out.println(
					"1 - Dar de alta un pasaje\n2 - Visualizar datos de pasaje\n3 - Actualizar datos de un pasajero\n4 - Dar de baja los pasajes de un pasajero\n5 - Visualizar importe total recaudado en un vuelo\n99 - Salir\nTeclee opcion: ");
			opcion = sc.nextInt();

			switch (opcion) {
			case 1:
				manager.darAltaPasaje();
				break;
			case 2:
				manager.consultaVuelo();
				break;
			case 3:
				manager.actualizarPasajero();
				break;
			case 4:
				manager.darBajaPasajesDePasajero();
				break;
			case 5:

				Session session = sessionFactory.openSession();
				session.beginTransaction();

				String hqlIdentificador = "FROM Vuelo";
				Query<Vuelo> queryIdentificador = session.createQuery(hqlIdentificador, Vuelo.class);
				List<Vuelo> listaIdentificadores = queryIdentificador.list();

				System.out.println("Identificadores de vuelos disponibles:");
				for (Vuelo vuelo : listaIdentificadores) {
					System.out.println(vuelo.getIdentificador());
				}

				System.out.println("Introduce el identificador del vuelo");
				String identificador = sc.next();
				manager.visualizarImporteTotalVuelo(identificador);
				break;
			case 99:
				System.out.println("Saliendo...");
				opcion = 99;
				break;
			}
		}

		manager.exit();
	}
}
