package me.friederikewild.demo.touchnote.domain.usecase;

import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.domain.ItemsRepository;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Use Case to fetch item for id
 */
public class GetItemUseCase extends UseCase<GetItemUseCase.RequestParams, GetItemUseCase.Result>
{
    @NonNull
    private final ItemsRepository repository;

    public GetItemUseCase(@NonNull ItemsRepository repository)
    {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(@NonNull RequestParams requestParams)
    {
        repository.getItem(requestParams.getItemId(),
                           item -> getUseCaseCallback().onSuccess(new GetItemUseCase.Result(item)),
                           () -> getUseCaseCallback().onError());
    }

    public static final class RequestParams implements UseCase.RequestParams
    {
        @NonNull
        private final String itemId;

        public RequestParams(@NonNull String itemId)
        {
            this.itemId = itemId;
        }

        @NonNull
        public String getItemId()
        {
            return itemId;
        }
    }

    public static final class Result implements UseCase.Result
    {
        @NonNull
        private final Item item;

        public Result(@NonNull Item item)
        {
            this.item = item;
        }

        @NonNull
        public Item getItem()
        {
            return item;
        }
    }
}
