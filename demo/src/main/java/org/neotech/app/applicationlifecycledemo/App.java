package org.neotech.app.applicationlifecycledemo;

import android.app.Application;
import android.widget.Toast;

import org.neotech.library.applicationlifecycle.ApplicationLifecycle;

/**
 * Created by Rolf Smit on 04-May-17.
 */

public class App extends Application implements ApplicationLifecycle.Listener {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationLifecycle.getInstance().init(this);

        /*
          Unregistering is not needed (in this specific case) for a listener attached to the
          Application class, because the Application class will live until the process the VM runs
          in is killed by Android.
         */
        ApplicationLifecycle.register(this);
    }

    @Override
    public void applicationDidEnterBackground() {
        Toast.makeText(this, "Application-Lifecycle: app is now running in the background!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void applicationDidEnterForeground() {
        Toast.makeText(this, "Application-Lifecycle: app is now running in the foreground!", Toast.LENGTH_SHORT).show();
    }
}
