package me.friederikewild.demo.touchnote.domain.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}
 */
public class UseCaseHandler
{
    private static UseCaseHandler INSTANCE;

    private final UseCaseScheduler useCaseScheduler;

    @VisibleForTesting
    public UseCaseHandler(UseCaseScheduler useCaseScheduler)
    {
        this.useCaseScheduler = useCaseScheduler;
    }

    public static UseCaseHandler getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return INSTANCE;
    }

    public <T extends UseCase.RequestParams, R extends UseCase.Result> void execute(
            final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback)
    {
        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));

        useCaseScheduler.execute(useCase::run);
    }

    public <V extends UseCase.Result> void notifyResponse(final V response,
                                                          final UseCase.UseCaseCallback<V> useCaseCallback)
    {
        useCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends UseCase.Result> void notifyError(final UseCase.UseCaseCallback<V> useCaseCallback)
    {
        useCaseScheduler.onError(useCaseCallback);
    }

    private static final class UiCallbackWrapper<V extends UseCase.Result>
            implements UseCase.UseCaseCallback<V>
    {
        private final UseCase.UseCaseCallback<V> callback;
        private final UseCaseHandler useCaseHandler;

        UiCallbackWrapper(UseCase.UseCaseCallback<V> callback,
                          UseCaseHandler useCaseHandler)
        {
            this.callback = callback;
            this.useCaseHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(@NonNull V response)
        {
            useCaseHandler.notifyResponse(response, callback);
        }

        @Override
        public void onError()
        {
            useCaseHandler.notifyError(callback);
        }
    }
}
