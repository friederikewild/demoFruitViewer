package me.friederikewild.demo.fruits.data.datasource.remote;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.entity.ItemEntity;
import retrofit2.http.GET;

/**
 * Define available api methods.
 * Can use "http://www.mocky.io/v2/" as base for testing specific responses.
 */
public interface FruitsApi
{
    String BASE_URL = "http://www.mocky.io/v2/";

    @GET("5a94869f3500000e009b0ec9")
    Flowable<List<ItemEntity>> getItems();
}
