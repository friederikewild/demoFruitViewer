package me.friederikewild.demo.touchnote.util;


import me.friederikewild.demo.touchnote.data.ItemsDataRepository;
import me.friederikewild.demo.touchnote.data.datasource.cache.CacheItemDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.CurrentTimeProvider;
import me.friederikewild.demo.touchnote.data.datasource.remote.EmptyItemsApiProvider;
import me.friederikewild.demo.touchnote.data.datasource.remote.ItemsApiProvider;
import me.friederikewild.demo.touchnote.data.datasource.remote.RemoteItemsDataStore;
import me.friederikewild.demo.touchnote.data.datasource.remote.RetrofitItemsApiProvider;
import me.friederikewild.demo.touchnote.data.entity.mapper.HtmlStringFormatter;
import me.friederikewild.demo.touchnote.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.touchnote.domain.usecase.GetItemsUseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCaseHandler;

/**
 * Simple manual injection helper to allow injection of mock implementations for testing.
 * This way dependencies in Activities can be changed while running tests.
 */
public class Injection
{
    public static ItemsDataRepository provideItemsDataRepository()
    {
        return ItemsDataRepository.getInstance(provideItemEntityDataMapper(),
                                               provideRemoteItemsDataStore(),
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

    public static RemoteItemsDataStore provideRemoteItemsDataStore()
    {
        return RemoteItemsDataStore.getInstance(provideItemsApiProvider());
    }

    public static ItemsApiProvider provideItemsApiProvider()
    {
        return RetrofitItemsApiProvider.getInstance();
    }

    /**
     * Alternative to test handling receiving an empty list from remote
     */
    public static ItemsApiProvider provideEmptyItemsApiProvider()
    {
        return new EmptyItemsApiProvider();
    }

    public static CacheItemDataStore provideCacheItemDataStore()
    {
        return CacheItemDataStore.getInstance(provideCurrentTimeProvider());
    }

    public static CurrentTimeProvider provideCurrentTimeProvider()
    {
        return CurrentTimeProvider.getInstance();
    }

    public static UseCaseHandler provideUseCaseHandler()
    {
        return UseCaseHandler.getInstance();
    }

    public static GetItemsUseCase provideGetItemsUseCase()
    {
        return new GetItemsUseCase(provideItemsDataRepository());
    }
}
