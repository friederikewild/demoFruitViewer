package me.friederikewild.demo.fruits.data.datasource;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Define data layer access to single fruit.
 */
public interface FruitDataStore
{
    /**
     * Request concrete fruit for specific id and get it via provided callback
     *
     * @param fruitId Id to look up in cache
     */
    Flowable<Optional<FruitEntity>> getFruit(@NonNull String fruitId);
}
