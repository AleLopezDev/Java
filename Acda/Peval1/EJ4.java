package peval1acda2223;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

/**
 * @version 1.0
 * @author Alejandro Lopez Aguilar
 */

public class EJ4 {

	/**
	 * Metodo que escribe un archivo xml usando un archivo de objetos creado
	 * previamente
	 * 
	 * @throws Exception
	 */
	public void escribirXML() throws Exception {

		// Lectura
		File lecturaArchivo = new File("C:\\Users\\usuario\\Desktop\\AcdaFicheros\\practicaEvaluable\\investor.dat");
		if (lecturaArchivo.exists()) {
			ObjectInputStream lectura = new ObjectInputStream(new FileInputStream(lecturaArchivo));

			DocumentBuilderFactory miFactoria = DocumentBuilderFactory.newInstance(); // Definicion de la factoria DOM
			DocumentBuilder miConstructor = miFactoria.newDocumentBuilder(); // Definicion del Constructor Dom
			DOMImplementation implementacion = miConstructor.getDOMImplementation(); // Interfaz DOM

			// Creacion del documento
			Document miDocumento = implementacion.createDocument(null, "companies", null);
			miDocumento.setXmlVersion("1.0");

			Investor i;

			try {

				while (true) {

					i = (Investor) lectura.readObject();

					// Creacion de la etiqueta investors
					Element investors = miDocumento.createElement("investors");

					// Creacion de la etiqueta investor
					Element investor = miDocumento.createElement("investor");
					investor.setAttribute("type", i.getType());

					// Creacion de la etiqueta company
					Element company = miDocumento.createElement("company");
					Text companyText = miDocumento.createTextNode(String.valueOf(i.getCompany()));
					company.appendChild(companyText);
					investor.appendChild(company);

					// Creacion Personal data
					Element personalData = miDocumento.createElement("personaldata");

					// Nombre y apellidos

					Element nombre = miDocumento.createElement("Nombre");
					Text textNombre = miDocumento.createTextNode(i.getName());
					nombre.appendChild(textNombre);
					personalData.appendChild(nombre);

					Element apellido = miDocumento.createElement("Apellido");
					Text textApellido = miDocumento.createTextNode(i.getSurname());
					apellido.appendChild(textApellido);
					personalData.appendChild(apellido);

					investor.appendChild(personalData);
					investors.appendChild(investor);
					miDocumento.getDocumentElement().appendChild(investors);

				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			// Creación y grabación del fichero
			Source source = new DOMSource(miDocumento);
			Result resultado = new StreamResult(
					new File("C:\\Users\\usuario\\Desktop\\AcdaFicheros\\practicaEvaluable\\companies.xml"));
			Transformer miTransformer = TransformerFactory.newInstance().newTransformer();
			miTransformer.transform(source, resultado);
			lectura.close();
			System.out.println("Archivo creado correctamente");
		} else {
			System.out.println("No existe el archivo o esta incorrectamente escrito");
		}
	}

}
