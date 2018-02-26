package me.friederikewild.demo.fruits.domain.usecase;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import io.reactivex.Single;
import me.friederikewild.demo.fruits.data.FruitsRepository;
import me.friederikewild.demo.fruits.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.util.schedulers.BaseSchedulerProvider;

/**
 * Use Case to fetch item for id
 */
public class GetItemUseCase implements UseCase<GetItemUseCase.RequestParams, Fruit>
{
    @NonNull
    private final FruitsRepository repository;
    @NonNull
    private final ItemEntityDataMapper mapper;
    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    public GetItemUseCase(@NonNull final FruitsRepository repository,
                          @NonNull final ItemEntityDataMapper mapper,
                          @NonNull final BaseSchedulerProvider schedulerProvider)
    {
        this.repository = repository;
        this.mapper = mapper;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Single<Fruit> execute(RequestParams requestParams)
    {
        return repository.getFruit(requestParams.getItemId())
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
