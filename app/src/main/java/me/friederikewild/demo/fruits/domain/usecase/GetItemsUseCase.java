package me.friederikewild.demo.fruits.domain.usecase;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import me.friederikewild.demo.fruits.data.ItemsRepository;
import me.friederikewild.demo.fruits.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.fruits.domain.model.Item;
import me.friederikewild.demo.fruits.util.schedulers.BaseSchedulerProvider;

/**
 * Use Case to fetch items
 */
public class GetItemsUseCase implements UseCase<GetItemsUseCase.RequestParams, List<Item>>
{
    @NonNull
    private final ItemsRepository repository;
    @NonNull
    private final ItemEntityDataMapper mapper;
    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    public GetItemsUseCase(@NonNull final ItemsRepository repository,
                           @NonNull final ItemEntityDataMapper mapper,
                           @NonNull final BaseSchedulerProvider schedulerProvider)
    {
        this.repository = repository;
        this.mapper = mapper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Single<List<Item>> execute(@NonNull RequestParams requestParams)
    {
        if (requestParams.forceUpdate)
        {
            repository.refreshData();
        }

        return repository.getItems()
                .flatMap(Flowable::fromIterable)
                .map(mapper::transform)
                .toList()
                .subscribeOn(schedulerProvider.asyncIoThread())
                .observeOn(schedulerProvider.mainThread());
    }

    public static final class RequestParams implements UseCase.RequestParams
    {
        private final boolean forceUpdate;

        public RequestParams(boolean forceUpdate)
        {
            this.forceUpdate = forceUpdate;
        }
    }
}
