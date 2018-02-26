package me.friederikewild.demo.fruits.domain.usecase;

import io.reactivex.Single;

/**
 * Abstract base form of a use case.
 * This defines the entry point into the domain layer.
 *
 * Type P for Request Params
 * Type R for Response
 */
public interface UseCase<P extends UseCase.RequestParams, R>
{
    /**
     * Start executing the use case
     *
     * @param requestParams Provide data needed for execution. Mark @Nullable if not used by concrete UseCase
     * @return Observable stream emitting Result R
     */
    Single<R> execute(P requestParams);

    /**
     * Provide data to pass with request
     */
    interface RequestParams
    {
        // Empty
    }
}
