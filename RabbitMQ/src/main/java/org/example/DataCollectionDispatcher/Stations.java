package org.example.DataCollectionDispatcher;

public class Stations {
    private int id;
    private String db_url;
    private float latitude, longitude;

    //constructor
    public Stations(int id, String db_url, float latitude, float longitude) {
        this.id = id;
        this.db_url = db_url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //getters
    public int getId() {
        return id;
    }

    public String getDb_url() {
        return db_url;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDb_url(String db_url) {
        this.db_url = db_url;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    //toString
    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", db_url='" + db_url + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
