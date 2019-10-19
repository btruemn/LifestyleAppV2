package com.msd.lifestyleapp.controller;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.msd.lifestyleapp.R;
import com.msd.lifestyleapp.bmr.HealthUtility;
import com.msd.lifestyleapp.model.User;
import com.msd.lifestyleapp.model.UserViewModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 */
public class GoalsFragment extends Fragment implements View.OnClickListener {

    private Spinner activityLevelSpinner, weightGoalSpinner, poundsSpinner;
    private String[] activityLevelArray, weightGoalArray, poundsArray;
    private int weight;
    private String weightGoal, activityLevel, poundsPerWeek, height, dob, username, sex;
    private View _view;
    private Menu _menu;
    private HealthUtility healthUtility;
    private double bmr;
    private TextView goalDisplay, calorieMessageDisplay, currentWeight;
    private Button getCaloriesButton;
    private UserViewModel userViewModel;
    private User currentUser;


    public GoalsFragment() {
        activityLevelArray = new String[]{"Sedentary", "Lightly-Active", "Active", "Very Active", "Extra-Active"};
        weightGoalArray = new String[]{"Gain", "Lose", "Maintain"};
        poundsArray = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        //setting the toolbar
        if (!MainActivity.isTablet) {
            Toolbar toolbar = view.findViewById(R.id.app_bar);
            toolbar.setTitle("Goals");
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        } else {
            view.findViewById(R.id.app_bar).setVisibility(View.GONE);
        }

        username = getArguments().getString("username");

        // Get a new or existing ViewModel from the ViewModelProvider.
        userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);


        userViewModel.getCurrentUser(username).observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;
                username = user.getName();
                height = user.getHeight();
                dob = user.getDob();
                sex = user.getSex();
                weight = user.getWeight();
                weightGoal = user.getFitnessGoal();
                activityLevel = user.getActivityLevel();
                poundsPerWeek = user.getPoundsPerWeek();

                healthUtility = new HealthUtility(weight, height, sex, dob);

                _view = view;
                setViews(_view);
            }
        });

        goalDisplay = view.findViewById(R.id.calorie_display);
        calorieMessageDisplay = view.findViewById(R.id.calorie_message);
        getCaloriesButton = view.findViewById(R.id.get_calories);
        currentWeight = view.findViewById(R.id.current_weight);
        getCaloriesButton.setOnClickListener(this);

        return view;
    }

    public void setViews(View view) {
        setSpinners(view);

        currentWeight.setText(String.format("%s lbs", Integer.toString(weight)));

        TextView heightView = view.findViewById(R.id.current_height);
        heightView.setText(height);

        TextView bmrView = view.findViewById(R.id.current_bmr);

        bmr = healthUtility.getBmr();

        int bmrInt = (int) Math.round(bmr);

        bmrView.setText(Integer.toString(bmrInt));
    }

    public void setSpinners(View view) {
        activityLevelSpinner = view.findViewById(R.id.activity_spinner);
        weightGoalSpinner = view.findViewById(R.id.weight_spinner);
        poundsSpinner = view.findViewById(R.id.pounds_spinner);

        ArrayAdapter<String> activityAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item2, activityLevelArray);
        activityLevelSpinner.setAdapter(activityAdapter);
        int activitySpinnerPosition = activityLevel == null ? 0 : activityAdapter.getPosition(activityLevel);
        activityLevelSpinner.setSelection(activitySpinnerPosition);

        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item2, weightGoalArray);
        weightGoalSpinner.setAdapter(weightAdapter);
        int weightSpinnerPosition = weightGoal == null ? 0 : weightAdapter.getPosition(weightGoal);
        weightGoalSpinner.setSelection(weightSpinnerPosition);

        ArrayAdapter<String> poundsAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item2, poundsArray);
        poundsSpinner.setAdapter(poundsAdapter);
//        System.out.println("LBS/WK: " + poundsPerWeek);
        int poundsSpinnerPosition = poundsPerWeek == null ? 0 : activityAdapter.getPosition(poundsPerWeek);
        poundsSpinner.setSelection(poundsSpinnerPosition);

        weightGoalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                LinearLayout layout = view.findViewById(R.id.pounds_view);

                if (position == 2) {
                    layout.setVisibility(View.GONE);
                }

                if (position == 0 || position == 1) {
                    layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().onCreateOptionsMenu(menu);
        _menu = menu;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        userViewModel.getCurrentUser(username).observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                username = user.getName();
                height = user.getHeight();
                dob = user.getDob();
                sex = user.getSex();
                weight = user.getWeight();
                weightGoal = user.getFitnessGoal();
                activityLevel = user.getActivityLevel();
                poundsPerWeek = user.getPoundsPerWeek();

                healthUtility = new HealthUtility(weight, height, sex, dob);

                if(_view != null) setSpinners(_view);

                if (_menu != null) {
                    getActivity().onCreateOptionsMenu(_menu);
                }

                if (goalDisplay.getVisibility() != View.INVISIBLE) {
                    getCaloriesButton.performClick();
                }
            }
        });


    }


