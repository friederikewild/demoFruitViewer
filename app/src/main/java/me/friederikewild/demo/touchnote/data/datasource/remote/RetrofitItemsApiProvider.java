package me.friederikewild.demo.touchnote.data.datasource.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

import me.friederikewild.demo.touchnote.BuildConfig;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
    public ItemsApi getItemsApi()
    {
        // Create api from interface ready for calls
        return retrofitClient.create(ItemsApi.class);
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
