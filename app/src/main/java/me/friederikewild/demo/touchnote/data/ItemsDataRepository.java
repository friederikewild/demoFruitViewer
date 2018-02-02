package me.friederikewild.demo.touchnote.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.CacheItemDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.ItemCache;
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

    private ItemEntityDataMapper mapper;

    private ItemsDataStore remoteItemsStore;
    private ItemCache cacheItemStore;

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
                                                  @NonNull final ItemsDataStore remoteItemsStore)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ItemsDataRepository(
                    mapper,
                    remoteItemsStore,
                    CacheItemDataStore.getInstance());
        }
        return INSTANCE;
    }

    @Override
    public void getItems(@NonNull GetItemsCallback callback, @NonNull GetNoDataCallback errorCallback)
    {
        // Clear cache on receiving data
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
                if (item != null)
                {
                    callback.onItemLoaded(item);
                }
            }, () -> getItemFromRemoteDataStore(itemId, callback, errorCallback));
        }
        else
        {
            getItemFromRemoteDataStore(itemId, callback, errorCallback);
        }
    }

    private void getItemFromRemoteDataStore(@NonNull String itemId,
                                            @NonNull GetItemCallback callback,
                                            @NonNull GetNoDataCallback errorCallback)
    {
        remoteItemsStore.getItem(itemId, itemEntity ->
        {
            final Item item = mapper.transform(itemEntity);
            if (item != null)
            {
                callback.onItemLoaded(item);
            }
        }, errorCallback::onNoDataAvailable);
    }

}