//    private void setUserInfo() {
//        userViewModel.getCurrentUser(username).observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//                username = user.getName();
//                height = user.getHeight();
//                dob = user.getDob();
//                sex = user.getSex();
//                weight = user.getWeight();
//                weightGoal = user.getFitnessGoal();
//                activityLevel = user.getActivityLevel();
//                poundsPerWeek = user.getPoundsPerWeek();
//            }
//        });
//    }

    private double roundDecimal(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getCalorieGoal() {

        double maintenance = getBaselineCals();

        String goal = weightGoalSpinner.getSelectedItem().toString();

        if (goal == "Gain") {
            double weightDiff = Double.parseDouble(poundsSpinner.getSelectedItem().toString());
            maintenance += (500 * weightDiff);
        }

        if (goal == "Lose") {
            double weightDiff = Double.parseDouble(poundsSpinner.getSelectedItem().toString());
            maintenance -= (500 * weightDiff);
        }

        maintenance = roundDecimal(maintenance, 2);

        return maintenance;
    }

    public double getBaselineCals() {
        String fitnessLevel = activityLevelSpinner.getSelectedItem().toString();

        double maintenance = 0;

        switch (fitnessLevel) {

            case "Sedentary": {
                maintenance = bmr * 1.2;
                break;
            }

            case "Lightly-Active": {
                maintenance = bmr * 1.375;
                break;
            }

            case "Active": {
                maintenance = bmr * 1.55;
                break;
            }

            case "Very Active": {
                maintenance = bmr * 1.725;
                break;
            }

            case "Extra-Active": {
                maintenance = bmr * 1.9;
                break;
            }
        }

        return maintenance;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.get_calories) {

            if (poundsSpinner.getSelectedItem() == null) {
                return;
            }

            double caloriesRequired = getCalorieGoal();

            if (sex.equals("Male")) {
//                System.out.println("sex: " + sex);
                if (caloriesRequired < 1200.0) {
                    weightGoalAlert();
                }
            }

            if (sex.equals("Female")) {
                if (caloriesRequired < 1000.0) {
                    weightGoalAlert();
                }
            }


            currentUser.setFitnessGoal(weightGoalSpinner.getSelectedItem().toString());
            currentUser.setActivityLevel(activityLevelSpinner.getSelectedItem().toString());
//            System.out.println("LBS/WEEK: " + poundsSpinner.getSelectedItem().toString());
            currentUser.setPoundsPerWeek(poundsSpinner.getSelectedItem().toString());
            userViewModel.update(currentUser);

            setCalorieViews(caloriesRequired);

        }
    }

    public void setCalorieViews(Double caloriesRequired) {
        goalDisplay.setVisibility(View.VISIBLE);

        int caloriesInt = (int) Math.round(caloriesRequired);

        goalDisplay.setText(Integer.toString(caloriesInt));

        String message = "*You need to consume " + caloriesInt + " calories per day to ";

        String goal = weightGoalSpinner.getSelectedItem().toString();

        if (goal == "Gain") {
            String poundsGoalStr = poundsSpinner.getSelectedItem().toString();
            String gainSub = "to gain " + poundsGoalStr + " lb(s)/week.";
            message += gainSub;

        }
        if (goal == "Lose") {
            String poundsGoalStr = poundsSpinner.getSelectedItem().toString();
            String loseSub = "to lose " + poundsGoalStr + " lb(s)/week.";
            message += loseSub;

        }
        if (goal == "Maintain") {
            String maintainSub = "maintain your weight";
            message += maintainSub;
        }

        calorieMessageDisplay.setText(message);
    }


    public void weightGoalAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Calorie Warning");
        if (sex.equals("Male")) {
            alertBuilder.setMessage("It is recommended that men consume at least 1200 calories/day.");
        }

        if (sex.equals("Female")) {
            alertBuilder.setMessage("It is recommended that women consume at least 1000 calories/day.");
        }
        alertBuilder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
}
