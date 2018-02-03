package me.friederikewild.demo.touchnote.util;

import me.friederikewild.demo.touchnote.data.ItemsDataRepository;
import me.friederikewild.demo.touchnote.data.datasource.remote.RemoteItemsDataStore;
import me.friederikewild.demo.touchnote.data.datasource.remote.RetrofitProvider;
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
        return ItemsDataRepository.getInstance(provideItemEntityDataMapper(), provideRemoteItemsDataStore());
    }

    public static ItemEntityDataMapper provideItemEntityDataMapper()
    {
        return new ItemEntityDataMapper();
    }

    public static RetrofitProvider provideItemsApiProvider()
    {
        return RetrofitProvider.getInstance();
    }

    public static RemoteItemsDataStore provideRemoteItemsDataStore()
    {
        return RemoteItemsDataStore.getInstance(provideItemsApiProvider());
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
