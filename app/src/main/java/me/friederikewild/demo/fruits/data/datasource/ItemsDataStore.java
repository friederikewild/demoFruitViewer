package me.friederikewild.demo.fruits.data.datasource;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Define data layer access to items as collection
 */
public interface ItemsDataStore
{
    /**
     * Request list of item entities from cache or remote.
     */
    Flowable<List<FruitEntity>> getItems();
}
