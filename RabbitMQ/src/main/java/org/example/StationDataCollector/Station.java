package org.example.StationDataCollector;

public class Station {
    public int id;
    public float kwh;
    public int custmer_id;

    public int port;

    //constructor
    public Station(int id, float kwh, int custmer_id, int port) {
        this.id = id;
        this.kwh = kwh;
        this.custmer_id = custmer_id;
        this.port = port;
    }

    //getters
    public int getId() {
        return id;
    }

    public float getKwh() {
        return kwh;
    }

    public int getCustomer_id() {
        return custmer_id;
    }

    public int getPort() {
        return port;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setKwh (int kwh) {
        this.kwh = kwh;
    }

    public void setCustomer_id (int custmer_id) {
        this.custmer_id = custmer_id;
    }

    public void setPort (int port) {
        this.port = port;
    }

    //toString
    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", kwh='" + kwh + '\'' +
                ", custmer_id=" + custmer_id +
                ", port =" + port +
                '}';
    }
}
