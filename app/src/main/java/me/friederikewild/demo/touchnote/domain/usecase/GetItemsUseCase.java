package me.friederikewild.demo.touchnote.domain.usecase;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.friederikewild.demo.touchnote.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.touchnote.data.ItemsRepository;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Use Case to fetch items
 */
public class GetItemsUseCase implements UseCase<GetItemsUseCase.RequestParams, List<Item>>
{
    @NonNull
    private final ItemsRepository repository;
    @NonNull
    private final ItemEntityDataMapper mapper;

    // TODO: Provide Schedulers for testing
    public GetItemsUseCase(@NonNull final ItemsRepository repository,
                           @NonNull final ItemEntityDataMapper mapper)
    {
        this.repository = repository;
        this.mapper = mapper;
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
                // TODO: Provide Schedulers as injections
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
