package me.friederikewild.demo.fruits.domain.usecase;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import io.reactivex.Single;
import me.friederikewild.demo.fruits.data.ItemsRepository;
import me.friederikewild.demo.fruits.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.fruits.domain.model.Item;
import me.friederikewild.demo.fruits.util.schedulers.BaseSchedulerProvider;

/**
 * Use Case to fetch item for id
 */
public class GetItemUseCase implements UseCase<GetItemUseCase.RequestParams, Item>
{
    @NonNull
    private final ItemsRepository repository;
    @NonNull
    private final ItemEntityDataMapper mapper;
    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    public GetItemUseCase(@NonNull final ItemsRepository repository,
                          @NonNull final ItemEntityDataMapper mapper,
                          @NonNull final BaseSchedulerProvider schedulerProvider)
    {
        this.repository = repository;
        this.mapper = mapper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Single<Item> execute(RequestParams requestParams)
    {
        return repository.getItem(requestParams.getItemId())
                .filter(Optional::isPresent)
                .firstOrError()
                .map(Optional::get)
                .map(mapper::transform)
                .subscribeOn(schedulerProvider.asyncIoThread())
                .observeOn(schedulerProvider.mainThread());
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
        String getItemId()
        {
            return itemId;
        }
    }
}
