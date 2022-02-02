package com.nestorchacin.coordinates;

import static java.lang.Math.*;

public class Coordinates {
    private static final double RADIUS = 6371230;
    private static final double MAX_DISCREP = 100;
    private static final double MATCH_THRESHOLD = pow(MAX_DISCREP / (RADIUS * PI/180), 2);
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

    /**
     * <p>The main assumption here, is that a small angle approximation is sufficient to compute a suitable comparison, and
     * the expression can be manipulated to make it less computationally expensive as follows:</p>
     *
     * <p>Assuming the Earth is a perfect sphere, the shortest distance between two points on the surface will lie along a
     * great circle around the sphere (a circle created by the intersection of the sphere and plane that passes through
     * its center point. Thus,</p>
     *
     * <p>d = r * theta .....  (1)</p>
     *
     * <p>where theta is the smallest angle between the two points, AKA the central angle. This angle can be approximated
     * by the following equation using the planar approximation (Assuming the objects lie on flat surface since they are
     * so close together):</p>
     *
     * <p>theta = sqrt(((lat2 - lat1) * (PI / 180)) ^ 2 + ((lon2 - lon1) * (PI / 180) * cos(lat2 * PI / 180)) ^ 2) .... (2)</p>
     *
     * <p>substituting for theta in (2) with (1) and squaring both sides,</p>
     *
     * <p>(d /( r * PI / 180))^2 = (lat2 - lat1) ^ 2 + ((lon2 - lon1) * cos(lat2 * PI / 180)) ^ 2</p>
     *
     * <p>And since d and r are given as constants, the left-hand side becomes a threshold to
     * determine the return value.</p>
     *
     * @param coordinate
     * @return True if coordinates are within threshold, False otherwise.
     */
    public Boolean isMatch(Coordinates coordinate) {
        double value;
        double cosval = pow(cos(Math.toRadians(coordinate.latitude)), 2);
        value = pow(coordinate.latitude - this.latitude, 2) + pow(coordinate.longitude - this.longitude, 2) * cosval;
        return (value < MATCH_THRESHOLD);
    }
}