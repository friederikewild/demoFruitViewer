package me.friederikewild.demo.touchnote;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import me.friederikewild.demo.touchnote.domain.model.Item;

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

    List<Item> EMPTY_ITEMS = new ArrayList<>();

    List<Item> ITEMS = Lists.newArrayList(
            new Item(FAKE_ID),
            new Item(FAKE_ID2),
            new Item(FAKE_ID3));

    List<ItemEntity> ENTITY_ITEMS = Lists.newArrayList(
            new ItemEntity(FAKE_ID),
            new ItemEntity(FAKE_ID2),
            new ItemEntity(FAKE_ID3));

    Item ITEM = new Item(FAKE_ID);

    static ItemEntity createFakeItemEntityWithTitleOnly()
    {
        return createFakeItemEntity(
                "",
                TestMockData.FAKE_TITLE,
                "",
                "",
                new ArrayList<>(),
                "");
    }

    static ItemEntity createFakeItemEntityWithDescriptionOnly()
    {
        return createFakeItemEntity(
                "",
                "",
                TestMockData.FAKE_DESCRIPTION,
                "",
                new ArrayList<>(),
                "");
    }

    static ItemEntity createFakeItemEntity()
    {
        return createFakeItemEntity(
                TestMockData.FAKE_ID,
                TestMockData.FAKE_TITLE,
                TestMockData.FAKE_DESCRIPTION,
                TestMockData.FAKE_DATE,
                new ArrayList<>(),
                TestMockData.FAKE_IMAGE_URL);
    }

    static ItemEntity createFakeItemEntity(@NonNull String id, @NonNull String title,
                                           @NonNull String description, @NonNull String date,
                                           @NonNull List<String> tags, @NonNull String imageUrl)
    {
        ItemEntity entity = new ItemEntity();
        entity.setId(id);
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setDate(date);
        entity.setTags(tags);
        entity.setImageUrl(imageUrl);
        return entity;
    }
}
