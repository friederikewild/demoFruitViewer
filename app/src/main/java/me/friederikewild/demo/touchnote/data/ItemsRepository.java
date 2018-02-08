package me.friederikewild.demo.touchnote.data;

import me.friederikewild.demo.touchnote.data.datasource.ItemDataStore;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;

/**
 * Define domain data access as interface for the repository.
 * Loading/Item access has simple callbacks to inform about loaded data / errors since calls are done asynchronously.
 */
public interface ItemsRepository extends ItemsDataStore, ItemDataStore
{
    /**
     * Request a fresh set of data
     */
    void refreshData();
}
