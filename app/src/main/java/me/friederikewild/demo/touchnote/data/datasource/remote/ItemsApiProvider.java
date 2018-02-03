package me.friederikewild.demo.touchnote.data.datasource.remote;

import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore.GetEntityItemsCallback;

/**
 * Define how items will be provided from the {@link ItemsApi} api
 * allowing no knowledge of used library to fetch items.
 */
public interface ItemsApiProvider
{
    /**
     * Asynchronously send the request to {@link ItemsApi#getItems}.
     * Notify {@param callback} with the data of its response
     * or {@param errorCallback} if an error occurred talking to the server,
     * creating the request, or while processing the response.
     *
     * @param callback      Callback for updates
     * @param errorCallback Callback for error e.g. no data available
     */
    void enqueueGetItems(@NonNull GetEntityItemsCallback callback,
                         @NonNull GetNoDataCallback errorCallback);
}
