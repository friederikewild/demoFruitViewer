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
import me.friederikewild.demo.fruits.data.entity.mapper.FruitEntityDataMapper;
import me.friederikewild.demo.fruits.domain.usecase.GetFruitUseCase;
import me.friederikewild.demo.fruits.domain.usecase.GetFruitsUseCase;
import me.friederikewild.demo.fruits.util.schedulers.BaseSchedulerProvider;
import me.friederikewild.demo.fruits.util.schedulers.RxSchedulerProvider;

/**
 * Simple manual injection helper to allow injection of mock implementations for testing.
 * This way dependencies in Activities can be changed while running tests.
 */
public class Injection
{
    public static FruitsRepository provideFruitsDataRepository()
    {
        return FruitsDataRepository.getInstance(
                provideRemoteFruitsDataStore(),
//                provideEmptyRemoteFruitsDataProvider(),
                provideCacheFruitDataStore());
    }

    public static FruitEntityDataMapper provideFruitEntityDataMapper()
    {
        return FruitEntityDataMapper.getInstance(provideHtmlStringFormatter());
    }

    public static HtmlStringFormatter provideHtmlStringFormatter()
    {
        return HtmlStringFormatter.getInstance();
    }

    public static FruitsDataStore provideRemoteFruitsDataStore()
    {
        return RemoteFruitsDataStore.getInstance(provideFruitsApiProvider());
    }

    /**
     * Alternative to test handling receiving an empty list from remote
     */
    public static FruitsDataStore provideEmptyRemoteFruitsDataProvider()
    {
        return new EmptyRemoteFruitsDataProvider();
    }

    public static FruitsApiProvider provideFruitsApiProvider()
    {
        return RetrofitFruitsApiProvider.getInstance();
    }

    public static FruitCache provideCacheFruitDataStore()
    {
        return CacheFruitDataStore.getInstance(provideCurrentTimeProvider());
    }

    public static CurrentTimeProvider provideCurrentTimeProvider()
    {
        return CurrentTimeProvider.getInstance();
    }

    public static GetFruitsUseCase provideGetFruitsUseCase()
    {
        return new GetFruitsUseCase(provideFruitsDataRepository(),
                                    provideFruitEntityDataMapper(),
                                    provideSchedulerProvider());
    }

    public static GetFruitUseCase provideGetFruitUseCase()
    {
        return new GetFruitUseCase(provideFruitsDataRepository(),
                                   provideFruitEntityDataMapper(),
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
