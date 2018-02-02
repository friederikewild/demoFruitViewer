package me.friederikewild.demo.touchnote.domain;

import android.support.annotation.NonNull;

import java.util.Collection;

import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Define domain data access as interface for the repository.
 * Loading/Item access has simple callbacks to inform about loaded data / errors since calls are done asynchronously.
 */
public interface ItemsRepository
{
    interface GetItemsCallback
    {
        void onItemsLoaded(@NonNull Collection<Item> items);
    }

    interface GetItemCallback
    {
        void onItemLoaded(@NonNull Item item);
    }

    /**
     * Request list of items and get them via provided callback
     *
     * @param callback      Callback for updates on loaded or error
     * @param errorCallback Callback for error e.g. no data available
     */
    void getItems(@NonNull GetItemsCallback callback, @NonNull GetNoDataCallback errorCallback);

    /**
     * Request concrete item for specific id and get it via provided callback
     *
     * @param itemId        Id to look up in cache
     * @param callback      Callback for updates on loaded or error
     * @param errorCallback Callback for error e.g. no data available
     */
    void getItem(@NonNull String itemId, @NonNull GetItemCallback callback,
                 @NonNull GetNoDataCallback errorCallback);
}
