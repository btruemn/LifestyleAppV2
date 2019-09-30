package com.msd.lifestyleapp.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Clouds implements Serializable {

    @SerializedName("all")
    @Expose
    private Integer all;

    @Override
    public String toString() {
        return "Clouds{" +
                "all=" + all +
                '}';
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }
}
