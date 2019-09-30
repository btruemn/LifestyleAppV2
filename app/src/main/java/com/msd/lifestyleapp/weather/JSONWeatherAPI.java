package com.msd.lifestyleapp.weather;

import com.msd.lifestyleapp.weather.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JSONWeatherAPI {

        @GET("2.5/weather")
        Call<Weather> getWeather(@Query("zip") String zip,
                                 @Query("country") String countryCode,
                                 @Query("units") String units,
                                 @Query("appid") String appid);

}
