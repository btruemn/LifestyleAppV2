package com.msd.lifestyleapp.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coords {


    @SerializedName("lon")
    @Expose
    public Double lon;
    @SerializedName("lat")
    @Expose
    public Double lat;

    @Override
    public String toString() {
        return "Coords{" +
                "lon=" + lon +
                ", lat=" + lat +
                '}';
    }

    public Double getLon() {
            return lon;
        }

    public void setLon(Double lon) {
            this.lon = lon;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

}
