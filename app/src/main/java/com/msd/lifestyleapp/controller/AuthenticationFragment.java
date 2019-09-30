package com.msd.lifestyleapp.controller;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.msd.lifestyleapp.R;
import com.msd.lifestyleapp.model.SharedPreferencesHandler;
import com.msd.lifestyleapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthenticationFragment extends Fragment implements View.OnClickListener, LocationListener {

    private boolean isTablet = MainActivity.isTablet;
    private SharedPreferencesHandler prefs;
    private TextView passwordTextView;
    private User currentUser;
    private String username;
    private TextView nameDisplay;

    //location
    private LocationManager locationManager;
    private String provider;
    double latitude, longitude;

    public AuthenticationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;

        if (MainActivity.isTablet) {
            view = inflater.inflate(R.layout.fragment_authentication_tablet, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_authentication, container, false);
        }

        if (!MainActivity.isTablet) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Login");
        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("NEED TO REQUEST PERMISSIONS");
            return view;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);

        } else {
            latitude = 40.768591;
            longitude = -111.846070;
            System.out.println("Location is null");
        }

//        getActivity().findViewById(R.id.submitPassword).setOnClickListener(this);

        view.findViewById(R.id.submitPassword).setOnClickListener(this);

        if (isTablet) {
            System.out.println("Using tablet");
        } else {
            System.out.println("Using phone");
            Bundle bundle = this.getArguments();
            username = bundle.getString("username", "failure");
            System.out.println("USERNAME: " + username);
            prefs = new SharedPreferencesHandler(view.getContext());
            currentUser = prefs.getUserByName(username);

            nameDisplay = view.findViewById(R.id.nameDisplay);
            nameDisplay.setText("Enter password for: " + currentUser.getName());
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitPassword: {

                if (isTablet) {
                    nameDisplay = getActivity().findViewById(R.id.nameDisplay);
                    username = nameDisplay.getText().toString().split(":")[1].trim();
                    prefs = new SharedPreferencesHandler(view.getContext());
                    System.out.println(username);
                    currentUser = prefs.getUserByName(username);
                }
//                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                passwordTextView = getActivity().findViewById(R.id.passwordInput);
                //                String password = encoder.encode(passwordTextView.getText().toString());
                String password = passwordTextView.getText().toString();

                if (!password.equals(currentUser.getPassword())) {
                    Toast.makeText(this.getActivity(), "Invalid password.", Toast.LENGTH_SHORT).show();
                } else {
                    //update user's location
                    String[] cityAndState = getCityStateAndPostalFromLatLong(latitude, longitude);


                    if (cityAndState.length != 0) {
                        //city
                        String city = cityAndState[0];
                        //state
                        String state = cityAndState[1];
                        //postal
                        String postalCode = cityAndState[2];

                        prefs.updateLocation(city, state, postalCode, currentUser);

                        //correct password...go to home page
                        Intent homePageIntent = new Intent(this.getActivity(), HomeActivity.class);

                        homePageIntent.putExtra("username", currentUser.getName());

                        this.startActivity(homePageIntent);
                    } else {

                    }

                }
            }
        }
    }

    public String[] getCityStateAndPostalFromLatLong(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this.getActivity(), Locale.getDefault());
        String city = "", state = "", postalCode = "";

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (Exception e) {
            System.out.println("Geocoder: Unable to retreive address from latitude/longitude");
            return new String[]{};
        }

        if (addresses.size() != 0) {
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            postalCode = addresses.get(0).getPostalCode();
            return new String[]{city, state, postalCode};
        } else {
            Toast.makeText(this.getActivity(), "Unable to retrieve your location.",
                    Toast.LENGTH_LONG).show();
            return new String[]{};
        }
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("NEED TO REQUEST PERMISSIONS");
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        //TODO: update location
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this.getActivity(), "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this.getActivity(), "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }


}
