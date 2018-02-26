package me.friederikewild.demo.fruits.data.datasource.remote;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.datasource.ItemsDataStore;
import me.friederikewild.demo.fruits.data.entity.ItemEntity;

/**
 * Alternative api provider to test empty return handling.
 */
@VisibleForTesting
public class EmptyRemoteItemsDataProvider implements ItemsDataStore
{
    private static final List<ItemEntity> FAKE_DATA = new ArrayList<>();

    @Override
    public Flowable<List<ItemEntity>> getItems()
    {
        return Flowable.fromIterable(FAKE_DATA).toList().toFlowable();
    }
}
