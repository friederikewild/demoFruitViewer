package me.friederikewild.demo.fruits.data.datasource;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Define data layer access to single item.
 */
public interface ItemDataStore
{
    /**
     * Request concrete item for specific id and get it via provided callback
     *
     * @param itemId Id to look up in cache
     */
    Flowable<Optional<FruitEntity>> getItem(@NonNull String itemId);
}
