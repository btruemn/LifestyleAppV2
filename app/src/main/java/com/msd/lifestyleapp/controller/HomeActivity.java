package com.msd.lifestyleapp.controller;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.msd.lifestyleapp.R;
import com.msd.lifestyleapp.model.User;
import com.msd.lifestyleapp.model.UserViewModel;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class HomeActivity extends AppCompatActivity {

    private static Menu _menu;
    private String username;
    private String path, subPath;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private User currentUser;
    private UserViewModel userViewModel;


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String res = intent.getStringExtra("image_update");
            System.out.println("INTENT RECEIVED");
            if (res != null) {
                onCreateOptionsMenu(_menu);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_home_tablet);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_home);
        }

        Intent receivedIntent = getIntent();
        username = receivedIntent.getStringExtra("username");
        System.out.println(username + " reached the home page!");

        Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("UPDATE"));

        // Get a new or existing ViewModel from the ViewModelProvider.
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getCurrentUser(username).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;
            }
        });
    }

    public void getBmiModule(View view) {
        if (MainActivity.isTablet) {
            tabletBmi();
        } else {
            phoneBmi();
        }
    }

    public void getGoalsModule(View view) {
        if (MainActivity.isTablet) {
            System.out.println("tablet goals requested");
            tabletGoals();
        } else {
            phoneGoals();
        }
    }

    public void phoneGoals() {
        //hide the icons
        ConstraintLayout layout = findViewById(R.id.icon_container);
        layout.setVisibility(View.GONE);

        //starting transaction
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        GoalsFragment goalsFragment = new GoalsFragment();
        goalsFragment.setArguments(bundle);
        findViewById(R.id.current_container).setVisibility(View.VISIBLE);
        fragmentTransaction.replace(R.id.current_container, goalsFragment, "goals_frag");

        fragmentTransaction.commit();
    }

    public void tabletGoals() {
        //starting transaction
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        GoalsFragment goalsFrag = new GoalsFragment();
        goalsFrag.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameLayout, goalsFrag, "goals_frag");
        fragmentTransaction.commit();
    }

    public void phoneBmi() {
        //hide the icons
        ConstraintLayout layout = findViewById(R.id.icon_container);
        layout.setVisibility(View.GONE);

        //starting transactions
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        BmiFragment bmiFrag = new BmiFragment();
        bmiFrag.setArguments(bundle);
        findViewById(R.id.current_container).setVisibility(View.VISIBLE);
        fragmentTransaction.replace(R.id.current_container, bmiFrag, "bmi_frag");

        fragmentTransaction.commit();
    }

    public void tabletBmi() {
        //starting transaction
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        BmiFragment bmiFrag = new BmiFragment();
        bmiFrag.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameLayout, bmiFrag, "bmi_frag");
        fragmentTransaction.commit();
    }


    public void getWeatherModule(View view) {
        if (MainActivity.isTablet) {
            tabletWeather();
        } else {
            phoneWeather();
        }
    }

    public void phoneWeather() {
        //hide the icons
        ConstraintLayout layout = findViewById(R.id.icon_container);
        layout.setVisibility(View.GONE);

        //starting transaction
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("zipcode", currentUser.getPostalCode());
        bundle.putString("city", currentUser.getCity());
        bundle.putString("state", currentUser.getState());

        WeatherFragment weatherFrag = new WeatherFragment();
        weatherFrag.setArguments(bundle);
        findViewById(R.id.current_container).setVisibility(View.VISIBLE);
        fragmentTransaction.replace(R.id.current_container, weatherFrag, "weather_frag");

        fragmentTransaction.commit();
    }

    public void tabletWeather() {
        //starting transaction
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        System.out.println("HOME POSTAL CODE: " + currentUser.getPostalCode());
        bundle.putString("zipcode", currentUser.getPostalCode());
        bundle.putString("city", currentUser.getCity());
        bundle.putString("state", currentUser.getState());

        WeatherFragment weatherFrag = new WeatherFragment();
        weatherFrag.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameLayout, weatherFrag, "weather_frag");
        fragmentTransaction.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        System.out.println("RECEIVED INTENT");
        String image = intent.getStringExtra("image_update");
        if (image != null) {
            onCreateOptionsMenu(_menu);
        }
    }

    @Override
    public void onBackPressed() {

        if(!MainActivity.isTablet) {
            findViewById(R.id.current_container).setVisibility(View.GONE);
            ConstraintLayout layout = findViewById(R.id.icon_container);
            layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        if (_menu != null) {
            setUserPhoto(_menu);
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        _menu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        menu.getItem(0).setTitle(username);

        path = Environment.getExternalStorageDirectory().toString();
        subPath = username.replace(" ", "_");
        subPath += ".png";

        setUserPhoto(menu);

        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.user_photo: {
                Intent userSelectionPageIntent = new Intent(this, UserInformationActivity.class);
                userSelectionPageIntent.putExtra("username", username);
                this.startActivity(userSelectionPageIntent);
                break;
            }

            case R.id.user_name: {
                Intent userSelectionPageIntent = new Intent(this, UserInformationActivity.class);
                userSelectionPageIntent.putExtra("username", username);
                this.startActivity(userSelectionPageIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void getNearbyHikes(View view) {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        double latitude = 0, longitude = 0;
        if (location != null) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);

        } else {
            System.out.println("Location is null");
        }

        String searchString = "geo:" + latitude + "," + longitude + "?q=hikes";

        Uri searchUri = Uri.parse(searchString);

        //Create the implicit intent
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);

        //If there's an activity associated with this intent, launch it
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }

    }

    public void setUserPhoto(Menu menu) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        File file = new File(myDir, "/" + subPath);

        if (file.exists()) {
            System.out.println("directory exists");
            Bitmap imageMap = BitmapFactory.decodeFile(file.toString());

            imageMap = getCircularBitmapWithWhiteBorder(imageMap, 1);
            Drawable drawable = new BitmapDrawable(getResources(), imageMap);
            menu.getItem(1).setIcon(drawable);
        } else {
            System.out.println("directory doesn't exist");
            Drawable defaultPic = getResources().getDrawable(R.drawable.no_picture);
            menu.getItem(1).setIcon(defaultPic);
        }
    }

    public Menu getMenu() {
        return _menu;
    }

    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
                                                          int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = (bitmap.getWidth() + borderWidth);
        final int height = (bitmap.getHeight() + borderWidth);


        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }


}
