package me.friederikewild.demo.fruits.data.datasource.cache;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.base.Optional;

import java.util.WeakHashMap;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Concrete data access cashed in memory for quick ui updates.
 * Elements are saved in a {@code WeakHashMap} to allow garbage collection when needed.
 * Setup as a singleton.
 */
public class CacheFruitDataStore implements FruitCache
{
    private static CacheFruitDataStore INSTANCE;

    @NonNull
    private final CurrentTimeProvider timeProvider;

    /**
     * Keep cache for 10 minutes
     */
    static final long EXPIRATION_TIME = 10L * 60L * 1000L;

    private final WeakHashMap<String, FruitEntity> fruitCache;
    private long lastUpdatedTimeMillis = 0;

    // Prevent direct instantiation, but allow it from tests to inject mocks
    @VisibleForTesting
    CacheFruitDataStore(@NonNull CurrentTimeProvider timeProvider)
    {
        fruitCache = new WeakHashMap<>(20);
        this.timeProvider = timeProvider;
    }

    public static CacheFruitDataStore getInstance(@NonNull CurrentTimeProvider timeProvider)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CacheFruitDataStore(timeProvider);
        }
        return INSTANCE;
    }

    @SuppressWarnings("Guava")
    @Override
    public Flowable<Optional<FruitEntity>> getFruit(@NonNull String fruitId)
    {
        if (!isExpired() && isCached(fruitId))
        {
            return Flowable.just(Optional.of(getFruitFromCache(fruitId)));
        }
        else
        {
            return Flowable.just(Optional.absent());
        }
    }

    private FruitEntity getFruitFromCache(@NonNull String fruitId)
    {
        return fruitCache.get(fruitId);
    }

    @Override
    public void putFruit(@NonNull FruitEntity fruit)
    {
        fruitCache.put(fruit.getId(), fruit);
        saveNowAsLastCacheUpdate();
    }

    private void saveNowAsLastCacheUpdate()
    {
        lastUpdatedTimeMillis = timeProvider.getCurrentTimeMillis();
    }

    @Override
    public boolean isCached(@NonNull String fruitId)
    {
        return fruitCache.containsKey(fruitId);
    }

    @Override
    public boolean isExpired()
    {
        long currentTimeMillis = timeProvider.getCurrentTimeMillis();

        boolean isExpired = ((currentTimeMillis - lastUpdatedTimeMillis) > EXPIRATION_TIME);

        if (isExpired)
        {
            clearAll();
        }

        return isExpired;
    }

    @Override
    public void clearAll()
    {
        fruitCache.clear();
    }
}
