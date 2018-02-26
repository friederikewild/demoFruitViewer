package me.friederikewild.demo.fruits.domain.usecase;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import io.reactivex.Single;
import me.friederikewild.demo.fruits.data.FruitsRepository;
import me.friederikewild.demo.fruits.data.entity.mapper.FruitEntityDataMapper;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.util.schedulers.BaseSchedulerProvider;

/**
 * Use Case to fetch fruit for id
 */
public class GetFruitUseCase implements UseCase<GetFruitUseCase.RequestParams, Fruit>
{
    @NonNull
    private final FruitsRepository repository;
    @NonNull
    private final FruitEntityDataMapper mapper;
    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    public GetFruitUseCase(@NonNull final FruitsRepository repository,
                           @NonNull final FruitEntityDataMapper mapper,
                           @NonNull final BaseSchedulerProvider schedulerProvider)
    {
        this.repository = repository;
        this.mapper = mapper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Single<Fruit> execute(RequestParams requestParams)
    {
        return repository.getFruit(requestParams.getFruitId())
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
        private final String fruitId;

        public RequestParams(@NonNull String fruitId)
        {
            this.fruitId = fruitId;
        }

        @NonNull
        String getFruitId()
        {
            return fruitId;
        }
    }
}
