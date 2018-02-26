package me.friederikewild.demo.fruits.data.datasource.cache;

import android.support.annotation.NonNull;

import me.friederikewild.demo.fruits.data.datasource.FruitDataStore;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Interface for caching fruits and retrieving them by id.
 * Loading is expected to be quick, therefore no extra callbacks.
 */
public interface FruitCache extends FruitDataStore
{
    /**
     * Save fruit in cache
     *
     * @param fruit The fruit to cache
     */
    void putFruit(@NonNull FruitEntity fruit);

    /**
     * Check if fruit with provided id is cached
     *
     * @param fruitId Id to look up in cache
     * @return {@code true} if cached, otherwise {@code false}
     */
    boolean isCached(final @NonNull String fruitId);

    /**
     * Check if cache is still trustworthy
     *
     * @return Flag if cache is expired
     */
    boolean isExpired();

    /**
     * Clear complete cache e.g. when new data was received.
     */
    void clearAll();
}
