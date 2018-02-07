package me.friederikewild.demo.touchnote.data.datasource;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import io.reactivex.Flowable;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

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
    Flowable<Optional<ItemEntity>> getItem(@NonNull String itemId);
}
