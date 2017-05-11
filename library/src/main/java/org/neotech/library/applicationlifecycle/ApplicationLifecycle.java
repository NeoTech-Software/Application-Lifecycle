package org.neotech.library.applicationlifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility class which can be used to listen to "application" global lifecycle events. Application
 * lifecycle events are app global events similar to the events found in iOS like:
 * applicationDidEnterBackground(). Currently two events are supported:
 * {@link Listener#applicationDidEnterForeground()} and
 * {@link Listener#applicationDidEnterBackground()}. In order to use this class you must initialize
 * it using the {@link ApplicationLifecycle#init(Application)} method. In order for it to work
 * correctly you must do this (as soon as possible) in the {@link Application#onCreate()} method.
 *
 * This class can be used either using the static state getters: {@link #isAppInBackground()} and
 * {@link #isAppInForeground()} or by registering a listener using {@link #register(Listener)}, when
 * using the last method you must unregister the listener
 *
 * Created by Rolf Smit on 15-Sep-16, modified 3-May-17.
 */
public class ApplicationLifecycle {

    private static ApplicationLifecycle sInstance = null;

    private final CopyOnWriteArraySet<Listener> listeners = new CopyOnWriteArraySet<>();
    private final ActivityLifecycleCounter counter = new ActivityLifecycleCounter();
    private final AtomicBoolean didInit = new AtomicBoolean(false);

    private ApplicationLifecycle(){
        // Private constructor (singleton pattern)
    }

    /**
     * Returns the singleton instance of the ApplicationLifecycle class.
     * @return The global instance of the ApplicationLifecycel class.
     */
    public static synchronized ApplicationLifecycle getInstance(){
        if(sInstance == null){
            sInstance = new ApplicationLifecycle();
        }
        return sInstance;
    }

    /**
     * Initialize the ApplicationLifecycle class. This method must be called (as soon as possible)
     * in your {@link Application#onCreate()} method!
     * @param application the Application instance.
     */
    public synchronized void init(Application application){
        if(!didInit.compareAndSet(false, true)){
            throw new IllegalStateException("init() has already been called, the ApplicationLifecycle class must only be initialized once!");
        }
        application.registerActivityLifecycleCallbacks(counter);
    }

    /**
     * Register a {@link ApplicationLifecycle.Listener}, you must manually unregister the listener
     * using {@link #unregister(Listener)} in order for it to be garbage collected.
     * @param applicationBackgroundListener the listener to register.
     */
    public static void register(Listener applicationBackgroundListener){
        getInstance().listeners.add(applicationBackgroundListener);
    }

    /**
     * Unregister a {@link ApplicationLifecycle.Listener}.
     * @param applicationBackgroundListener the listener to unregister.
     * @see #register(Listener)
     */
    public static void unregister(Listener applicationBackgroundListener){
        getInstance().listeners.remove(applicationBackgroundListener);
    }

    /**
     * Check whether the app is in the background, in the background means that all running
     * Activities are stopped if any.
     * @return true if the app is in the background.
     * @see ApplicationLifecycle#isAppInForeground()
     */
    public static boolean isAppInBackground(){
        return getInstance().counter.started == 0;
    }

    /**
     * Check whether the app is in the foreground, in the foreground means that at least one running
     * Activity is started.
     * @return true if the app is in the foreground.
     * @see ApplicationLifecycle#isAppInBackground()
     */
    public static boolean isAppInForeground(){
        return !isAppInBackground();
    }

    /**
     * Listener for receiving Application lifecycle events.
     */
    public interface Listener {
        void applicationDidEnterBackground();
        void applicationDidEnterForeground();
    }

    private class ActivityLifecycleCounter implements Application.ActivityLifecycleCallbacks {

        private int started = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            started++;
            if(started == 1){
                for(Listener listener: listeners){
                    listener.applicationDidEnterForeground();
                }
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            started--;
            if(started == 0){
                for(Listener listener: listeners){
                    listener.applicationDidEnterBackground();
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }
}