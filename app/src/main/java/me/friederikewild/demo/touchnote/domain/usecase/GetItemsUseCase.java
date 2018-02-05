package me.friederikewild.demo.touchnote.domain.usecase;

import android.support.annotation.NonNull;

import java.util.List;

import me.friederikewild.demo.touchnote.domain.ItemsRepository;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Use Case to fetch items
 */
public class GetItemsUseCase extends UseCase<GetItemsUseCase.RequestParams, GetItemsUseCase.Result>
{
    @NonNull
    private final ItemsRepository repository;

    public GetItemsUseCase(@NonNull ItemsRepository repository)
    {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(@NonNull RequestParams requestParams)
    {
        if (requestParams.forceUpdate)
        {
            repository.refreshData();
        }

        repository.getItems(items -> getUseCaseCallback().onSuccess(new Result(items)),
                            () -> getUseCaseCallback().onError());
    }

    public static final class RequestParams implements UseCase.RequestParams
    {
        private final boolean forceUpdate;

        public RequestParams(boolean forceUpdate)
        {
            this.forceUpdate = forceUpdate;
        }
    }

    public static final class Result implements UseCase.Result
    {
        private final List<Item> items;

        public Result(@NonNull List<Item> items)
        {
            this.items = items;
        }

        @NonNull
        public List<Item> getItems()
        {
            return items;
        }
    }
}
