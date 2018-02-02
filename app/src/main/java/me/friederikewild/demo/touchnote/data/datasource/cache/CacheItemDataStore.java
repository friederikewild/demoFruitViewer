package me.friederikewild.demo.touchnote.data.datasource.cache;

import android.support.annotation.NonNull;

import java.util.WeakHashMap;

import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

/**
 * Concrete data access cashed in memory for quick ui updates.
 * Elements are saved in a {@code WeakHashMap} to allow garbage collection when needed.
 * Setup as a singleton.
 */
public class CacheItemDataStore implements ItemCache
{
    private static CacheItemDataStore INSTANCE;

    @NonNull
    private final CurrentTimeProvider timeProvider;

    /**
     * Keep cache for 10 minutes
     */
    static final long EXPIRATION_TIME = 10L * 60L * 1000L;

    private final WeakHashMap<String, ItemEntity> itemCache;
    private long lastUpdatedTimeMillis = 0;

    // Prevent direct instantiation, but allow it from tests to inject mocks
    CacheItemDataStore(@NonNull CurrentTimeProvider timeProvider)
    {
        itemCache = new WeakHashMap<>(20);
        this.timeProvider = timeProvider;
    }

    public static CacheItemDataStore getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CacheItemDataStore(new CurrentTimeProvider());
        }
        return INSTANCE;
    }

    @Override
    public void getItem(@NonNull String itemId, @NonNull GetEntityItemCallback callback)
    {
        if (isCached(itemId))
        {
            callback.onItemLoaded(getItem(itemId));
        }
        else
        {
            callback.onNoDataAvailable();
        }
    }

    private ItemEntity getItem(@NonNull String itemId)
    {
        return itemCache.get(itemId);
    }

    @Override
    public void putItem(@NonNull ItemEntity item)
    {
        itemCache.put(item.getId(), item);
        saveNowAsLastCacheUpdate();
    }

    private void saveNowAsLastCacheUpdate()
    {
        lastUpdatedTimeMillis = timeProvider.getCurrentTimeMillis();
    }

    @Override
    public boolean isCached(@NonNull String itemId)
    {
        return itemCache.containsKey(itemId);
    }

    @Override
    public boolean isExpired()
    {
        long currentTimeMillis = timeProvider.getCurrentTimeMillis();

        boolean isExpired = ((currentTimeMillis - lastUpdatedTimeMillis) > EXPIRATION_TIME);

        if (isExpired)
        {
            clearAll();
        }

        return isExpired;
    }

    @Override
    public void clearAll()
    {
        itemCache.clear();
    }
}
