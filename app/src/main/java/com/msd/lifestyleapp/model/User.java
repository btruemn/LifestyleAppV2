package com.msd.lifestyleapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    //not null values
    @PrimaryKey
    @NonNull
    private String name;
    private String password;
    private String dob;
    private String height;
    private int weight;
    private String sex;

    //optional values
    private String fitnessGoal;
    private String activityLevel;
    private String poundsPerWeek;
    private String photoPath;
    private String city;
    private String state;
    private String postalCode;


    public User(@NonNull String _name, String _dob, String _height, int _weight, String _sex, String _password) {
        name = _name;
        dob = _dob;
        height = _height;
        weight = _weight;
        sex = _sex;
        password = _password;
    }

    public User(@NonNull String _name, String _dob, String _height, int _weight, String _sex, String _password, String _city,
                String _state, String _postalCode, String _fitnessGoal, String _activityLevel, String _poundsPerWeek) {
        name = _name;
        dob = _dob;
        height = _height;
        weight = _weight;
        sex = _sex;
        password = _password;
        city = _city;
        state = _state;
        postalCode = _postalCode;
        fitnessGoal = _fitnessGoal;
        activityLevel = _activityLevel;
        poundsPerWeek = _poundsPerWeek;
    }

    public User(@NonNull String name, String password, String dob, String height, int weight, String sex, String fitnessGoal, String activityLevel, String poundsPerWeek, String photoPath, String city, String state, String postalCode) {
        this.name = name;
        this.password = password;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.fitnessGoal = fitnessGoal;
        this.activityLevel = activityLevel;
        this.poundsPerWeek = poundsPerWeek;
        this.photoPath = photoPath;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getPoundsPerWeek() {
        return poundsPerWeek;
    }

    public void setPoundsPerWeek(String poundsPerWeek) {
        this.poundsPerWeek = poundsPerWeek;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", dob='" + dob + '\'' +
                ", height='" + height + '\'' +
                ", weight=" + weight +
                ", sex='" + sex + '\'' +
                ", fitnessGoal='" + fitnessGoal + '\'' +
                ", activityLevel='" + activityLevel + '\'' +
                ", poundsPerWeek='" + poundsPerWeek + '\'' +
                ", photoPath='" + photoPath + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}