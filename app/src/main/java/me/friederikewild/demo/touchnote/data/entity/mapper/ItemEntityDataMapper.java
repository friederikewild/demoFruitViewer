package me.friederikewild.demo.touchnote.data.entity.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Mapper to transform between Item in the data layer {@link me.friederikewild.demo.touchnote.data.entity.ItemEntity}
 * to {@link me.friederikewild.demo.touchnote.domain.model.Item} in the domain layer.
 *
 * NOTE: Currently data is read only, therefore no mapping needed in the opposite direction.
 * Setup as a singleton.
 */
public class ItemEntityDataMapper
{
    private static ItemEntityDataMapper INSTANCE;

    @NonNull
    private final HtmlStringFormatter formatter;

    @VisibleForTesting // Alternative provide clearInstance for tests
    public ItemEntityDataMapper(@NonNull HtmlStringFormatter formatter)
    {
        this.formatter = formatter;
    }

    public static ItemEntityDataMapper getInstance(@NonNull HtmlStringFormatter formatter)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ItemEntityDataMapper(formatter);
        }
        return INSTANCE;
    }

    @NonNull
    public Item transform(@NonNull ItemEntity itemEntity)
    {
        // Note: Title and Description can include html that needs transformation
        return new Item(itemEntity.getId(),
                        formatter.formatHtml(itemEntity.getTitle()),
                        formatter.formatHtml(itemEntity.getDescription()),
                        itemEntity.getDate(),
                        itemEntity.getTags(),
                        itemEntity.getImageUrl());
    }
}
