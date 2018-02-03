package me.friederikewild.demo.touchnote.data.datasource.remote;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;

/**
 * Concrete remote data store.
 * Setup as a singleton.
 */
public class RemoteItemsDataStore implements ItemsDataStore
{
    private static RemoteItemsDataStore INSTANCE;

    @NonNull
    private ItemsApiProvider itemsApiProvider;

    @VisibleForTesting
    RemoteItemsDataStore(@NonNull ItemsApiProvider itemsApiProvider)
    {
        this.itemsApiProvider = itemsApiProvider;
    }

    public static RemoteItemsDataStore getInstance(@NonNull ItemsApiProvider itemsApiProvider)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RemoteItemsDataStore(itemsApiProvider);
        }
        return INSTANCE;
    }

    @Override
    public void getItems(@NonNull GetEntityItemsCallback callback,
                         @NonNull GetNoDataCallback errorCallback)
    {
        itemsApiProvider.enqueueGetItems(callback, errorCallback);
    }
}
