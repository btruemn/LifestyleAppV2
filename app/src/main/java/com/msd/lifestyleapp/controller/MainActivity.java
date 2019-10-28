package com.msd.lifestyleapp.controller;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.msd.lifestyleapp.R;
import com.msd.lifestyleapp.model.AWSHandler;
import com.msd.lifestyleapp.model.UserViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity {
    /**
     * This will serve to determine whether or not a user has used the app before by checking
     * the phone's local storage for key/value pairs. If the user has already registered, they
     * will be taken to the main profile page (UserSelectionActivity). If they haven't registered, they
     * will be taken to an activity where they can register (RegistrationActivity)
     *
     * @param savedInstanceState
     */

    public static boolean isTablet;
    private UserViewModel userViewModel;
    private boolean usersExist;
    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String encryptionKey           = "ABCDEFGHIJKLMNOP";
    private static final String characterEncoding       = "UTF-8";
    private static final String cipherTransformation    = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";

    private AWSHandler awsHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a new or existing ViewModel from the ViewModelProvider.
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        AWSMobileClient.getInstance().initialize(this).execute();

        awsHandler = new AWSHandler();

        //upload database
        awsHandler.uploadWithTransferUtility("/data/data/com.msd.lifestyleapp/databases/awss3transfertable.db", this);

    }

    private boolean checkIsTablet() {
        Display display = this.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float widthInches = metrics.widthPixels / metrics.xdpi;
        float heightInches = metrics.heightPixels / metrics.ydpi;
        double diagonalInches = Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
        return diagonalInches >= 7.0;
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (!checkPermission()) {
            System.out.println("File access enabled");
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
                return;
            } else {
                System.out.println("File access enabled");
            }
        }

        if (!locationEnabled()) {
            buildAlertMessageNoGps();
        } else {
            userViewModel.getAllUserNames().observe(this, new Observer<List<String>>(){
                @Override
                public void onChanged(@Nullable final List<String> names) {
                    // Update the cached copy of the words in the adapter.
//                    System.out.println("NAMES: " + names.toString());
                    usersExist = names.size() > 0;

                    isTablet = checkIsTablet();

                    if (isTablet) {
                        System.out.println("This device is a tablet.");
                    } else {
                        System.out.println("This device is a phone.");
                    }

                    if (!usersExist) {
                        Intent userNotRegisteredIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                        startActivity(userNotRegisteredIntent);
                    } else {
                        Intent userSelectionIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(userSelectionIntent);
                    }
                }
            });

        }
    }


    public boolean locationEnabled() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            System.out.println("NEED TO REQUEST PERMISSIONS");
            return false;
        }
        return true;
    }

    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS is required for this app, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    System.exit(0);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private static final int PERMISSION_REQUEST_CODE = 200;

    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission Required");
                alertBuilder.setMessage("This app requires access to photo storage.");
                alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE));
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    System.out.println("authorized");
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
