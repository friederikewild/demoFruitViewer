package me.friederikewild.demo.touchnote.data.datasource.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.Collection;

import me.friederikewild.demo.touchnote.BuildConfig;
import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

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

        // TODO: Something to explore further. Could enhance user experience
//        enableOkHttpCaching(httpClientBuilder);

        final OkHttpClient httpClient = httpClientBuilder.build();

        // Setup retrofitClient builder
        final Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(ItemsApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient);

        // Create retrofitClient
        return retrofitBuilder.build();
    }

    /**
     * Something to experiment with in the future.
     * Needs proper handling of providing {@link Context#getCacheDir} and {@link #isNetworkAvailable}.
     * Both could be wrapped and provided via injections.
     *
     * @param httpClientBuilder The created and not yet used http client builder
     * @param cacheDir          Link to the context cache dir
     */
    @SuppressWarnings("Unused")
    private void enableOkHttpCaching(@NonNull OkHttpClient.Builder httpClientBuilder, @NonNull File cacheDir)
    {
        long maxSize = 10 * 1024 * 1024; // 10 MB
        httpClientBuilder.cache(new Cache(cacheDir, maxSize))
                .addInterceptor(chain ->
                                {
                                    Request request = chain.request();
                                    if (isNetworkAvailable())
                                    {
                                        request = request.newBuilder().header("Cache-Control",
                                                                              "public, max-age=" + 60).build();
                                    }
                                    else
                                    {
                                        request = request.newBuilder().header("Cache-Control",
                                                                              "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                                    }
                                    return chain.proceed(request);
                                });
    }

    @NonNull
    private HttpLoggingInterceptor createHttpLoggingInterceptor()
    {
        // Custom logger to get output on Logcat instead of terminal
        final HttpLoggingInterceptor.Logger logger = message ->
        {
            Timber.tag("RemoteApi");
            Timber.d("OkHttp: %s", message);
        };

        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
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

    private static boolean isNetworkAvailable()
    {
        /*
        public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
                */
        return true;
    }
}
