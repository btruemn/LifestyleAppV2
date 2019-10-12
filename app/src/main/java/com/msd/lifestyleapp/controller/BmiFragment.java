package com.msd.lifestyleapp.controller;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msd.lifestyleapp.R;
import com.msd.lifestyleapp.bmr.HealthUtility;
import com.msd.lifestyleapp.model.User;
import com.msd.lifestyleapp.model.UserViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 */
public class BmiFragment extends Fragment {

    private TextView bmiTv, bmrTv, weightTv;
    private String username, dob, height, sex;
    private int weight;
    private double bmi, bmr;
    private Menu _menu;
    private HealthUtility healthUtility;
    private UserViewModel userViewModel;

    public BmiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        // Get a new or existing ViewModel from the ViewModelProvider.
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        //setting the toolbar
        if(!MainActivity.isTablet) {
            Toolbar toolbar = view.findViewById(R.id.app_bar);
            toolbar.setTitle("BMI");
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }else{
            view.findViewById(R.id.app_bar).setVisibility(View.GONE);
        }

        bmiTv = view.findViewById(R.id.bmi_display);
        bmrTv = view.findViewById(R.id.bmr_display);
        weightTv = view.findViewById(R.id.weight_display);

        username = getArguments().getString("username");

        userViewModel.getCurrentUser(username).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                dob = user.getDob();
                username = user.getName();
                weight = user.getWeight();
                height = user.getHeight();
                sex = user.getSex();

                healthUtility = new HealthUtility(weight, height, sex, dob);
                bmi = healthUtility.getBmi();
                bmr = healthUtility.getBmr();

                bmiTv.setText(Double.toString(bmi));
                bmrTv.setText(Double.toString(bmr));
                weightTv.setText(Integer.toString(weight));
            }

        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userViewModel.getCurrentUser(username).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                dob = user.getDob();
                username = user.getName();
                weight = user.getWeight();
                height = user.getHeight();
                sex = user.getSex();

                healthUtility = new HealthUtility(weight, height, sex, dob);
                bmi = healthUtility.getBmi();
                bmr = healthUtility.getBmr();

                bmiTv.setText(Double.toString(bmi));
                bmrTv.setText(Double.toString(bmr));
                weightTv.setText(Integer.toString(weight));

                if (_menu != null) {
                    getActivity().onCreateOptionsMenu(_menu);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().onCreateOptionsMenu(menu);
        _menu = menu;
    }

    private void setUserInfo() {
        userViewModel.getCurrentUser(username).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                dob = user.getDob();
                username = user.getName();
                weight = user.getWeight();
                height = user.getHeight();
                sex = user.getSex();
            }
        });

    }
}
