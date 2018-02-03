package me.friederikewild.demo.touchnote.data.datasource.remote;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.Collection;

import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Concrete remote data store.
 * Setup as a singleton.
 */
public class RemoteItemsDataStore implements ItemsDataStore
{
    private static RemoteItemsDataStore INSTANCE;

    @NonNull
    private Retrofit retrofit;

    @VisibleForTesting
    RemoteItemsDataStore(@NonNull RetrofitProvider retrofitProvider)
    {
        this.retrofit = retrofitProvider.createRetrofitClient();
    }

    public static RemoteItemsDataStore getInstance(@NonNull RetrofitProvider retrofitProvider)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new RemoteItemsDataStore(retrofitProvider);
        }
        return INSTANCE;
    }

    @Override
    public void getItems(@NonNull GetEntityItemsCallback callback,
                         @NonNull GetNoDataCallback errorCallback)
    {
        // Create api call from interface
        ItemsApi api = retrofit.create(ItemsApi.class);
        Call<Collection<ItemEntity>> call = api.getItems();

        call.enqueue(new Callback<Collection<ItemEntity>>()
        {
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
        });
    }
}
