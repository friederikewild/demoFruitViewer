package me.friederikewild.demo.fruits.data.datasource.remote;

import android.support.annotation.NonNull;

/**
 * Provide creation of a {@link FruitsApi} api.
 * Allowing no knowledge of used library to fetch items.
 */
public interface ItemsApiProvider
{
    @NonNull
    FruitsApi getItemsApi();
}
