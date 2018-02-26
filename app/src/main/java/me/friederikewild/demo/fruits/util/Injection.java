package me.friederikewild.demo.fruits.util;


import java.io.Serializable;

import me.friederikewild.demo.fruits.data.ItemsDataRepository;
import me.friederikewild.demo.fruits.data.ItemsRepository;
import me.friederikewild.demo.fruits.data.datasource.ItemsDataStore;
import me.friederikewild.demo.fruits.data.datasource.cache.CacheItemDataStore;
import me.friederikewild.demo.fruits.data.datasource.cache.CurrentTimeProvider;
import me.friederikewild.demo.fruits.data.datasource.cache.ItemCache;
import me.friederikewild.demo.fruits.data.datasource.remote.EmptyRemoteItemsDataProvider;
import me.friederikewild.demo.fruits.data.datasource.remote.ItemsApiProvider;
import me.friederikewild.demo.fruits.data.datasource.remote.RemoteItemsDataStore;
import me.friederikewild.demo.fruits.data.datasource.remote.RetrofitItemsApiProvider;
import me.friederikewild.demo.fruits.data.entity.mapper.HtmlStringFormatter;
import me.friederikewild.demo.fruits.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.fruits.domain.usecase.GetItemUseCase;
import me.friederikewild.demo.fruits.domain.usecase.GetItemsUseCase;
import me.friederikewild.demo.fruits.util.schedulers.BaseSchedulerProvider;
import me.friederikewild.demo.fruits.util.schedulers.RxSchedulerProvider;

/**
 * Simple manual injection helper to allow injection of mock implementations for testing.
 * This way dependencies in Activities can be changed while running tests.
 */
public class Injection
{
    public static ItemsRepository provideItemsDataRepository()
    {
        return ItemsDataRepository.getInstance(
                provideRemoteItemsDataStore(),
//                provideEmptyRemoteItemsDataProvider(),
                provideCacheItemDataStore());
    }

    public static ItemEntityDataMapper provideItemEntityDataMapper()
    {
        return ItemEntityDataMapper.getInstance(provideHtmlStringFormatter());
    }

    public static HtmlStringFormatter provideHtmlStringFormatter()
    {
        return HtmlStringFormatter.getInstance();
    }

    public static ItemsDataStore provideRemoteItemsDataStore()
    {
        return RemoteItemsDataStore.getInstance(provideItemsApiProvider());
    }

    /**
     * Alternative to test handling receiving an empty list from remote
     */
    public static ItemsDataStore provideEmptyRemoteItemsDataProvider()
    {
        return new EmptyRemoteItemsDataProvider();
    }

    public static ItemsApiProvider provideItemsApiProvider()
    {
        return RetrofitItemsApiProvider.getInstance();
    }

    public static ItemCache provideCacheItemDataStore()
    {
        return CacheItemDataStore.getInstance(provideCurrentTimeProvider());
    }

    public static CurrentTimeProvider provideCurrentTimeProvider()
    {
        return CurrentTimeProvider.getInstance();
    }

    public static GetItemsUseCase provideGetItemsUseCase()
    {
        return new GetItemsUseCase(provideItemsDataRepository(),
                                   provideItemEntityDataMapper(),
                                   provideSchedulerProvider());
    }

    public static GetItemUseCase provideGetItemUseCase()
    {
        return new GetItemUseCase(provideItemsDataRepository(),
                                  provideItemEntityDataMapper(),
                                  provideSchedulerProvider());
    }

    public static BaseSchedulerProvider provideSchedulerProvider()
    {
        return RxSchedulerProvider.getInstance();
    }

    public static Bundler<Serializable> provideSerializableBundler()
    {
        return new SerializableBundler();
    }
}
