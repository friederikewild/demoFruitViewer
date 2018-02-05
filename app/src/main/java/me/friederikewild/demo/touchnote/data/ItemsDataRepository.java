package me.friederikewild.demo.touchnote.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.Collection;
import java.util.List;

import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.CacheItemDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.ItemCache;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import me.friederikewild.demo.touchnote.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.touchnote.domain.ItemsRepository;
import me.friederikewild.demo.touchnote.domain.model.Item;

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

    private final ItemEntityDataMapper mapper;

    private final ItemsDataStore remoteItemsStore;
    private final ItemCache cacheItemStore;

    // Prevent direct instantiation, but allow it from tests to inject mocks
    @VisibleForTesting
    ItemsDataRepository(
            @NonNull final ItemEntityDataMapper mapper,
            @NonNull final ItemsDataStore remote,
            @NonNull final ItemCache cache)
    {
        this.mapper = mapper;
        this.remoteItemsStore = remote;
        this.cacheItemStore = cache;
    }

    public static ItemsDataRepository getInstance(@NonNull final ItemEntityDataMapper mapper,
                                                  @NonNull final ItemsDataStore remoteItemsStore,
                                                  @NonNull final CacheItemDataStore cacheItemDataStore)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ItemsDataRepository(
                    mapper,
                    remoteItemsStore,
                    cacheItemDataStore);
        }
        return INSTANCE;
    }

    @SuppressWarnings("TrivialMethodReference")
    @Override
    public void getItems(@NonNull GetItemsCallback callback, @NonNull GetNoDataCallback errorCallback)
    {
        remoteItemsStore.getItems(itemEntityList ->
                                  {
                                      updateCache(itemEntityList);

                                      final List<Item> items = mapper.transform(itemEntityList);
                                      callback.onItemsLoaded(items);
                                  }, errorCallback::onNoDataAvailable);
    }

    private void updateCache(Collection<ItemEntity> itemEntityList)
    {
        cacheItemStore.clearAll();
        for (ItemEntity item : itemEntityList)
        {
            cacheItemStore.putItem(item);
        }
    }

    @Override
    public void getItem(@NonNull String itemId,
                        @NonNull GetItemCallback callback,
                        @NonNull GetNoDataCallback errorCallback)
    {
        if (!cacheItemStore.isExpired() && cacheItemStore.isCached(itemId))
        {
            cacheItemStore.getItem(itemId, itemEntity ->
            {
                final Item item = mapper.transform(itemEntity);
                callback.onItemLoaded(item);
            }, () -> getItemFromRemoteDataStore(itemId, callback, errorCallback));
        }
        else
        {
            getItemFromRemoteDataStore(itemId, callback, errorCallback);
        }
    }

    @Override
    public void refreshData()
    {
        cacheItemStore.clearAll();
    }

    /**
     * Fallback method to catch all items from remote to then pick item.
     *
     * @param itemId        Id to look up in cache
     * @param callback      Callback for updates
     * @param errorCallback Callback for error e.g. no data available
     */
    @SuppressWarnings("TrivialMethodReference")
    private void getItemFromRemoteDataStore(@NonNull String itemId,
                                            @NonNull GetItemCallback callback,
                                            @NonNull GetNoDataCallback errorCallback)
    {
        // Request to get all items and cache them
        getItems(items ->
                 {
                     if (!cacheItemStore.isExpired() && cacheItemStore.isCached(itemId))
                     {
                         cacheItemStore.getItem(itemId, itemEntity ->
                         {
                             final Item item = mapper.transform(itemEntity);
                             callback.onItemLoaded(item);
                         }, errorCallback::onNoDataAvailable);
                     }
                     else
                     {
                         // Still item not found
                         errorCallback.onNoDataAvailable();
                     }
                 }, errorCallback::onNoDataAvailable);
    }
}
