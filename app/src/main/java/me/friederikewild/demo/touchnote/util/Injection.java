package me.friederikewild.demo.touchnote.util;


import java.io.Serializable;

import me.friederikewild.demo.touchnote.data.ItemsDataRepository;
import me.friederikewild.demo.touchnote.data.ItemsRepository;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.CacheItemDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.CurrentTimeProvider;
import me.friederikewild.demo.touchnote.data.datasource.cache.ItemCache;
import me.friederikewild.demo.touchnote.data.datasource.remote.EmptyRemoteItemsDataProvider;
import me.friederikewild.demo.touchnote.data.datasource.remote.ItemsApiProvider;
import me.friederikewild.demo.touchnote.data.datasource.remote.RemoteItemsDataStore;
import me.friederikewild.demo.touchnote.data.datasource.remote.RetrofitItemsApiProvider;
import me.friederikewild.demo.touchnote.data.entity.mapper.HtmlStringFormatter;
import me.friederikewild.demo.touchnote.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.touchnote.domain.usecase.GetItemUseCase;
import me.friederikewild.demo.touchnote.domain.usecase.GetItemsUseCase;

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
        return new GetItemsUseCase(provideItemsDataRepository(), provideItemEntityDataMapper());
    }

    public static GetItemUseCase provideGetItemUseCase()
    {
        return new GetItemUseCase(provideItemsDataRepository(), provideItemEntityDataMapper());
    }

    public static Bundler<Serializable> provideSerializableBundler()
    {
        return new SerializableBundler();
    }
}
