package me.friederikewild.demo.fruits;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import timber.log.Timber;

/**
 * Extend application to allow for enhanced logging using Timber.
 */
public class DemoApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        if (BuildConfig.DEBUG)
        {
            Timber.plant(new Timber.DebugTree());
        }
        else
        {
            Timber.plant(new CrashReportingTree());
        }
    }

    /**
     * A tree which logs important information for crash reporting.
     * For simplification just a stricter version of the {@link timber.log.Timber.DebugTree}.
     */
    private static class CrashReportingTree extends Timber.DebugTree
    {
        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t)
        {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
            {
                return;
            }

            // Log the critical priorities
            super.log(priority, tag, message, t);
        }
    }
}
