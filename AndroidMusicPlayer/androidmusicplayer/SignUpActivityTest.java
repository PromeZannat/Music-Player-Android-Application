package com.example.androidmusicplayer;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import androidx.test.espresso.Espresso;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> mActivityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);

    private SignUpActivity mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);

    private Instrumentation getInstrumentation() {
    }

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity;
    }

    @Test
    public void testLaunch(){
        View view = mActivity.findViewById(R.id.signUpEmailEditTextId);
        View view = mActivity.findViewById(R.id.signUpPasswordEditTextId);

        assertNotNull(view);
    }
    public void testLaunchOfMainActivityOnButtonClick()
    {


        assertNotNull(mActivity.findViewById(R.id.signUpButtonId));

        onView(withId(R.id.signInButtonId)).perform(click());

       Activity mainActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

       assertNotNull(mainActivity);

       SignUpActivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}
