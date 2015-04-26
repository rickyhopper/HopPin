package com.rhxp.hoppin.model;

/**
 * Created by xavierprimus on 4/26/15.
 */
public class Geo {
    private double[] coordinates = new double[2];

    public Geo(double[] coordinates){
        this.coordinates = coordinates;
    }

    public double getLat(){
        return coordinates[0];
    }

    public double getLon(){
        return coordinates[1];
    }

    public double[] getCoordinates(){
        return this.coordinates;
    }
    @Override
    public String toString() {
        return "Geo [coordinates=[" + coordinates[0] + ", " + coordinates[1] + "]";
    }
}
