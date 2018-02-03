package me.friederikewild.demo.touchnote.data.datasource.remote;

import android.support.annotation.NonNull;

import java.util.Collection;

import me.friederikewild.demo.touchnote.BuildConfig;
import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Facade wrapper for retrofitClient as concrete implementation of {@link ItemsApiProvider}
 * Call {@link #enqueueGetItems}
 *
 * Setup as a singleton.
 */
public class RetrofitItemsApiProvider implements ItemsApiProvider
{
    private static RetrofitItemsApiProvider INSTANCE;

    @NonNull
    private Retrofit retrofitClient;

    private RetrofitItemsApiProvider()
    {
        retrofitClient = createRetrofitClient();
    }

    public static RetrofitItemsApiProvider getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RetrofitItemsApiProvider();
        }
        return INSTANCE;
    }

    private Retrofit createRetrofitClient()
    {
        final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG)
        {
            // Setup logging
            final HttpLoggingInterceptor loggingInterceptor = createHttpLoggingInterceptor();
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }

        final OkHttpClient httpClient = httpClientBuilder.build();

        // Setup retrofitClient builder
        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(ItemsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient);

        // Create retrofitClient
        return retrofitBuilder.build();
    }

    @NonNull
    private HttpLoggingInterceptor createHttpLoggingInterceptor()
    {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @NonNull
    private ItemsApi getItemsApi()
    {
        // Create api from interface ready for calls
        return retrofitClient.create(ItemsApi.class);
    }

    @Override
    public void enqueueGetItems(@NonNull final ItemsDataStore.GetEntityItemsCallback callback,
                                @NonNull final GetNoDataCallback errorCallback)
    {
        final ItemsApi itemsApi = getItemsApi();
        final Call<Collection<ItemEntity>> call = itemsApi.getItems();

        // Executed call asynchronously
        call.enqueue(new ItemsCollectionCallback(callback, errorCallback));
    }

    /**
     * Callback for async request to {@link #enqueueGetItems}.
     * One and only one method will be invoked in response to a given request.
     */
    private static final class ItemsCollectionCallback implements Callback<Collection<ItemEntity>>
    {
        @NonNull
        private final ItemsDataStore.GetEntityItemsCallback callback;
        @NonNull
        private final GetNoDataCallback errorCallback;

        ItemsCollectionCallback(@NonNull final ItemsDataStore.GetEntityItemsCallback callback,
                                @NonNull final GetNoDataCallback errorCallback)
        {
            this.callback = callback;
            this.errorCallback = errorCallback;
        }

        @Override
        public void onResponse(@NonNull Call<Collection<ItemEntity>> call,
                               @NonNull Response<Collection<ItemEntity>> response)
        {
            final Collection<ItemEntity> responseBody = response.body();
            if (responseBody != null)
            {
                callback.onItemsLoaded(responseBody);
            }
            else
            {
                errorCallback.onNoDataAvailable();
            }
        }

        @Override
        public void onFailure(@NonNull Call<Collection<ItemEntity>> call, @NonNull Throwable t)
        {
            errorCallback.onNoDataAvailable();
        }
    }
}
