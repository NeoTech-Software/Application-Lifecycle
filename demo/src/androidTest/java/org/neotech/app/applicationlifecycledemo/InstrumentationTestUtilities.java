package org.neotech.app.applicationlifecycledemo;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

/**
 * Created by Rolf Smit on 12-May-17.
 */
public class InstrumentationTestUtilities {

    public static CharSequence getLauncherNameForActivity(ActivityTestRule<?> activityTestRule) {
        final PackageManager packageManager = activityTestRule.getActivity().getPackageManager();
        try {
            ActivityInfo info = packageManager.getActivityInfo(activityTestRule.getActivity().getComponentName(), 0);
            return info.loadLabel(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void bringAppToForeground(ActivityTestRule<?> activityTestRule) throws RemoteException, UiObjectNotFoundException {
        final UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        uiDevice.pressRecentApps();
        uiDevice.findObject(new UiSelector().description(getLauncherNameForActivity(activityTestRule).toString())).click();
    }
}
