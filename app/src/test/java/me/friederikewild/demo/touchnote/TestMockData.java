package me.friederikewild.demo.touchnote;

import com.google.common.collect.Lists;

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


    List<Item> EMPTY_ITEMS = Lists.newArrayList();

    List<Item> ITEMS = Lists.newArrayList(
            new Item(FAKE_ID),
            new Item(FAKE_ID2),
            new Item(FAKE_ID3));

    List<ItemEntity> ENTITY_ITEMS = Lists.newArrayList(
            new ItemEntity(FAKE_ID),
            new ItemEntity(FAKE_ID2),
            new ItemEntity(FAKE_ID3));
}
