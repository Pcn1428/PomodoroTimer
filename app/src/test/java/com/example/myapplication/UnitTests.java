package com.example.myapplication;

import org.junit.Test;



import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 * Note: all the UI testing
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {

    private MainActivity tMainActivity;
    private MainActivity2 tMainActivity2;

    @Test
    public void millisToString_isCorrect() {
        long testLong = 60000;
        String result = HelperMethods.MillisToString(testLong);
        String expected = "01:00";
        assertEquals(result,expected);
    }

    @Test
    public void stringToMillis_isCorrect() {
        String test = "30";
        long result = HelperMethods.StringToMillis(test);
        long expected = 1800000;
        assertEquals(result,expected);
    }

    @Test
    public void countdownToMillis_isCorrect() {
        String test = "05:23";
        long expected = 323000;
        long result = HelperMethods.CountdownToMillis(test);
        assertEquals(result,expected);
    }
}