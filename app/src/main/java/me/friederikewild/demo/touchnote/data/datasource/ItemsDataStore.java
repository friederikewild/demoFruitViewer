package me.friederikewild.demo.touchnote.data.datasource;

import android.support.annotation.NonNull;

import java.util.Collection;

import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

/**
 * Define data layer access to items as collection and specific items via id.
 */
public interface ItemsDataStore extends ItemDataStore
{
    interface GetEntityItemsCallback
    {
        void onItemsLoaded(@NonNull Collection<ItemEntity> items);

        void onNoDataAvailable();
    }

    /**
     * Request list of items and get them via provided callback
     *
     * @param callback Callback for updates on loaded or error
     */
    void getItems(@NonNull GetEntityItemsCallback callback);
}
