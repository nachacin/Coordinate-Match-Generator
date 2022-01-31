package com.nestorchacin.coordinates;

public class Coordinates {
    private int id;
    private double latitude;
    private double longitude;
    private Source source;

    public Coordinates (int id, double latitude, double longitude, Source source) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.source = source;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", source=" + source +
                '}';
    }
}
