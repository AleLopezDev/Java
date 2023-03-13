package net.codejava.hibernate;

import javax.persistence.*;

@Entity
@Table(name = "pasaje")
public class Pasaje {
    private int cod;
    private int pasajeroCod;
    private String identificador;
    private int numAsiento;
    private String clase;
    private int pvp;

    public Pasaje() {
    }

    @Id
    @Column(name = "COD")
    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Column(name = "PASAJERO_COD")
    public int getPasajeroCod() {
        return pasajeroCod;
    }

    public void setPasajeroCod(int pasajeroCod) {
        this.pasajeroCod = pasajeroCod;
    }

    @Column(name = "IDENTIFICADOR")
    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    @Column(name = "NUMASIENTO")
    public int getNumAsiento() {
        return numAsiento;
    }

    public void setNumAsiento(int numAsiento) {
        this.numAsiento = numAsiento;
    }

    @Column(name = "CLASE")
    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    @Column(name = "PVP")
    public int getPvp() {
        return pvp;
    }

    public void setPvp(int pvp) {
        this.pvp = pvp;
    }
}
