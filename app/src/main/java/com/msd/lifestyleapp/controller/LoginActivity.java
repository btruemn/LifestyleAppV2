package com.msd.lifestyleapp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.msd.lifestyleapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//TODO: fix app so it doesn't crash after enabling location

public class LoginActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
//    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login/Register");

        //starting transaction
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (MainActivity.isTablet) {
            FrameLayout layout = findViewById(R.id.loginContainer);
            layout.setVisibility(View.GONE);
            //this is a tablet...render UserSelectionFragment and AuthenticationFragment
            UserSelectionFragment selectionFragment = new UserSelectionFragment();
            AuthenticationFragment authenticationFragment = new AuthenticationFragment();

            fragmentTransaction.replace(R.id.userSelectionContainer, selectionFragment, "selection_frag");
            fragmentTransaction.replace(R.id.loginContainer, authenticationFragment, "login_frag");

            fragmentTransaction.commit();

        } else {
            //hide the login fragment
            FrameLayout layout = findViewById(R.id.loginContainer);
            layout.setVisibility(View.GONE);

            //just render UserSelectionFragment
            UserSelectionFragment selectionFragment = new UserSelectionFragment();
            fragmentTransaction.replace(R.id.userSelectionContainer, selectionFragment, "selection_frag");
            fragmentTransaction.commit();
        }

//        // Get a new or existing ViewModel from the ViewModelProvider.
//        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

    }

    @Override
    public void onBackPressed() {

        Fragment f = getSupportFragmentManager().findFragmentByTag("selection_frag");
        if (f == null && !MainActivity.isTablet) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            this.startActivity(loginIntent);
        }
    }
}
