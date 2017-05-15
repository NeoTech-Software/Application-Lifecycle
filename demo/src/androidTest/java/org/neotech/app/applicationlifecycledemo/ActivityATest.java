package org.neotech.app.applicationlifecycledemo;


import android.os.RemoteException;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neotech.library.applicationlifecycle.ApplicationLifecycle;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ActivityATest {

    @Rule
    public ActivityTestRule<ActivityA> mActivityTestRule = new ActivityTestRule<>(ActivityA.class);

    private UiDevice mDevice;

    @Before
    public void setUp() throws Exception{
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void activityATest() throws UiObjectNotFoundException, RemoteException {

        // 1. The Activity is started by the test, assert that the app is in the foreground
        Assert.assertTrue(ApplicationLifecycle.isAppInForeground());
        Assert.assertFalse(ApplicationLifecycle.isAppInBackground());

        onView(withId(android.R.id.text1)).check(matches(withText("Main Activity")));

        // 2. Click the "Add another Activity" button
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.button), withText("Add another Activity to the back-stack"), isDisplayed()));
        appCompatButton.perform(click());

        // 3.1 Assert that the app is still in the foreground
        Assert.assertTrue(ApplicationLifecycle.isAppInForeground());
        Assert.assertFalse(ApplicationLifecycle.isAppInBackground());

        // 3.2 Assert that the first child activity contains the correct text
        onView(withId(android.R.id.text1)).check(matches(withText("Activity 1")));

        // 4. Click the home button so that the app goes to the background
        mDevice.pressHome();

        // 5. Assert that the app is in the background.
        Assert.assertFalse(ApplicationLifecycle.isAppInForeground());
        Assert.assertTrue(ApplicationLifecycle.isAppInBackground());

        // 6. Bring the app to the foreground
        InstrumentationTestUtilities.bringAppToForeground(mActivityTestRule);

        // 7.1 Assert that the app is in the foreground
        Assert.assertTrue(ApplicationLifecycle.isAppInForeground());
        Assert.assertFalse(ApplicationLifecycle.isAppInBackground());

        // 7.2 Assert that the first child activity contains the correct text
        onView(withId(android.R.id.text1)).check(matches(withText("Activity 1")));

        // 8. Start two more child activities
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button), withText("Add another Activity to the back-stack"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.button), withText("Add another Activity to the back-stack"), isDisplayed()));
        appCompatButton3.perform(click());

        // 9.1 Assert that the app is in the foreground
        Assert.assertTrue(ApplicationLifecycle.isAppInForeground());
        Assert.assertFalse(ApplicationLifecycle.isAppInBackground());

        // 9.2 Assert that the first child activity contains the correct text
        onView(withId(android.R.id.text1)).check(matches(withText("Activity 3")));

        // 10. Close the child activities and the first activity (main)
        mDevice.pressBack();
        mDevice.pressBack();
        mDevice.pressBack();
        mDevice.pressBack();

        SystemClock.sleep(1000);

        // 11. Assert that the app is in the background
        Assert.assertFalse(ApplicationLifecycle.isAppInForeground());
        Assert.assertTrue(ApplicationLifecycle.isAppInBackground());
    }

}
