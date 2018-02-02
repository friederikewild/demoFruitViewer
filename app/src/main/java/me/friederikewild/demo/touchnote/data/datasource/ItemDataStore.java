package me.friederikewild.demo.touchnote.data.datasource;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

/**
 * Define data layer access to single item.
 */
public interface ItemDataStore
{
    /**
     * Get concrete item for specific id
     *
     * @param itemId Id to look up in cache
     * @return The cached item or {@code null} in case not cached
     */
    @Nullable
    ItemEntity getItem(@NonNull String itemId);
}
