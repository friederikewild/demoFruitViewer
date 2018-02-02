package me.friederikewild.demo.touchnote.data.datasource;

import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

/**
 * Define data layer access to single item.
 */
public interface ItemDataStore
{
    interface GetEntityItemCallback
    {
        void onItemLoaded(@NonNull ItemEntity itemEntity);
    }

    /**
     * Request concrete item for specific id and get it via provided callback
     *
     * @param itemId        Id to look up in cache
     * @param callback      Callback for updates
     * @param errorCallback Callback for error e.g. no data available
     */
    void getItem(@NonNull String itemId,
                 @NonNull GetEntityItemCallback callback,
                 @NonNull GetNoDataCallback errorCallback);
}
