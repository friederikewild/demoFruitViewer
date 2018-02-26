package me.friederikewild.demo.fruits.util;


import java.io.Serializable;

import me.friederikewild.demo.fruits.data.FruitsDataRepository;
import me.friederikewild.demo.fruits.data.FruitsRepository;
import me.friederikewild.demo.fruits.data.datasource.FruitsDataStore;
import me.friederikewild.demo.fruits.data.datasource.cache.CacheFruitDataStore;
import me.friederikewild.demo.fruits.data.datasource.cache.CurrentTimeProvider;
import me.friederikewild.demo.fruits.data.datasource.cache.FruitCache;
import me.friederikewild.demo.fruits.data.datasource.remote.EmptyRemoteFruitsDataProvider;
import me.friederikewild.demo.fruits.data.datasource.remote.FruitsApiProvider;
import me.friederikewild.demo.fruits.data.datasource.remote.RemoteFruitsDataStore;
import me.friederikewild.demo.fruits.data.datasource.remote.RetrofitFruitsApiProvider;
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
    public static FruitsRepository provideItemsDataRepository()
    {
        return FruitsDataRepository.getInstance(
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

    public static FruitsDataStore provideRemoteItemsDataStore()
    {
        return RemoteFruitsDataStore.getInstance(provideItemsApiProvider());
    }

    /**
     * Alternative to test handling receiving an empty list from remote
     */
    public static FruitsDataStore provideEmptyRemoteItemsDataProvider()
    {
        return new EmptyRemoteFruitsDataProvider();
    }

    public static FruitsApiProvider provideItemsApiProvider()
    {
        return RetrofitFruitsApiProvider.getInstance();
    }

    public static FruitCache provideCacheItemDataStore()
    {
        return CacheFruitDataStore.getInstance(provideCurrentTimeProvider());
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
