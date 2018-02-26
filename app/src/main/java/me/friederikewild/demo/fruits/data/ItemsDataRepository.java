package me.friederikewild.demo.fruits.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.datasource.ItemsDataStore;
import me.friederikewild.demo.fruits.data.datasource.cache.ItemCache;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Concrete repository getting data from remote or cache.
 * For simplicity, the synchronisation between locally cached and
 * remote data is kept to a minimum. When remote data is received the cache will be overwritten,
 * When an item is requested it will be used from cache if available.
 * Setup as a singleton.
 */
public class ItemsDataRepository implements ItemsRepository
{
    private static ItemsDataRepository INSTANCE;

    private final ItemsDataStore remoteItemsStore;
    private final ItemCache cacheItemStore;

    // Prevent direct instantiation, but allow it from tests to inject mocks
    @VisibleForTesting
    ItemsDataRepository(
            @NonNull final ItemsDataStore remote,
            @NonNull final ItemCache cache)
    {
        this.remoteItemsStore = remote;
        this.cacheItemStore = cache;
    }

    public static ItemsDataRepository getInstance(@NonNull final ItemsDataStore remoteItemsStore,
                                                  @NonNull final ItemCache cacheItemDataStore)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ItemsDataRepository(
                    remoteItemsStore,
                    cacheItemDataStore);
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<FruitEntity>> getItems()
    {
        return getItemsFromRemoteAndCache()
                .firstOrError()
                .toFlowable();
    }

    @SuppressWarnings("Convert2MethodRef")
    private Flowable<List<FruitEntity>> getItemsFromRemoteAndCache()
    {
        return remoteItemsStore.getItems()
                .flatMap(tasks -> Flowable.fromIterable(tasks)
                        .doOnNext(item -> cacheItemStore.putItem(item))
                        .toList()
                        .toFlowable());
    }

    @Override
    public Flowable<Optional<FruitEntity>> getItem(@NonNull String itemId)
    {
        final Flowable<Optional<FruitEntity>> cachedItem = cacheItemStore.getItem(itemId);
        final Flowable<Optional<FruitEntity>> remoteItem = getItemFromRemoteDataStore(itemId);

        // Request cache first, if not available, request remote
        return Flowable.concat(cachedItem, remoteItem)
                .firstElement()
                .toFlowable();
    }

    @Override
    public void refreshData()
    {
        cacheItemStore.clearAll();
    }

    /**
     * Fallback method to catch all items from remote to then pick item.
     *
     * @param itemId Id to look up in cache
     */
    @SuppressWarnings({"Convert2MethodRef", "Guava"})
    private Flowable<Optional<FruitEntity>> getItemFromRemoteDataStore(@NonNull String itemId)
    {
        return getItemsFromRemoteAndCache()
                .flatMap(items -> Flowable.fromIterable(items))
                .filter(itemEntity -> itemEntity.getId().equals(itemId))
                .map(itemEntity -> Optional.of(itemEntity))
                .firstOrError()
                .toFlowable();
    }
}
