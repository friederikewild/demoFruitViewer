package me.friederikewild.demo.fruits.data;

import me.friederikewild.demo.fruits.data.datasource.FruitDataStore;
import me.friederikewild.demo.fruits.data.datasource.FruitsDataStore;

/**
 * Define domain data access as interface for the repository.
 * Loading/Fruit access has simple callbacks to inform about loaded data / errors since calls are done asynchronously.
 */
public interface FruitsRepository extends FruitsDataStore, FruitDataStore
{
    /**
     * Request a fresh set of data
     */
    void refreshData();
}
