package com.msd.lifestyleapp.weather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class WeatherViewModel extends AndroidViewModel {

    private LiveData<Weather> weather;
    private WeatherRepository weatherRepository;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository();

    }

    public LiveData<Weather> getWeather(String postalCode) {
        if (weather == null) { weather = weatherRepository.getWeather(postalCode);}
        return weatherRepository.getWeather(postalCode);
    }
}
