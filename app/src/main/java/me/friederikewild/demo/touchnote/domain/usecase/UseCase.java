package me.friederikewild.demo.touchnote.domain.usecase;

import android.support.annotation.NonNull;

/**
 * Abstract base form of a use case.
 * This defines the entry point into the domain layer.
 */
public abstract class UseCase<P extends UseCase.RequestParams, R extends UseCase.Result>
{
    private P requestParams;

    private UseCaseCallback<R> useCaseCallback;

    public void setRequestValues(@NonNull P requestParams)
    {
        this.requestParams = requestParams;
    }

    public P getRequestValues()
    {
        return requestParams;
    }

    public UseCaseCallback<R> getUseCaseCallback()
    {
        return useCaseCallback;
    }

    public void setUseCaseCallback(@NonNull UseCaseCallback<R> useCaseCallback)
    {
        this.useCaseCallback = useCaseCallback;
    }

    void run()
    {
        executeUseCase(requestParams);
    }

    /**
     * Start executing the use case
     *
     * @param requestParams Provide data needed for execution
     */
    protected abstract void executeUseCase(@NonNull P requestParams);


    /**
     * Provide data to pass with request
     */
    public interface RequestParams
    {
        // Empty
    }

    /**
     * Data received from a request
     */
    public interface Result
    {
        // Empty
    }

    /**
     * Callback for updates when use case was executed
     *
     * @param <CR> Result type
     */
    public interface UseCaseCallback<CR extends Result>
    {
        void onSuccess(@NonNull CR result);

        void onError();
    }
}
