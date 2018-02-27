package me.friederikewild.demo.fruits.data.entity.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import me.friederikewild.demo.fruits.data.entity.FruitEntity;
import me.friederikewild.demo.fruits.domain.model.Fruit;

/**
 * Mapper to transform between Fruit representation in the data layer {@link me.friederikewild.demo.fruits.data.entity.FruitEntity}
 * to {@link me.friederikewild.demo.fruits.domain.model.Fruit} in the domain and presentation layer.
 *
 * NOTE: Currently data is read only, therefore no mapping needed in the opposite direction.
 * Setup as a singleton.
 */
public class FruitEntityDataMapper
{
    private static FruitEntityDataMapper INSTANCE;

    private static final Map<String, String> SOURCE_PROVIDER = ImmutableMap.<String, String>builder()
            .put("Wikipedia", "https://en.wikipedia.org/wiki/")
            .build();

    @NonNull
    private final HtmlStringFormatter formatter;

    @VisibleForTesting // Alternative provide clearInstance for tests
    public FruitEntityDataMapper(@NonNull HtmlStringFormatter formatter)
    {
        this.formatter = formatter;
    }

    public static FruitEntityDataMapper getInstance(@NonNull HtmlStringFormatter formatter)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new FruitEntityDataMapper(formatter);
        }
        return INSTANCE;
    }

    @NonNull
    public Fruit transform(@NonNull FruitEntity fruitEntity)
    {
        // Check if additional source url can be provided from source provider
        final String sourceUrl;
        if (SOURCE_PROVIDER.containsKey(fruitEntity.getSourceProvider()))
        {
            sourceUrl = SOURCE_PROVIDER.get(fruitEntity.getSourceProvider()) + fruitEntity.getTitle();
        }
        else
        {
            sourceUrl = "";
        }

        // TODO: Provide image data. ImageProvider + Id will lead to imageUrl and credits html

        // Note: Title and Description can include html that needs transformation
        return new Fruit(fruitEntity.getId(),
                         formatter.formatHtml(fruitEntity.getTitle()),
                         formatter.formatHtml(fruitEntity.getDescription()),
                         fruitEntity.getDate(),
                         fruitEntity.getTags(),
                         fruitEntity.getSourceProvider(),
                         sourceUrl,
                         fruitEntity.getImageId(), // TODO: Update with real api
                         fruitEntity.getImageProvider() // TODO: Enhance Credits and create html
        );
    }
}
