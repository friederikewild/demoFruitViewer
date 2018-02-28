package me.friederikewild.demo.fruits.data.entity.mapper;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import me.friederikewild.demo.fruits.data.datasource.remote.FruitsApi;
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

    private static final Map<String, String> PROVIDERS = ImmutableMap.<String, String>builder()
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
        final String sourceUrl = createSourceUrl(fruitEntity);
        final String imageUrl = createImageUrl(fruitEntity);
        final String imageProvider = createImageProvider(fruitEntity);

        // Note: Title and Description can include html that needs transformation
        return new Fruit(fruitEntity.getId(),
                         formatter.formatHtml(fruitEntity.getTitle()),
                         formatter.formatHtml(fruitEntity.getDescription()),
                         fruitEntity.getDate(),
                         fruitEntity.getTags(),
                         fruitEntity.getSourceProvider(),
                         sourceUrl,
                         imageUrl,
                         imageProvider
        );
    }

    @NonNull
    private String createSourceUrl(@NonNull FruitEntity fruitEntity)
    {
        // Check if additional source url can be provided from source provider by combining base url plus title
        final String provider = fruitEntity.getSourceProvider();
        return PROVIDERS.containsKey(provider) ? PROVIDERS.get(provider) + fruitEntity.getTitle() : "";
    }

    @NonNull
    private String createImageUrl(FruitEntity fruitEntity)
    {
        // TODO: Update with real api. Provide image data from photo api result object for imageUrl

        final String imageId = fruitEntity.getImageId();
        return !Strings.isNullOrEmpty(imageId) ? String.format(FruitsApi.PHOTO_BASE_URL, imageId) : "";
    }

    @NonNull
    private String createImageProvider(FruitEntity fruitEntity)
    {
        // TODO: Enhance Credits and create html from photo api to allow clickable links
        return fruitEntity.getImageProvider();
    }
}
