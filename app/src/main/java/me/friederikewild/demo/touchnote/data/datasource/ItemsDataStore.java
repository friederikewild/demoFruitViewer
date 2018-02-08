package me.friederikewild.demo.touchnote.data.datasource;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

/**
 * Define data layer access to items as collection
 */
public interface ItemsDataStore
{
    /**
     * Request list of item entities from cache or remote.
     */
    Flowable<List<ItemEntity>> getItems();
}
