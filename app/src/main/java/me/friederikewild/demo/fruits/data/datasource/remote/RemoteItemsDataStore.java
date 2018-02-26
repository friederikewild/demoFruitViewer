package me.friederikewild.demo.fruits.data.datasource.remote;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.datasource.ItemsDataStore;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Concrete remote data store
 *
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
    public Flowable<List<FruitEntity>> getItems()
    {
        final FruitsApi fruitsApi = itemsApiProvider.getItemsApi();
        return fruitsApi.getFruits();
    }
}
