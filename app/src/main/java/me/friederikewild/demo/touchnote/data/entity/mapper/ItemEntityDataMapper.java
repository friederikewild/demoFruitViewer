package me.friederikewild.demo.touchnote.data.entity.mapper;

import android.support.annotation.NonNull;

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
    @NonNull
    public Item transform(@NonNull ItemEntity itemEntity)
    {
        return new Item(itemEntity.getId(),
                        itemEntity.getTitle(),
                        itemEntity.getDescription(),
                        itemEntity.getDate(),
                        itemEntity.getTags(),
                        itemEntity.getImageUrl());
    }

    @NonNull
    public List<Item> transform(@NonNull Collection<ItemEntity> itemEntities)
    {
        final List<Item> items = new ArrayList<>(10);

        for (ItemEntity entity : itemEntities)
        {
            if (entity != null)
            {
                final Item item = transform(entity);
                items.add(item);
            }
        }

        return items;
    }
}
