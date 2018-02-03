package me.friederikewild.demo.touchnote;

import me.friederikewild.demo.touchnote.domain.usecase.UseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCaseScheduler;

/**
 * A scheduler that executes synchronously, for testing purposes.
 */
public class TestUseCaseScheduler implements UseCaseScheduler
{
    @Override
    public void execute(Runnable runnable)
    {
        runnable.run();
    }

    @Override
    public <TR extends UseCase.Result> void notifyResponse(TR response,
                                                           UseCase.UseCaseCallback<TR> useCaseCallback)
    {
        useCaseCallback.onSuccess(response);
    }

    @Override
    public <TR extends UseCase.Result> void onError(
            UseCase.UseCaseCallback<TR> useCaseCallback)
    {
        useCaseCallback.onError();
    }
}
