package me.friederikewild.demo.touchnote.util.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Provide the different {@link io.reactivex.Scheduler} options.
 */
public interface BaseSchedulerProvider
{
    @NonNull
    Scheduler mainThread();

    @NonNull
    Scheduler asyncIoThread();

    @NonNull
    Scheduler computationThread();

}
