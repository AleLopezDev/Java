package net.codejava.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "vuelo")
public class Vuelo {

    private String identificador;
    private String aeropuerto_origen;
    private String aeropuerto_destino;
    private String tipoVuelo;
    private Date fecha;

    public Vuelo() {
    }

    @Id
    @Column(name = "IDENTIFICADOR")
    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    @Column(name = "AEROPUERTO_ORIGEN")
    public String getAeropuerto_origen() {
        return aeropuerto_origen;
    }

    public void setAeropuerto_origen(String aeropuerto_origen) {
        this.aeropuerto_origen = aeropuerto_origen;
    }

    @Column(name = "AEROPUERTO_DESTINO")
    public String getAeropuerto_destino() {
        return aeropuerto_destino;
    }

    public void setAeropuerto_destino(String aeropuerto_destino) {
        this.aeropuerto_destino = aeropuerto_destino;
    }

    @Column(name = "TIPO_VUELO")
    public String getTipoVuelo() {
        return tipoVuelo;
    }

    public void setTipoVuelo(String tipoVuelo) {
        this.tipoVuelo = tipoVuelo;
    }

    @Column(name = "FECHA_VUELO")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
