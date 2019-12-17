package com.example.androidmusicplayer;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> mActivityTestRule = new ActivityTestRule<SignUpActivity>(SignUpActivity.class);

    private SignUpActivity mActivity = null;
    
    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity;
    }

    @Test
    public void testLaunch()
    {

    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}
