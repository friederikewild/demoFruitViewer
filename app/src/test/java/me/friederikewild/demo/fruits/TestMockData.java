package me.friederikewild.demo.fruits;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import me.friederikewild.demo.fruits.data.entity.FruitEntity;
import me.friederikewild.demo.fruits.domain.model.Fruit;

/**
 * Collection of test data and collections for unit tests.
 */
public interface TestMockData
{
    String FAKE_ID = "123";
    String FAKE_ID2 = "456";
    String FAKE_ID3 = "789";

    String FAKE_TITLE = "Title";
    String FAKE_DESCRIPTION = "Description";
    String FAKE_DATE = "2016-03-20T00:00:00.000Z";
    String FAKE_IMAGE_URL = "Image";
    String FAKE_IMAGE_PROVIDER = "Provider";

    List<Fruit> EMPTY_FRUITS = new ArrayList<>();

    List<Fruit> FRUITS = Lists.newArrayList(
            new Fruit(FAKE_ID),
            new Fruit(FAKE_ID2),
            new Fruit(FAKE_ID3));

    List<FruitEntity> ENTITY_FRUITS = Lists.newArrayList(
            new FruitEntity(FAKE_ID),
            new FruitEntity(FAKE_ID2),
            new FruitEntity(FAKE_ID3));

    Fruit FRUIT = new Fruit(FAKE_ID);

    static FruitEntity createFakeFruitEntityWithTitleOnly()
    {
        return createFakeFruitEntity(
                "",
                TestMockData.FAKE_TITLE,
                "",
                "",
                new ArrayList<>(),
                "");
    }

    static FruitEntity createFakeFruitEntityWithDescriptionOnly()
    {
        return createFakeFruitEntity(
                "",
                "",
                TestMockData.FAKE_DESCRIPTION,
                "",
                new ArrayList<>(),
                "");
    }

    static FruitEntity createFakeFruitEntity()
    {
        return createFakeFruitEntity(
                TestMockData.FAKE_ID,
                TestMockData.FAKE_TITLE,
                TestMockData.FAKE_DESCRIPTION,
                TestMockData.FAKE_DATE,
                new ArrayList<>(),
                TestMockData.FAKE_IMAGE_PROVIDER);
    }

    static FruitEntity createFakeFruitEntity(@NonNull String id, @NonNull String title,
                                             @NonNull String description, @NonNull String date,
                                             @NonNull List<String> tags, @NonNull String imageProvider)
    {
        FruitEntity fruitEntity = new FruitEntity();
        fruitEntity.setId(id);
        fruitEntity.setTitle(title);
        fruitEntity.setDescription(description);
        fruitEntity.setDate(date);
        fruitEntity.setTags(tags);
        fruitEntity.setImageProvider(imageProvider);
        return fruitEntity;
    }
}
