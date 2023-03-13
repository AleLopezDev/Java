package peval1acda2223;

import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.*;

import org.w3c.dom.*;

/**
 * @version 1.0
 * @author Alejandro Lopez Aguilar
 */

public class EJ5 {

	private static Scanner sc = new Scanner(System.in);
	private static String ruta = "";

	/**
	 * Metodo que permite la lectura de un archivo xml creado previamente
	 * 
	 * @throws Exception
	 */
	public void lecturaXML() throws Exception {

		System.out.println("Introduce la ruta que contiene el archivo companies.xml");
		ruta = sc.next();

		File f = new File(ruta + File.separator + "companies.XML");

		if (f.exists()) {

			DocumentBuilderFactory miFactoria = DocumentBuilderFactory.newInstance();
			DocumentBuilder constructor = miFactoria.newDocumentBuilder();
			Document documento = constructor.parse(f);

			System.out.println();
			System.out.println(documento.getDocumentElement().getNodeName().toUpperCase());
			System.out.println("-----------------------------");

			NodeList nList = documento.getElementsByTagName("investor");

			for (int i = 0; i < nList.getLength(); i++) {

				Node nNode = nList.item(i);
				Element x = (Element) nNode;
				// System.out.println("Elemento actual: " + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) nNode;

					System.out.println("Tipo: " + e.getAttribute("type"));
					System.out.println("Compañia: " + e.getElementsByTagName("company").item(0).getTextContent());
					System.out.println("Nombre: " + e.getElementsByTagName("Nombre").item(0).getTextContent());
					System.out.println("Apellido: " + e.getElementsByTagName("Apellido").item(0).getTextContent());
				}

				System.out.println();

			}
		} else {
			System.out.println("No existe");
		}

	}
}
