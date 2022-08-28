package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AndroidTests2 {
    private MainActivity2 tMainActivity2;

    @Rule
    public ActivityTestRule<MainActivity2> activityRule = new ActivityTestRule<>(MainActivity2.class);

    @Before
    public void setup() {
        tMainActivity2 = activityRule.getActivity();
        setTestParameters();
    }

    // Checks if all elements are displayed correctly on the second page
    @Test
    public void testLocationOnCreate2() {
        onView(withId(R.id.text_view_countdown)).check(matches(isCompletelyDisplayed()));

        onView(withId(R.id.button_start_pause)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.button_start_pause)).check(matches(withText("Start")));
    }

    /* Start the timer. Waits 5s.
       Checks if the start time is different
       Checks if the start/pause button is set to "Pause"
       Checks if the timer running flag is set to true
    */
    @Test
    public void testStartTimer() {
        // test
        delay(5);
        long startTime = tMainActivity2.getmTimeLeftInMillis();
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(5);
        long currentTime = tMainActivity2.getmTimeLeftInMillis();

        // assertions
        assertTrue(currentTime < startTime);
        assertTrue(tMainActivity2.ismTimerRunning());
        onView(withId(R.id.button_start_pause)).check(matches(withText("Pause")));
    }

    /* Start and then pause immediately.
       Checks if the time remaining matches
       Checks if the start/pause button is set to "Start"
       Checks if the timer running flag is set to false
    */
    @Test
    public void testPauseTimer() {
        // test
        long startTime = tMainActivity2.getmTimeLeftInMillis();
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(3);
        onView(withId(R.id.button_start_pause)).perform(click());
        long pausedTime = tMainActivity2.getmTimeLeftInMillis();

        // assertions
        onView(withId(R.id.button_start_pause)).check(matches(withText("Start")));
        assertTrue(pausedTime < startTime);
    }

    /* Starts and pauses immediately. Reset is chosen.
       Checks if the time matches the initial time.
       Checks if the start/pause button is set to "Start"
       Checks if the timer running flag is set to false
     */
    @Test
    public void testResetTimer() {
        setTestParameters();
        // test
        long startTime = tMainActivity2.getmTimeLeftInMillis();
        onView(withId(R.id.button_start_pause)).perform(click());
        onView(withId(R.id.button_start_pause)).perform(click());
        onView(withId(R.id.button_reset)).perform(click());
        long resetTime = tMainActivity2.getmTimeLeftInMillis();

        // assertions
        assertTrue(startTime == resetTime);
        onView(withId(R.id.button_start_pause)).check(matches(withText("Start")));
    }

    /* Goes through each timer type until finished. Full reset is chosen
       Checks that the start time is the same before and after the reset
       Checks that all of the timer data is also reset
     */
    @Test
    public void testFullReset() {
        // test production --> short break --> long break --> finished --> reset
        long startTime = tMainActivity2.getmTimeLeftInMillis();
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(30);
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(15);
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(15);
        onView(withId(R.id.button_full_reset)).perform(click());
        long resetTime = tMainActivity2.getmTimeLeftInMillis();

        // assertions
        assertTrue(startTime == resetTime);
        assertEquals(30000, tMainActivity2.getmProductionTime());
        assertEquals(15000, tMainActivity2.getmShortBreakTime());
        assertEquals(15000, tMainActivity2.getmLongBreakTime());
        assertEquals(30000, tMainActivity2.getmResetTime());
        assertEquals(1, tMainActivity2.getStartCycles());
        assertEquals(1, tMainActivity2.getRemainingCycles());
        assertEquals(30000, tMainActivity2.getmTimeLeftInMillis());
        onView(withId(R.id.button_start_pause)).check(matches(withText("Start")));
    }

    /*Does*/
    @Test
    public void testInitTimerInfo() {
        assertTrue(tMainActivity2.isProductionRunning());
        assertFalse(tMainActivity2.isShortBreakRunning());
        assertFalse(tMainActivity2.isLongBreakRunning());
        assertFalse(tMainActivity2.ismTimerRunning());

        assertEquals(tMainActivity2.getmTimeLeftInMillis(), tMainActivity2.getmProductionTime());
        assertEquals(tMainActivity2.getmProductionTime(), tMainActivity2.getmResetTime());
        assertEquals(tMainActivity2.getStartCycles(), tMainActivity2.getRemainingCycles());
    }

    // Case 1: Production --> Short Break
    @Test
    public void testChangeTimerType1() {
        // test
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(31);

        // assertions
        assertFalse(tMainActivity2.isProductionRunning());
        assertTrue(tMainActivity2.isShortBreakRunning());
        onView(withId(R.id.text_view_timer_type)).check(matches(withText("Short Break")));
        assertEquals(tMainActivity2.getmShortBreakTime(), tMainActivity2.getmTimeLeftInMillis());
        assertEquals(tMainActivity2.getmShortBreakTime(), tMainActivity2.getmResetTime());
        assertTrue(tMainActivity2.getStartCycles() > tMainActivity2.getRemainingCycles());
    }

    // Case 2: Short Break to Production
    @Test
    public void testChangeTimerType2() {
        // test
        tMainActivity2.setRemainingCycles(2);
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(30);
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(15);

        // assertions
        assertFalse(tMainActivity2.isShortBreakRunning());
        assertTrue(tMainActivity2.isProductionRunning());
        onView(withId(R.id.text_view_timer_type)).check(matches(withText("Be Productive")));
        assertEquals(tMainActivity2.getmProductionTime(), tMainActivity2.getmTimeLeftInMillis());
        assertEquals(tMainActivity2.getmProductionTime(), tMainActivity2.getmResetTime());
    }

    // Case 3: Short Break to Long Break
    @Test
    public void testChangeTimerType3() {
        // test
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(30);
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(15);

        // assertions
        assertFalse(tMainActivity2.isShortBreakRunning());
        assertTrue(tMainActivity2.isLongBreakRunning());
        onView(withId(R.id.text_view_timer_type)).check(matches(withText("Long Break")));
        assertEquals(tMainActivity2.getmLongBreakTime(), tMainActivity2.getmTimeLeftInMillis());
        assertEquals(tMainActivity2.getmLongBreakTime(), tMainActivity2.getmResetTime());
    }

    // Case 4: Long Break to Finished
    @Test
    public void TestChangeTimerType4() {
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(30);
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(15);
        onView(withId(R.id.button_start_pause)).perform(click());
        delay(17);

        // assertions
        assertFalse(tMainActivity2.isProductionRunning());
        assertFalse(tMainActivity2.isShortBreakRunning());
        assertFalse(tMainActivity2.isLongBreakRunning());
        assertFalse(tMainActivity2.ismTimerRunning());

        onView(withId(R.id.text_view_timer_type)).check(matches(withText("Good Job!")));
        onView(withId(R.id.button_full_reset)).check(matches(isCompletelyDisplayed()));

    }

    // Sleep
    private void delay(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setTestParameters() {
        tMainActivity2.setmProductionTime(30000);
        tMainActivity2.setmShortBreakTime(15000);
        tMainActivity2.setmLongBreakTime(15000);
        tMainActivity2.setmResetTime(30000);
        tMainActivity2.setStartCycles(1);
        tMainActivity2.setRemainingCycles(1);
        tMainActivity2.setmTimeLeftInMillis(30000);
    }
}