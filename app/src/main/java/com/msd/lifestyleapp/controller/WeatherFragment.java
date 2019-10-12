package com.msd.lifestyleapp.controller;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msd.lifestyleapp.R;
import com.msd.lifestyleapp.model.User;
import com.msd.lifestyleapp.model.UserRepository;
import com.msd.lifestyleapp.model.UserViewModel;
import com.msd.lifestyleapp.weather.Weather;
import com.msd.lifestyleapp.weather.WeatherViewModel;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    public String username, city, state, zipcode;
    public TextView locationTv, weatherTv, responseTv, conditionsTv, humidityTv, minTempTv, maxTempTv;
    public ImageView weatherIcon;
    public ProgressBar mProgressBar;
    private UserViewModel userViewModel;
    private WeatherViewModel weatherViewModel;
//    private User currentUser;


    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Get a new or existing ViewModel from the ViewModelProvider.
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        if (!MainActivity.isTablet) {
            Toolbar toolbar = view.findViewById(R.id.app_bar);
            toolbar.setTitle("Weather");
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        } else {
            view.findViewById(R.id.app_bar).setVisibility(View.GONE);
        }

        mProgressBar = view.findViewById(R.id.progressBar);
        username = getArguments().getString("username");
        zipcode = getArguments().getString("zipcode");
        city = getArguments().getString("city");
        state = getArguments().getString("state");
        System.out.println(zipcode + city + state);
        locationTv = view.findViewById(R.id.city_field);
        weatherTv = view.findViewById(R.id.temperature);
        weatherIcon = view.findViewById(R.id.weather_icon);
        conditionsTv = view.findViewById(R.id.conditions_field);
        humidityTv = view.findViewById(R.id.humidity_field);
        minTempTv = view.findViewById(R.id.min_temp_field);
        maxTempTv = view.findViewById(R.id.max_temp_field);

        weatherViewModel.getWeather(zipcode).observe(this, new Observer<Weather>() {
            @Override
            public void onChanged(Weather weather) {
                if (weather != null) {
                    locationTv.setText(city + ", " + state);

                    mProgressBar.setVisibility(View.GONE);

                    chooseWeatherIcon(weather.wi.get(0).getIcon(), weatherIcon);

                    //Round the temperature to one decimal place
                    double actualTemp = Math.round(weather.temperature.getTemp() * 10) / 10.0;
                    double actualMin = Math.round(weather.temperature.getTempMin() * 10) / 10.0;
                    double actualMax = Math.round(weather.temperature.getTempMax() * 10) / 10.0;

                    weatherTv.setText(actualTemp + "°");

                    conditionsTv.setText(weather.wi.get(0).getDescription());

                    humidityTv.setText("Humidity: " + weather.temperature.getHumidity() + "%");

                    minTempTv.setText("Low: " + weather.temperature.getTempMin() + "°");

                    maxTempTv.setText("High: " + weather.temperature.getTempMax() + "°");
                }
            }
        });
        return view;
    }

    public void setWeatherInfo() {


    }

//        mProgressBar = new ProgressBar(this.getContext());
//        mProgressBar.setVisibility(View.VISIBLE);

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().onCreateOptionsMenu(menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    public void chooseWeatherIcon(String iconId, ImageView icon_field) {
        switch (iconId) {
            case "01d":
                icon_field.setImageResource(R.drawable.sunny);
                break;

            case "02d":
                icon_field.setImageResource(R.drawable.few_clouds);
                break;

            case "03d":
                icon_field.setImageResource(R.drawable.scattered_clouds);
                break;

            case "04d":
                icon_field.setImageResource(R.drawable.broken_clouds);
                break;

            case "09d":
                icon_field.setImageResource(R.drawable.rain);
                break;

            case "10d":
                icon_field.setImageResource(R.drawable.rain_with_sun);
                break;

            case "11d":
                icon_field.setImageResource(R.drawable.thunder);
                break;

            case "13d":
                icon_field.setImageResource(R.drawable.snow);
                break;

            case "50d":
                icon_field.setImageResource(R.drawable.mist);
                break;

            case "01n":
                icon_field.setImageResource(R.drawable.night);
                break;

            case "02n":
                icon_field.setImageResource(R.drawable.few_clouds_n);
                break;

            case "03n":
                icon_field.setImageResource(R.drawable.scattered_clouds);
                break;

            case "04n":
                icon_field.setImageResource(R.drawable.broken_clouds);
                break;

            case "09n":
                icon_field.setImageResource(R.drawable.rain);
                break;

            case "10n":
                icon_field.setImageResource(R.drawable.night_rain);
                break;

            case "11n":
                icon_field.setImageResource(R.drawable.thunder);
                break;

            case "13n":
                icon_field.setImageResource(R.drawable.snow);
                break;

            case "50n":
                icon_field.setImageResource(R.drawable.mist);
                break;

            default:
                break;
        }
    }
}

