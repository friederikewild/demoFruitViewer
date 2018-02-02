package me.friederikewild.demo.touchnote.data.datasource.cache;

import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.data.datasource.ItemDataStore;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

/**
 * Interface for caching items and retrieving them by id.
 * Loading is expected to be quick, therefore no extra callbacks.
 */
public interface ItemCache extends ItemDataStore
{
    /**
     * Save item in cache
     *
     * @param item The item to cache
     */
    void putItem(@NonNull ItemEntity item);

    /**
     * Check if item with provided id is cached
     *
     * @param itemId Id to look up in cache
     * @return {@code true} if cached, otherwise {@code false}
     */
    boolean isCached(final @NonNull String itemId);

    /**
     * Check if cache is still trustworthy
     *
     * @return Flag if cache is expired
     */
    boolean isExpired();

    /**
     * Clear complete cache e.g. when new data was received.
     */
    void clearAll();
}
