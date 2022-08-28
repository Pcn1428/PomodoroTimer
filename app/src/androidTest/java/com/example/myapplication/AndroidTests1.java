package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.os.Bundle;

import java.util.concurrent.TimeUnit;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class AndroidTests1 {
    private MainActivity tMainActivity;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        tMainActivity = activityRule.getActivity();
    }

    // Checks if all elements are displayed correctly on the first page
    @Test
    public void testLocationOnCreate1() {
        onView(withId(R.id.text_title_production_time)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.text_title_short_break)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.text_title_long_break)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.text_title_cycle)).check(matches(isCompletelyDisplayed()));

        onView(withId(R.id.text_production_time)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.text_short_break)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.text_long_break)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.text_cycles)).check(matches(isCompletelyDisplayed()));

        onView(withId(R.id.button_to_timer)).check(matches(isCompletelyDisplayed()));

        // Checking that the default values are displayed correctly
        onView(withId(R.id.text_production_time)).check(matches(withText("25")));
        onView(withId(R.id.text_short_break)).check(matches(withText("5")));
        onView(withId(R.id.text_long_break)).check(matches(withText("30")));
        onView(withId(R.id.text_cycles)).check(matches(withText("2")));
    }



    // Checks if the user inputs are set to the correct string
    @Test
    public void testGetTimerInfo() {
        // test
        tMainActivity.getInputInformation();

        // assertions
        assertEquals("25", tMainActivity.getProductionTimeInput());
        assertEquals("5", tMainActivity.getShortBreakInput());
        assertEquals("30", tMainActivity.getLongBreakInput());
        assertEquals("2", tMainActivity.getCyclesInput());
    }

    // Checks if the bundle generated as the correct information
    @Test
    public void testGenerateBundle() {
        // test
        Bundle testBundle = new Bundle();
        tMainActivity.getInputInformation();
        tMainActivity.generateBundle(testBundle);

        // assertions
        assertEquals("25", testBundle.get("productionTime"));
        assertEquals("5", testBundle.get("shortBreak"));
        assertEquals("30", testBundle.get("longBreak"));
        assertEquals("2", testBundle.get("cycles"));
    }

    // Checks if the correct alert dialog displays for when any of the fields are empty
    @Test
    public void testAlertDialog1() {
        // Test
        onView(withId(R.id.text_production_time)).perform(clearText());
        onView(withId(R.id.button_to_timer)).perform(click());

        // Assertions
        onView(withText("Empty Fields")).check(matches(isDisplayed()));
        onView(withText("Please enter all values")).check(matches(isDisplayed()));
    }

    // Checks if the correct alert dialog is displayed for when there are minutes entered less than 1 but more than 99
    @Test
    public void testAlertDialog2() {
        // Test
        onView(withId(R.id.text_production_time)).perform(clearText());
        onView(withId(R.id.text_production_time)).perform(typeText("100"));
        onView(withId(R.id.button_to_timer)).perform(click());

        // Assertions
        onView(withText("Minutes")).check(matches(isDisplayed()));
        onView(withText("Please enter between 1 and 99 minutes")).check(matches(isDisplayed()));
    }

    // Checks if the correct dialog is displayed for when the number of cycles are less than 1 and greater than 10
    @Test
    public void testAlertDialog3() {
        onView(withId(R.id.text_cycles)).perform(clearText());
        onView(withId(R.id.text_cycles)).perform(typeText("11"));
        onView(withId(R.id.button_to_timer)).perform(click());

        // Assertions
        onView(withText("Cycles")).check(matches(isDisplayed()));
        onView(withText("Please enter between 1 and 10 cycles")).check(matches(isDisplayed()));
    }

    // Sleep
    private void delay(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
