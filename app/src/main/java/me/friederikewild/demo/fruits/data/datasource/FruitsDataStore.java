package me.friederikewild.demo.fruits.data.datasource;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

/**
 * Define data layer access to fruits as collection
 */
public interface FruitsDataStore
{
    /**
     * Request list of fruit entities from cache or remote.
     */
    Flowable<List<FruitEntity>> getFruits();
}
