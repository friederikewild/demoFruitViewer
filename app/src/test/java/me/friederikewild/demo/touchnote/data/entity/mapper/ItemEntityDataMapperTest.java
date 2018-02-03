package me.friederikewild.demo.touchnote.data.entity.mapper;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import me.friederikewild.demo.touchnote.domain.model.Item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the implementation of {@link ItemEntityDataMapper}
 *
 * NOTE: Displaying tags and date are not part of the presented data,
 * therefore no extra tests on those special types added for now.
 */
public class ItemEntityDataMapperTest
{
    private static final String FAKE_ID = "123";
    private static final String FAKE_TITLE = "Title";
    private static final String FAKE_DESCRIPTION = "Description";
    private static final String FAKE_DATE = "2016-03-20T00:00:00.000Z";
    private static final String FAKE_IMAGE_URL = "Image";

    //    Mapper under test
    private ItemEntityDataMapper mapper;

    @Before
    public void setupDataMapper()
    {
        mapper = new ItemEntityDataMapper();
    }

    @Test
    public void givenTransformFilledItem_ThenModelIdIsSet()
    {
        // Given
        final ItemEntity entity = createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getId(), is(FAKE_ID));
    }

    @Test
    public void givenTransformFilledItem_ThenModelTitleIsSet()
    {
        // Given
        final ItemEntity entity = createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getTitle(), is(FAKE_TITLE));
    }

    @Test
    public void givenTransformFilledItem_ThenModelDescriptionIsSet()
    {
        // Given
        final ItemEntity entity = createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getDescription(), is(FAKE_DESCRIPTION));
    }

    @Test
    public void givenTransformFilledItem_ThenModelDateIsSet()
    {
        // Given
        final ItemEntity entity = createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getDate(), is(FAKE_DATE));
    }

    @Test
    public void givenTransformFilledItem_ThenModelImageUrlIsFilled()
    {
        // Given
        final ItemEntity entity = createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getImageUrl(), is(FAKE_IMAGE_URL));
    }

    @Test
    public void givenTransformCollection_ThenModelListCreatedOfSameLength()
    {
        // Given
        final Collection<ItemEntity> entities = new ArrayList<>();
        entities.add(createFakeItemEntity());
        entities.add(createFakeItemEntity());

        // When
        final List<Item> items = mapper.transform(entities);

        // Then
        assertThat(items.size(), is(entities.size()));
    }

    private ItemEntity createFakeItemEntity()
    {
        ItemEntity entity = new ItemEntity();
        entity.setId(FAKE_ID);
        entity.setTitle(FAKE_TITLE);
        entity.setDescription(FAKE_DESCRIPTION);
        entity.setDate(FAKE_DATE);
        entity.setTags(new ArrayList<>());
        entity.setImageUrl(FAKE_IMAGE_URL);
        return entity;
    }
}
