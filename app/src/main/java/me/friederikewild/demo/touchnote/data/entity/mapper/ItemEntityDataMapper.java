package me.friederikewild.demo.touchnote.data.entity.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Mapper to transform between Item in the data layer {@link me.friederikewild.demo.touchnote.data.entity.ItemEntity}
 * to {@link me.friederikewild.demo.touchnote.domain.model.Item} in the domain layer.
 *
 * NOTE: Currently data is read only, therefore no mapping needed in the opposite direction.
 */
public class ItemEntityDataMapper
{
    @Nullable
    public Item transform(@Nullable ItemEntity itemEntity)
    {
        Item item = null;
        if (itemEntity != null)
        {
            item = new Item(itemEntity.getId(),
                            itemEntity.getTitle(),
                            itemEntity.getDescription(),
                            itemEntity.getDate(),
                            itemEntity.getTags(),
                            itemEntity.getImageUrl());
        }
        return item;
    }

    @NonNull
    public List<Item> transform(@NonNull Collection<ItemEntity> itemEntities)
    {
        final List<Item> items = new ArrayList<>(10);

        for (ItemEntity entity : itemEntities)
        {
            final Item item = transform(entity);
            if (item != null)
            {
                items.add(item);
            }
        }

        return items;
    }
}
