package me.friederikewild.demo.fruits.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.datasource.FruitsDataStore;
import me.friederikewild.demo.fruits.data.datasource.cache.FruitCache;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Concrete repository getting data from remote or cache.
 * For simplicity, the synchronisation between locally cached and
 * remote data is kept to a minimum. When remote data is received the cache will be overwritten,
 * When a fruit is requested it will be used from cache if available.
 * Setup as a singleton.
 */
public class FruitsDataRepository implements FruitsRepository
{
    private static FruitsDataRepository INSTANCE;

    private final FruitsDataStore remoteFruitsStore;
    private final FruitCache cacheFruitStore;

    // Prevent direct instantiation, but allow it from tests to inject mocks
    @VisibleForTesting
    FruitsDataRepository(
            @NonNull final FruitsDataStore remote,
            @NonNull final FruitCache cache)
    {
        this.remoteFruitsStore = remote;
        this.cacheFruitStore = cache;
    }

    public static FruitsDataRepository getInstance(@NonNull final FruitsDataStore remoteFruitsStore,
                                                   @NonNull final FruitCache cacheFruitDataStore)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new FruitsDataRepository(
                    remoteFruitsStore,
                    cacheFruitDataStore);
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<FruitEntity>> getFruits()
    {
        return getFruitsFromRemoteAndCache()
                .firstOrError()
                .toFlowable();
    }

    @SuppressWarnings("Convert2MethodRef")
    private Flowable<List<FruitEntity>> getFruitsFromRemoteAndCache()
    {
        return remoteFruitsStore.getFruits()
                .flatMap(tasks -> Flowable.fromIterable(tasks)
                        .doOnNext(fruit -> cacheFruitStore.putFruit(fruit))
                        .toList()
                        .toFlowable());
    }

    @Override
    public Flowable<Optional<FruitEntity>> getFruit(@NonNull String fruitId)
    {
        final Flowable<Optional<FruitEntity>> cachedFruit = cacheFruitStore.getFruit(fruitId);
        final Flowable<Optional<FruitEntity>> remoteFruit = getFruitFromRemoteDataStore(fruitId);

        // Request cache first, if not available, request remote
        return Flowable.concat(cachedFruit, remoteFruit)
                .firstElement()
                .toFlowable();
    }

    @Override
    public void refreshData()
    {
        cacheFruitStore.clearAll();
    }

    /**
     * Fallback method to catch all fruits from remote to then pick fruit.
     *
     * @param fruitId Id to look up in cache
     */
    @SuppressWarnings({"Convert2MethodRef", "Guava"})
    private Flowable<Optional<FruitEntity>> getFruitFromRemoteDataStore(@NonNull String fruitId)
    {
        return getFruitsFromRemoteAndCache()
                .flatMap(fruits -> Flowable.fromIterable(fruits))
                .filter(fruitEntity -> fruitEntity.getId().equals(fruitId))
                .map(fruitEntity -> Optional.of(fruitEntity))
                .firstOrError()
                .toFlowable();
    }
}
