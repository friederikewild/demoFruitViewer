package me.friederikewild.demo.touchnote.domain.usecase;

/**
 * Interface for schedulers, see {@link UseCaseThreadPoolScheduler}.
 */
public interface UseCaseScheduler
{
    void execute(Runnable runnable);

    <R extends UseCase.Result> void notifyResponse(final R response,
                                                   final UseCase.UseCaseCallback<R> useCaseCallback);

    <R extends UseCase.Result> void onError(
            final UseCase.UseCaseCallback<R> useCaseCallback);
}
