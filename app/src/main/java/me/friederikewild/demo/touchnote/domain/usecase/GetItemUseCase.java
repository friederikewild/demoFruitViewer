package me.friederikewild.demo.touchnote.domain.usecase;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.friederikewild.demo.touchnote.data.ItemsRepository;
import me.friederikewild.demo.touchnote.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Use Case to fetch item for id
 */
public class GetItemUseCase implements UseCase<GetItemUseCase.RequestParams, Item>
{
    @NonNull
    private final ItemsRepository repository;
    @NonNull
    private final ItemEntityDataMapper mapper;

    // TODO: Provide Schedulers for testing
    public GetItemUseCase(@NonNull final ItemsRepository repository,
                          @NonNull final ItemEntityDataMapper mapper)
    {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Single<Item> execute(RequestParams requestParams)
    {
        return repository.getItem(requestParams.getItemId())
                .filter(Optional::isPresent)
                .firstOrError()
                .map(Optional::get)
                .map(mapper::transform)
                // TODO: Provide Schedulers as injections
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
