package me.friederikewild.demo.fruits.data.datasource.remote;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.entity.ItemEntity;
import retrofit2.http.GET;

/**
 * Define available api methods.
 * Can use "http://www.mocky.io/v2/" as base for testing specific responses.
 */
public interface ItemsApi
{
    String BASE_URL = "http://www.mocky.io/v2/";

    @GET("57ee2ca8260000f80e1110fa")
    Flowable<List<ItemEntity>> getItems();
}
