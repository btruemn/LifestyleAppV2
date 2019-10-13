package com.msd.lifestyleapp.weather;


import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.sql.SQLOutput;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRepository {

    private Retrofit rf;
    private JSONWeatherAPI jsonWeatherAPI;
    private Call<Weather> weatherCall;
    private String apiKey = "008e52012c1bf318c1d1b7f66e5e363d";
    private MutableLiveData<Weather> weather = new MutableLiveData<>();


    public WeatherRepository() {
        rf = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonWeatherAPI = rf.create(JSONWeatherAPI.class);
    }

    public LiveData<Weather> getWeather(String postalCode) {

        weatherCall = jsonWeatherAPI.getWeather(postalCode, "US", "Imperial", apiKey);
        System.out.println("Weather Call Established");
        weatherCall.enqueue(new Callback<Weather>() {

            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                }
                weather.postValue(response.body());
                System.out.println("Success");
            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        return weather;
    }
}
