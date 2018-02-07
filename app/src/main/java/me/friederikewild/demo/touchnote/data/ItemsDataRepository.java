package me.friederikewild.demo.touchnote.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.ItemCache;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

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
    public Flowable<List<ItemEntity>> getItems()
    {
        return getFlowableItemsFromRemoteAndCache()
                .toList()
                .toFlowable();
    }

    @SuppressWarnings("Convert2MethodRef")
    private Flowable<ItemEntity> getFlowableItemsFromRemoteAndCache()
    {
        return remoteItemsStore.getItems()
                .flatMap(tasks -> Flowable.fromIterable(tasks)
                        .doOnNext(item -> cacheItemStore.putItem(item)));
    }

    @Override
    public Flowable<Optional<ItemEntity>> getItem(@NonNull String itemId)
    {
        final Flowable<Optional<ItemEntity>> cachedItem = cacheItemStore.getItem(itemId);
        final Flowable<Optional<ItemEntity>> remoteItem = getItemFromRemoteDataStore(itemId);

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
    private Flowable<Optional<ItemEntity>> getItemFromRemoteDataStore(@NonNull String itemId)
    {
        return getFlowableItemsFromRemoteAndCache()
                .filter(itemEntity -> itemEntity.getId().equals(itemId))
                .map(itemEntity -> Optional.of(itemEntity));
    }
}
