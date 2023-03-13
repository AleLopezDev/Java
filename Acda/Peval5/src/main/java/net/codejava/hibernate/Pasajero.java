package net.codejava.hibernate;

import javax.persistence.*;

@Entity
@Table(name = "pasajero")
public class Pasajero {

    private int cod;
    private String nombre;
    private String telefono;
    private String direccion;
    private String pais;

    public Pasajero() {
    }

    @Id
    @Column(name = "COD")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Column(name = "NOMBRE")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "TLF")
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Column(name = "DIRECCION")
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Column(name = "PAIS")
    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
