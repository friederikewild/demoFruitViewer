package me.friederikewild.demo.fruits.data.datasource.remote;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.datasource.FruitsDataStore;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Alternative api provider to test empty return handling.
 */
@VisibleForTesting
public class EmptyRemoteFruitsDataProvider implements FruitsDataStore
{
    private static final List<FruitEntity> FAKE_DATA = new ArrayList<>();

    @Override
    public Flowable<List<FruitEntity>> getFruits()
    {
        return Flowable.fromIterable(FAKE_DATA).toList().toFlowable();
    }
}
