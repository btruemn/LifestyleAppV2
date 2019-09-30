package com.msd.lifestyleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.msd.lifestyleapp.controller.LoginActivity;
import com.msd.lifestyleapp.model.SharedPreferencesHandler;
import com.msd.lifestyleapp.model.User;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.PickerActions.setDate;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    Intent intent;
    static SharedPreferences.Editor preferencesEditor;
    static SharedPreferencesHandler prefs;

    /*
    To test an activity, you use the ActivityTestRule class provided by the Android Testing Support Library.

    This rule provides functional testing of a single activity. The activity under test will be launched before each test annotated with @Test and before any method annotated with @Before. It will be terminated after the test is completed and all methods annotated with @After are finished. The Activity under Test can be accessed during your test by calling ActivityTestRule#getActivity().
     */
    @Rule
    public ActivityTestRule<LoginActivity> mLoginActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @BeforeClass
    public static void setup() {
        Context targetContext = getInstrumentation().getTargetContext();
        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(targetContext).edit();
    }

    @Test
    public void registerNewUserPhoneTest() {
        //register test users
        onView(withId(R.id.registerNewUserButton)).perform(click());

        onView(withId(R.id.enterName)).perform(typeText("test user"))
                .check(matches(withText(containsString("test user"))));

        onView(withId(R.id.enterPassword)).perform(typeText("password"));
        onView(withId(R.id.enterConfirmPassword)).perform(typeText("password"));
        onView(withId(R.id.enterAge)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(setDate(1990, 2, 15));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.enterAge)).check(matches(withText(containsString("February 15, 1990"))));


        onView(withId(R.id.enterHeight)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(20).perform(click());
        onView(withId(R.id.enterHeight)).check(matches(withSpinnerText(containsString("5'7\""))));

        onView(withId(R.id.enterWeight)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(100).perform(click());
        onView(withId(R.id.enterWeight)).check(matches(withSpinnerText(containsString("149"))));


        onView(withId(R.id.enterSex)).perform(click());
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());
        onView(withId(R.id.enterSex)).check(matches(withSpinnerText(containsString("Male"))));

        onView(withId(R.id.submitButton)).perform(click());

        SharedPreferencesHandler sph = new SharedPreferencesHandler(InstrumentationRegistry.getTargetContext());
        User testUser = sph.getUserByName("test user");
        assertEquals(testUser.getName(), "test user");
        assertEquals(testUser.getDob(),"1990/1/15");
        assertEquals(testUser.getHeight(), "5'7\"");
        assertEquals(testUser.getWeight(), 149);
        assertEquals(testUser.getSex(), "Male");
    }

    @Test
    public void loginUserTest() {
        //TODO: test logging in as test user
//        onView(withId(R.id.))



    }

    public void registerNewUserTabletTest() {
        //TODO: implement
    }

    @AfterClass
    public static void cleanUp() {
        SharedPreferencesHandler sph = new SharedPreferencesHandler(InstrumentationRegistry.getTargetContext());
        sph.deleteUser("test user");
    }

}
