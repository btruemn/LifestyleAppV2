package com.msd.lifestyleapp.controller;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.msd.lifestyleapp.R;
import com.msd.lifestyleapp.model.User;
import com.msd.lifestyleapp.model.UserViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class RegistrationActivity extends AppCompatActivity implements TextView.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView nameTextView, passwordTextView, confirmPasswordTextView;
    private Spinner heightSpinner, weightSpinner, sexSpinner;
//    private SharedPreferencesHandler prefs;
    private String name, password, confirmPassword, dob, height, weight, sex;
    private UserViewModel userViewModel;
    private boolean usersExist;

    int year, month, day;

    private EditText dateSelection;

    private static final int MILLIS_IN_SECOND = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;
    private static final int DAYS_IN_YEAR = 365;
    private static final long MILLISECONDS_IN_YEAR =
            (long) MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR
                    * HOURS_IN_DAY * DAYS_IN_YEAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setTitle("Register");

        //view has too many fields to make sense in landscape
        if (!MainActivity.isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_registration);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_registration_tablet);
        }

        nameTextView = findViewById(R.id.enterName);
        passwordTextView = findViewById(R.id.enterPassword);
        confirmPasswordTextView = findViewById(R.id.enterConfirmPassword);
        heightSpinner = findViewById(R.id.enterHeight);
        weightSpinner = findViewById(R.id.enterWeight);
        sexSpinner = findViewById(R.id.enterSex);

        ArrayList<String> weights = getWeights();
        String[] sexes = new String[]{"", "Male", "Female"};
        String[] heights = getHeights();

        dateSelection = findViewById(R.id.enterAge);
        dateSelection.setOnClickListener(this);

        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, weights);
        weightSpinner.setAdapter(weightAdapter);

        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, sexes);
        sexSpinner.setAdapter(sexAdapter);

        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, heights);
        heightSpinner.setAdapter(heightAdapter);

        findViewById(R.id.submitButton).setOnClickListener(this);

        // Get a new or existing ViewModel from the ViewModelProvider.
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

//        prefs = new SharedPreferencesHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        userViewModel.getAllUserNames().removeObservers(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        userViewModel.getAllUserNames().removeObservers(this);
    }

    public static ArrayList<String> getAges() {
        ArrayList<String> ages = new ArrayList<>();
        ages.add("");
        for (int i = 15; i < 100; i++) {
            ages.add(Integer.toString(i));
        }
        return ages;
    }

    public static ArrayList<String> getWeights() {
        ArrayList<String> weights = new ArrayList<>();
        weights.add("");
        for (int i = 50; i < 400; i++) {
            weights.add(Integer.toString(i));
        }
        return weights;
    }

    public static String[] getHeights() {
        String[] heights = new String[]{"", "4'0\"", "4'1\"", "4'2\"", "4'3\"", "4'4\"", "4'5\"", "4'6\"", "4'7\"", "4'8\"", "4'9\"", "4'10\"", "4'11\"",
                "5'0\"", "5'1\"", "5'2\"", "5'3\"", "5'4\"", "5'5\"", "5'6\"", "5'7\"", "5'8\"", "5'9\"", "5'10\"", "5'11\"",
                "6'0\"", "6'1\"", "6'2\"", "6'3\"", "6'4\"", "6'5\"", "6'6\"", "6'7\"", "6'8\"", "6'9\"", "6'10\"", "6'11\""};
        return heights;
    }

    //Overriding this so the user can't go back to the previous screen if no users exist yet
    @Override
    public void onBackPressed() {

        userViewModel.getAllUserNames().observe(this, new Observer<List<String>>(){
            @Override
            public void onChanged(@Nullable final List<String> names) {
                usersExist = names.size() > 0;
            }
        });
        if (usersExist) {
            super.onBackPressed();
            return;
        }

        return;
    }


    public void onClickReaction(int viewId) {
        switch (viewId) {
            case R.id.submitButton: {
                name = nameTextView.getText().toString().trim();
                password = passwordTextView.getText().toString();
                confirmPassword = confirmPasswordTextView.getText().toString();
                height = heightSpinner.getSelectedItem().toString();
                weight = weightSpinner.getSelectedItem().toString();
                sex = sexSpinner.getSelectedItem().toString();

                if (!checkFieldsFilledOut()) return;
                if (!passwordsMatch()) return;

                userViewModel.getAllUserNames().observe(this, new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> strings) {
                        if (strings.contains(name)) {
                            Toast.makeText(RegistrationActivity.this, "Name is already taken.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            //storing all of the user's values in db
                            User user = new User(name, dob, height, Integer.parseInt(weight), sex, password);
                            userViewModel.insert(user);
                            System.out.println("User " + name + " added!");

                            //here we will start the new intent to the user selection page
                            Intent userSelectionIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(userSelectionIntent);
                        }
                    }
                });


                break;
            }
            case R.id.enterAge: {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dp = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, this, mYear, mMonth, mDay);

                long today = new Date().getTime();
                long maxDate = today - (MILLISECONDS_IN_YEAR * 12); //only let birth date go back 12 years

                dp.getDatePicker().setMaxDate(maxDate);
                dp.setTitle("Select date");
                dp.show();
            }
        }
    }

    public String getStringMonth(int month) {
        String monthStr = "";
        switch (month) {
            case 0: {
                monthStr = "January";
                break;
            }
            case 1: {
                monthStr = "February";
                break;
            }
            case 2: {
                monthStr = "March";
                break;
            }
            case 3: {
                monthStr = "April";
                break;
            }
            case 4: {
                monthStr = "May";
                break;
            }
            case 5: {
                monthStr = "June";
                break;
            }
            case 6: {
                monthStr = "July";
                break;
            }
            case 7: {
                monthStr = "August";
                break;
            }
            case 8: {
                monthStr = "September";
                break;
            }
            case 9: {
                monthStr = "October";
                break;
            }
            case 10: {
                monthStr = "November";
                break;
            }
            case 11: {
                monthStr = "December";
                break;
            }
        }
        return monthStr;
    }

    public boolean checkFieldsFilledOut() {
        //one of the fields was not filled out
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight) || TextUtils.isEmpty(sex) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "Please populate all of the above fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean passwordsMatch() {
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {

        onClickReaction(view.getId());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.day = dayOfMonth;
        this.month = month;
        this.year = year;
        dateSelection.setText(getStringMonth(this.month) + " " + this.day + ", " + this.year);

        dob = this.year + "/" + this.month + "/" + this.day;

    }
}
