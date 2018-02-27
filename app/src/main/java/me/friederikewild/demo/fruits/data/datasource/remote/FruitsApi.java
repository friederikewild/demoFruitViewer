package me.friederikewild.demo.fruits.data.datasource.remote;

import java.util.List;

import io.reactivex.Flowable;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;
import retrofit2.http.GET;

/**
 * Define available api methods.
 * Can use "http://www.mocky.io/v2/" as base for testing specific responses.
 */
public interface FruitsApi
{
    String BASE_URL = "https://friederikewild.github.io/demoFruitViewer/api/";

    @GET("fruits.json")
    Flowable<List<FruitEntity>> getFruits();
}
