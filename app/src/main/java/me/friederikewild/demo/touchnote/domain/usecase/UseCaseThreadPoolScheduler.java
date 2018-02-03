package me.friederikewild.demo.touchnote.domain.usecase;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Executes {@link UseCase} as asynchronous tasks using a {@link ThreadPoolExecutor}
 */
public class UseCaseThreadPoolScheduler implements UseCaseScheduler
{
    private static final int POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final int TIMEOUT = 30;

    private final Handler handler = new Handler();

    private ThreadPoolExecutor threadPoolExecutor;

    public UseCaseThreadPoolScheduler()
    {
        threadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
                                                    TimeUnit.SECONDS,
                                                    new ArrayBlockingQueue<>(POOL_SIZE));
    }

    @Override
    public void execute(Runnable runnable)
    {
        threadPoolExecutor.execute(runnable);
    }

    @Override
    public <R extends UseCase.Result> void notifyResponse(final R response,
                                                          final UseCase.UseCaseCallback<R> useCaseCallback)
    {
        handler.post(() -> useCaseCallback.onSuccess(response));
    }

    @Override
    public <R extends UseCase.Result> void onError(
            final UseCase.UseCaseCallback<R> useCaseCallback)
    {
        handler.post(useCaseCallback::onError);
    }
}
