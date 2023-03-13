package peval1acda2223;

import java.io.Serializable;

/**
 * @version 1.0
 * @author Alejandro Lopez Aguilar
 */

public class Investor implements Serializable {

	private int codigo;
	private char company;
	private String name, surname, calidad, type;

	public Investor(int codigo, char company, String name, String surname, String calidad, String type) {

		this.codigo = codigo;
		this.company = company;
		this.name = name;
		this.surname = surname;
		this.calidad = calidad;
		this.type = type;

	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public char getCompany() {
		return company;
	}

	public void setCompany(char company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCalidad() {
		return calidad;
	}

	public void setCalidad(String calidad) {
		this.calidad = calidad;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}