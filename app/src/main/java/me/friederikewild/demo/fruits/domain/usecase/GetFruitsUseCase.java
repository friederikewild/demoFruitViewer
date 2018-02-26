package me.friederikewild.demo.fruits.domain.usecase;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import me.friederikewild.demo.fruits.data.FruitsRepository;
import me.friederikewild.demo.fruits.data.entity.mapper.FruitEntityDataMapper;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.util.schedulers.BaseSchedulerProvider;

/**
 * Use Case to fetch fruits
 */
public class GetFruitsUseCase implements UseCase<GetFruitsUseCase.RequestParams, List<Fruit>>
{
    @NonNull
    private final FruitsRepository repository;
    @NonNull
    private final FruitEntityDataMapper mapper;
    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    public GetFruitsUseCase(@NonNull final FruitsRepository repository,
                            @NonNull final FruitEntityDataMapper mapper,
                            @NonNull final BaseSchedulerProvider schedulerProvider)
    {
        this.repository = repository;
        this.mapper = mapper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Single<List<Fruit>> execute(@NonNull RequestParams requestParams)
    {
        if (requestParams.forceUpdate)
        {
            repository.refreshData();
        }

        return repository.getFruits()
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
