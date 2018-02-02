package me.friederikewild.demo.touchnote.data.datasource;

import android.support.annotation.NonNull;

import java.util.Collection;

import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

/**
 * Define data layer access to items as collection and specific items via id.
 */
public interface ItemsDataStore extends ItemDataStore
{
    interface GetEntityItemsCallback
    {
        void onItemsLoaded(@NonNull Collection<ItemEntity> items);
    }

    /**
     * Request list of items and get them via provided callback
     *
     * @param callback      Callback for updates
     * @param errorCallback Callback for error e.g. no data available
     */
    void getItems(@NonNull GetEntityItemsCallback callback,
                  @NonNull GetNoDataCallback errorCallback);
}
