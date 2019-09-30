package com.msd.lifestyleapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    //@Before - will be run before every test. @After - will run after each test.
    //@BeforeClass - will one once before all tests in this class, @AfterClasst

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}