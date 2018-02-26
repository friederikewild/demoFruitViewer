package me.friederikewild.demo.fruits.data.datasource.remote;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.datasource.FruitsDataStore;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Concrete remote data store
 *
 * Setup as a singleton.
 */
public class RemoteFruitsDataStore implements FruitsDataStore
{
    private static RemoteFruitsDataStore INSTANCE;

    @NonNull
    private FruitsApiProvider fruitsApiProvider;

    @VisibleForTesting
    RemoteFruitsDataStore(@NonNull FruitsApiProvider fruitsApiProvider)
    {
        this.fruitsApiProvider = fruitsApiProvider;
    }

    public static RemoteFruitsDataStore getInstance(@NonNull FruitsApiProvider fruitsApiProvider)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RemoteFruitsDataStore(fruitsApiProvider);
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<FruitEntity>> getFruits()
    {
        final FruitsApi fruitsApi = fruitsApiProvider.getFruitsApi();
        return fruitsApi.getFruits();
    }
}
