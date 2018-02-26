package me.friederikewild.demo.fruits.util.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Collection of {@link Scheduler} for RxJava calls
 * Setup as a singleton.
 */
public class RxSchedulerProvider implements BaseSchedulerProvider
{
    private static RxSchedulerProvider INSTANCE;

    private RxSchedulerProvider()
    {
        // Hide
    }

    public static RxSchedulerProvider getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RxSchedulerProvider();
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public Scheduler mainThread()
    {
        return AndroidSchedulers.mainThread();
    }

    @NonNull
    @Override
    public Scheduler asyncIoThread()
    {
        return Schedulers.io();
    }

    @NonNull
    @Override
    public Scheduler computationThread()
    {
        return Schedulers.computation();
    }
}
