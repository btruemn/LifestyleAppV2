package com.msd.lifestyleapp.controller;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.msd.lifestyleapp.R;
import com.msd.lifestyleapp.bmr.HealthUtility;
import com.msd.lifestyleapp.model.SharedPreferencesHandler;
import com.msd.lifestyleapp.model.User;
import com.msd.lifestyleapp.model.UserViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class UserInformationActivity extends AppCompatActivity implements TextView.OnClickListener {

    private String username;
    private int weight;
    private String height, sex, dob, age, city, state;
    private double bmi, bmr;
    private String subPath;
    private HealthUtility healthUtility;
    private UserViewModel userViewModel;
    private User currentUser;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private TextView ageTv, sexTv, bmiTv, bmrTv, nameTv, locationTv;

    private Spinner heightSpinner, weightSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_user_information_tablet);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_user_information);
        }

        Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle("User Information");
        setSupportActionBar(toolbar);

        username = getIntent().getStringExtra("username");

        // Get a new or existing ViewModel from the ViewModelProvider.
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getCurrentUser(username).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                dob = user.getDob();
                username = user.getName();
                weight = user.getWeight();
                height = user.getHeight();
                sex = user.getSex();
                city = user.getCity();
                state = user.getState();

                healthUtility = new HealthUtility(weight, height, sex, dob);

                bmi = healthUtility.getBmi();
                bmr = healthUtility.getBmr();
                age = healthUtility.getAge();
                setUserInfoTextViews();
            }
        });

        findViewById(R.id.edit_button).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);
        findViewById(R.id.change_photo).setOnClickListener(this);
        findViewById(R.id.delete_profile_button).setOnClickListener(this);

        subPath = username.replace(" ", "_");
        subPath += ".png";
        setUserPhoto();

        userViewModel.getCurrentUser(username).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;
            }
        });
    }

    public void setUserPhoto() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        File file = new File(myDir, "/" + subPath);

        ImageView profPic = findViewById(R.id.profile_image);

        if (file.exists()) {
            System.out.println("directory exists");
            Bitmap imageMap = BitmapFactory.decodeFile(file.toString());
            profPic.setImageBitmap(imageMap);

        } else {
            System.out.println("directory doesn't exist");
            Drawable defaultPic = getResources().getDrawable(R.drawable.no_picture);
            profPic.setImageDrawable(defaultPic);
        }
    }

    public void deleteUserPhoto() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        File file = new File(myDir, "/" + subPath);

        if (file.exists()) {
            file.delete();
            System.out.println("user image deleted");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout: {
                Intent userSelectionPageIntent = new Intent(this, LoginActivity.class);
                userSelectionPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(userSelectionPageIntent);
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void setUserInfoTextViews() {

        String[] heights = RegistrationActivity.getHeights();
        ArrayList<String> weights = RegistrationActivity.getWeights();

        //setting location
        locationTv = findViewById(R.id.location_display);
        locationTv.setText("");
        locationTv.setText(city + ", " + state);

        //setting name
        nameTv = findViewById(R.id.name_display);
        nameTv.setText("");
        nameTv.setText(username);

        //setting age
        ageTv = findViewById(R.id.age_display);
        ageTv.setText("");
        ageTv.setText(age);

        //setting height
        heightSpinner = findViewById(R.id.height_display);
        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, heights);
        int heightSpinnerPosition = heightAdapter.getPosition(height);
        heightSpinner.setAdapter(heightAdapter);
        heightSpinner.setSelection(heightSpinnerPosition);
        heightSpinner.setAlpha(0.5f);
        heightSpinner.setEnabled(false);

        //setting weight
        weightSpinner = findViewById(R.id.weight_display);
        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, weights);
        int weightSpinnerPosition = weightAdapter.getPosition(Integer.toString(weight));
        weightSpinner.setAdapter(weightAdapter);
        weightSpinner.setSelection(weightSpinnerPosition);
        weightSpinner.setAlpha(0.5f);
        weightSpinner.setEnabled(false);

        //setting sex
        sexTv = findViewById(R.id.sex_display);
        sexTv.setText("");
        sexTv.setText(sex);

        //setting bmi
        bmiTv = findViewById(R.id.bmi_display);
        bmiTv.setText("");
        bmiTv.setText(Double.toString(bmi));

        //setting bmr
        bmrTv = findViewById(R.id.bmr_display);
        bmrTv.setText("");
        bmrTv.setText(Double.toString(bmr));
    }

//    private void setUserInfo() {
//        userViewModel.getCurrentUser(username).observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//                dob = user.getDob();
//                username = user.getName();
//                weight = user.getWeight();
//                height = user.getHeight();
//                sex = user.getSex();
//                city = user.getCity();
//                state = user.getState();
//            }
//        });
//    }

    public double roundDecimal(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView profPic = findViewById(R.id.profile_image);
            profPic.setImageBitmap(imageBitmap);

            saveImage(imageBitmap);


            Intent intent = new Intent("UPDATE").putExtra("image_update", "update");
            LocalBroadcastManager.getInstance(UserInformationActivity.this).sendBroadcast(intent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button: {
                findViewById(R.id.edit_button).setEnabled(false);
                findViewById(R.id.edit_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.save_button).setVisibility(View.VISIBLE);
                findViewById(R.id.save_button).setEnabled(true);
                findViewById(R.id.weight_display).setEnabled(true);
                findViewById(R.id.weight_display).setAlpha(1.0f);
                findViewById(R.id.height_display).setEnabled(true);
                findViewById(R.id.height_display).setAlpha(1.0f);

                break;
            }
            case R.id.save_button: {
                findViewById(R.id.save_button).setEnabled(false);
                findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.edit_button).setVisibility(View.VISIBLE);
                findViewById(R.id.edit_button).setEnabled(true);
                findViewById(R.id.weight_display).setEnabled(false);
                findViewById(R.id.height_display).setEnabled(false);
                findViewById(R.id.weight_display).setAlpha(0.5f);
                findViewById(R.id.height_display).setAlpha(0.5f);


                Spinner wd = findViewById(R.id.weight_display);
                Spinner hd = findViewById(R.id.height_display);

                int newWeight = Integer.parseInt(wd.getSelectedItem().toString());
                String newHeight = hd.getSelectedItem().toString();
                currentUser.setWeight(newWeight);
                currentUser.setHeight(newHeight);
                userViewModel.update(currentUser);

                userViewModel.getCurrentUser(username).observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        dob = user.getDob();
                        username = user.getName();
                        weight = user.getWeight();
                        height = user.getHeight();
                        sex = user.getSex();
                        city = user.getCity();
                        state = user.getState();

                        healthUtility = new HealthUtility(weight, height, sex, dob);
                        bmi = healthUtility.getBmi();
                        bmr = healthUtility.getBmr();
                        setUserInfoTextViews();
                    }
                });


                break;
            }
            case R.id.delete_profile_button: {
                buildAlertMessageDeleteProfile();
                break;
            }
            case R.id.change_photo: {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            }
        }
    }

    private void saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");

        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File file = new File(myDir, "/" + subPath);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildAlertMessageDeleteProfile() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete your profile?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    deleteUserPhoto();
                    userViewModel.deleteByUserName(username);
                    Intent userSelectionPageIntent = new Intent(this, LoginActivity.class);
                    userSelectionPageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    this.startActivity(userSelectionPageIntent);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
