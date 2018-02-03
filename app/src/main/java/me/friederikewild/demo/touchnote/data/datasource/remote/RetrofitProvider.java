package me.friederikewild.demo.touchnote.data.datasource.remote;

import me.friederikewild.demo.touchnote.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Facade wrapper for retrofitClient usage
 *
 * Setup as a singleton.
 */
public class RetrofitProvider
{
    private static RetrofitProvider INSTANCE;

    private RetrofitProvider()
    {
        // Nothing
    }

    public static RetrofitProvider getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RetrofitProvider();
        }
        return INSTANCE;
    }

    public Retrofit createRetrofitClient()
    {
        final OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG)
        {
            // Setup logging
            final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
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
}
