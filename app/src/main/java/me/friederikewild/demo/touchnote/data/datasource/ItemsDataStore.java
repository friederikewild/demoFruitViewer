package me.friederikewild.demo.touchnote.data.datasource;

import android.support.annotation.NonNull;

import java.util.Collection;

import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

/**
 * Define data layer access to items.
 */
public interface ItemsDataStore
{
    /**
     * Get list of items
     *
     * @return Collection with all items
     */
    @NonNull
    Collection<ItemEntity> getItems();
}
