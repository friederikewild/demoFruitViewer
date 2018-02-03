package me.friederikewild.demo.touchnote.data.datasource.remote;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.Lists;

import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;

/**
 * Alternative api provider to test empty return handling.
 */
@VisibleForTesting
public class EmptyItemsApiProvider implements ItemsApiProvider
{
    @Override
    public void enqueueGetItems(@NonNull ItemsDataStore.GetEntityItemsCallback callback,
                                @NonNull GetNoDataCallback errorCallback)
    {
        callback.onItemsLoaded(Lists.newArrayList());
    }
}
