package com.msd.lifestyleapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * users will be stored in shared preferences as follows:
 * -a set of names is stored called "names"...prefs.getString("names") will give all names
 * -users are stored as key/value pairs with their names...key is the name and value is a json
 *      string of the user object
 */
public class SharedPreferencesHandler {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Gson gson;


    public SharedPreferencesHandler(Context context){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        gson = new Gson();
    }

    public boolean addTime(){
        long oneHour = 3600000;
        long now = System.currentTimeMillis();

        String time = prefs.getString("time", "");

        Long t = now + oneHour;
        String entry = t.toString();

        if(time.isEmpty()){
            editor.putString("time", entry);
            editor.commit();
            return false;
        }

        Long prefTime = Long.parseLong(time);

        if(now >= prefTime){
            editor.putString("time", entry);
            editor.commit();
            return true;
        }

        return false;
    }

    public void addUser(User user){

        if(!nameAlreadyExists(user.getName())) {
            Set<String> names = prefs.getStringSet("names", Collections.emptySet());
            if(names.isEmpty()) names = new HashSet();
            names.add(user.getName());
            editor.putStringSet("names", names);
            editor.commit();

            User newUser = new User(user.getName(), user.getDob(), user.getHeight(),
                    user.getWeight(), user.getSex(), user.getPassword());

            String json = gson.toJson(newUser);
            editor.putString(user.getName(), json);
            editor.commit();
        }
    }


    public void updateUser(String name, int weight, String height){
        String jsonUser = prefs.getString(name, "");
        User user = gson.fromJson(jsonUser, User.class);
        user.setWeight(weight);
        user.setHeight(height);

        String json = gson.toJson(user);
        editor.putString(name, json);
        editor.commit();
    }

    public void updateImagePath(String name, String imagePath){
        String jsonUser = prefs.getString(name, "");
        User user = gson.fromJson(jsonUser, User.class);
        user.setPhotoPath(imagePath);

        String json = gson.toJson(user);
        editor.putString(name, json);
        editor.commit();
    }

    public boolean nameAlreadyExists(String name){

        Set<String> names = prefs.getStringSet("names", Collections.emptySet());

        if(names.contains(name)) return true;
        return false;
    }

    public boolean usersExist(){
        Set<String> names = prefs.getStringSet("names", Collections.emptySet());
        if(names.isEmpty()) return false;
        return true;
    }

    public Set<String> getNames(){
        return prefs.getStringSet("names", Collections.emptySet());
    }

    public User getUserByName(String name){
        String jsonUser = prefs.getString(name, "");
        User user = gson.fromJson(jsonUser, User.class);
        return user;
    }

    public void updateUserFitnessGoals(User user, String _fitnessGoal, String _activityLevel, String _poundsPerWeek){
        User newUser = new User(user.getName(), user.getDob(), user.getHeight(),
                user.getWeight(), user.getSex(), user.getPassword(), user.getCity(), user.getState(), user.getPostalCode(), _fitnessGoal,
                _activityLevel, _poundsPerWeek);

        String json = gson.toJson(newUser);
        editor.putString(user.getName(), json);
        editor.commit();
    }

    public void deleteUser(String name){
        Set<String> names = prefs.getStringSet("names", Collections.emptySet());
        names.remove(name);
        editor.putStringSet("names", names);
        editor.remove(name);
        editor.commit();
    }

    public void updateLocation(String city, String state, String postalCode, User user){

        User newUser = new User(user.getName(), user.getDob(), user.getHeight(),
                user.getWeight(), user.getSex(), user.getPassword(), city, state, postalCode, user.getFitnessGoal(),
                user.getActivityLevel(), user.getPoundsPerWeek());

        String json = gson.toJson(newUser);
        editor.putString(user.getName(), json);
        editor.commit();
    }

}
