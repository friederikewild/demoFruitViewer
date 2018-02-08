package me.friederikewild.demo.touchnote.util.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Schedulers for testing are executed synchronously.
 */
public class InstantTestSchedulerProvider implements BaseSchedulerProvider
{
    @NonNull
    @Override
    public Scheduler mainThread()
    {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler asyncIoThread()
    {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler computationThread()
    {
        return Schedulers.trampoline();
    }
}
