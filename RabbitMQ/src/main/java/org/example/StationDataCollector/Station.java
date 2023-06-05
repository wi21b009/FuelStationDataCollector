package org.example.StationDataCollector;

public class Station {
    private int id;
    private float kwh;
    private int custmer_id;

    //constructor
    public Station(int id, float kwh, int custmer_id) {
        this.id = id;
        this.kwh = kwh;
        this.custmer_id = custmer_id;
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

    //toString
    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", kwh='" + kwh + '\'' +
                ", custmer_id=" + custmer_id +
                '}';
    }
}
