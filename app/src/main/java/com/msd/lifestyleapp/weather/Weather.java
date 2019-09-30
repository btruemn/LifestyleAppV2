package com.msd.lifestyleapp.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable {

    @SerializedName("coord")
    @Expose
    public Coords coord;
    @SerializedName("weather")
    @Expose
    public List<WeatherInfo> wi = null;
    @SerializedName("base")
    @Expose
    public String base;
    @SerializedName("main")
    @Expose
    public Temperature temperature;
    @SerializedName("visibility")
    @Expose
    public Integer visibility;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("dt")
    @Expose
    public Integer dt;
    @SerializedName("sys")
    @Expose
    public Sys sys;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("cod")
    @Expose
    public Integer cod;

    @Override
    public String toString() {
        return "Weather{" +
                "coord=" + coord +
                ", weather=" + wi +
                ", base='" + base + '\'' +
                ", temperature=" + temperature +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", dt=" + dt +
                ", sys=" + sys +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                '}';
    }

}
