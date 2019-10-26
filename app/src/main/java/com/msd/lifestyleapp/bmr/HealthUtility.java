package com.msd.lifestyleapp.bmr;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class HealthUtility {

    private double bmr, bmi;
    private int weight;
    private String sex, age, height, dob;

    public HealthUtility(int _weight, String _height, String _sex, String _dob) {
        weight = _weight;
        sex = _sex;
        int[] parsedDob = parseDob(_dob);
        age = setAge(parsedDob[0], parsedDob[1], parsedDob[2]);
        height = _height;
    }


    private double roundDecimal(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // BMI = (weight/height*height) * 703
    public double getBmi() {
        double heightInInches = getHeightInInches();
        double heightSquared = heightInInches * heightInInches;
        double res = weight / heightSquared;
        res *= 703;

        return roundDecimal(res, 2);
    }

    // Female BMR = 655 + (4.35 * weight) + (4.7 * height) - (4.7 * age)
    // Male BMR = 66 + (6.23 * weight) + (12.7 * height) - (6.8 * age)
    public double getBmr() {

        double initial, factor1, factor2, factor3, part1, part2, part3;

        if (sex == "Male") {
            initial = 66;
            factor1 = 6.23;
            factor2 = 12.7;
            factor3 = 6.8;
        } else {
            initial = 655;
            factor1 = 4.35;
            factor2 = 4.7;
            factor3 = 4.7;
        }

        part1 = factor1 * weight;
        part2 = factor2 * getHeightInInches();
        part3 = factor3 * Integer.parseInt(age);

        double res = initial + part1 + part2 - part3;

        return Math.round(res);
    }

    private double getHeightInInches() {

        String ft = "";
        if(TextUtils.isEmpty(height)) System.out.println("HEIGHT IS EMPTY");
        ft += height.charAt(0);

        int feetInInches = Integer.parseInt(ft) * 12;

        String parsedInches = "";

        for (int i = 1; i < height.length(); i++) {
            if (height.charAt(i) != '\'' && height.charAt(i) != '"') {
                parsedInches += height.charAt(i);
            }
        }
        return feetInInches + Integer.parseInt(parsedInches);
    }

    public int[] parseDob(String _dob) {
        String[] parsed = _dob.split("/");
        int y = Integer.parseInt(parsed[0]);
        int m = Integer.parseInt(parsed[1]);
        int d = Integer.parseInt(parsed[2]);
        return new int[]{y, m, d};
    }

    private String setAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = Integer.valueOf(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public String getAge() {
        return age;
    }

}
