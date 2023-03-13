package net.codejava.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * @author alexrose
 * @version 1.0
 * Clase Conexion - Clase que se encarga de realizar la conexion con la base de datos
 */
public class Conexion {
    MongoDatabase db;
    // Url de conexion = mongodb+srv://alexrose:alelopez123@cluster0.n49txyt.mongodb.net/test
    public Conexion() {
        MongoClient cliente = MongoClients.create("mongodb+srv://alexrose:alelopez123@cluster0.n49txyt.mongodb.net/?retryWrites=true&w=majority");
        db = cliente.getDatabase("recetas");
    }

    public MongoDatabase getDb() {
        return db;
    }

    public void setDb(MongoDatabase db) {
        this.db = db;
    }
}
