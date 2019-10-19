package com.msd.lifestyleapp.controller;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msd.lifestyleapp.R;
import com.msd.lifestyleapp.model.UserViewModel;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSelectionFragment extends Fragment implements View.OnClickListener {

    //    private SharedPreferencesHandler prefs;
    private Button registerUserButton;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private UserViewModel userViewModel;


    public UserSelectionFragment() {
        // Required empty public constructor
    }

    public interface onUserSelected {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_user_selection, container, false);

//        setContentView(R.layout.activity_userselection);
//        getSupportActionBar().setTitle("Lifestyle");

//        prefs = new SharedPreferencesHandler(view.getContext()); /////WILL THIS WORK?

        // Get a new or existing ViewModel from the ViewModelProvider.
        userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

        registerUserButton = view.findViewById(R.id.registerNewUserButton);
        registerUserButton.setOnClickListener(this);

        //capturing the user selection screen's linear layout
        LinearLayout layout = view.findViewById(R.id.userSelectionLayout);

        //all users stored in shared preferences
//        Set<String> names = prefs.getNames();
        userViewModel.getAllUserNames().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable final List<String> names) {
                System.out.print("ALL USERS CHANGED: ");
                System.out.println(names.toString());
                //loop through all users creating buttons for each of them
                int startId = (registerUserButton.getId()) + 1;
                for (String name : names) {
                    createButton(name, layout, startId, view);
                    startId++;
                }
            }
        });

        return view;
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_user_selection, container, false);
    }

    public void createButton(String name, LinearLayout layout, int id, View view) {
        Button button = new Button(view.getContext());
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        button.getBackground().setColorFilter(getResources().getColor(R.color.colorButtonBlue), PorterDuff.Mode.MULTIPLY);
        button.setText(name);
        button.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        button.setHeight(60);
        button.setWidth(600);
        button.setId(id);
        button.setPadding(20, 0, 20, 40);
        layout.addView(button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        userViewModel.getAllUserNames().removeObservers(getActivity());

        //the user chose to register a new account...direct them to the registration page
        if (view.getId() == R.id.registerNewUserButton) {
            Intent registrationPageIntent = new Intent(view.getContext(), RegistrationActivity.class);
            this.startActivity(registrationPageIntent);
            return;
        }

        String username = "";
        int registerButtonId = (registerUserButton.getId()) + 1;

        for (int i = registerButtonId; i <= registerButtonId + 100; i++) {
            int id = view.getId();
            if (id == i) {
                Button btn = view.findViewById(i);
                username = btn.getText().toString();
                break;
            }
        }

        if (MainActivity.isTablet) {
            tabletUserSelection(username);
        } else {
            phoneUserSelection(username);
        }
    }

    public void tabletUserSelection(String username) {

        userViewModel.setUsername(username);
        //        TextView nameDisplay = view.findViewById(R.id.nameDisplay);

//        AuthenticationFragment authenticationFragment = (AuthenticationFragment) getFragmentManager().findFragmentByTag("login_frag");
//
//        FrameLayout layout = authenticationFragment.getActivity().findViewById(R.id.loginContainer);
//        TextView nameDisplay = authenticationFragment.getActivity().findViewById(R.id.nameDisplay);
//
//        Bundle bundle = new Bundle();
//        bundle.putString("username", username);
//        authenticationFragment.setArguments(bundle);
//        nameDisplay.setText("Enter password for: " + username);
//        layout.setVisibility(View.VISIBLE);
    }

    public void phoneUserSelection(String username) {
        //starting transaction
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        AuthenticationFragment authenticationFragment = new AuthenticationFragment();
        Bundle bundle = new Bundle();
//        System.out.println("PhoneUserSelection for user " + username);
        bundle.putString("username", username);
        authenticationFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.userSelectionContainer, authenticationFragment, "login_frag");
        fragmentTransaction.commit();
    }

}
