package me.friederikewild.demo.touchnote.data.entity.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.friederikewild.demo.touchnote.TestMockData;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import me.friederikewild.demo.touchnote.domain.model.Item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link ItemEntityDataMapper}
 *
 * NOTE: Displaying tags and date are not part of the presented data,
 * therefore no extra tests on those special types added for now.
 */
public class ItemEntityDataMapperTest
{
    //    Mapper under test
    private ItemEntityDataMapper mapper;

    @Mock
    private HtmlStringFormatter htmlStringFormatterMock;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        // No special formatting done as default
        when(htmlStringFormatterMock.formatHtml(anyString())).then(returnsFirstArg());

        mapper = new ItemEntityDataMapper(htmlStringFormatterMock);
    }

    @Test
    public void givenTransformFilledItem_ThenModelIdIsSet()
    {
        // Given
        final ItemEntity entity = TestMockData.createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getId(), is(TestMockData.FAKE_ID));
    }

    @Test
    public void givenTransformFilledItem_ThenModelTitleIsSet()
    {
        // Given
        final ItemEntity entity = TestMockData.createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getTitle(), is(TestMockData.FAKE_TITLE));
    }

    @Test
    public void givenTransformFilledItem_ThenModelTitleIsHtmlFormatted()
    {
        // Given
        final ItemEntity entity = TestMockData.createFakeItemEntityWithTitleOnly();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        verify(htmlStringFormatterMock).formatHtml(TestMockData.FAKE_TITLE);
    }

    @Test
    public void givenTransformFilledItem_ThenModelDescriptionIsSet()
    {
        // Given
        final ItemEntity entity = TestMockData.createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getDescription(), is(TestMockData.FAKE_DESCRIPTION));
    }

    @Test
    public void givenTransformFilledItem_ThenModelDescriptionIsHtmlFormatted()
    {
        // Given
        final ItemEntity entity = TestMockData.createFakeItemEntityWithDescriptionOnly();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getDescription(), is(TestMockData.FAKE_DESCRIPTION));
    }

    @Test
    public void givenTransformFilledItem_ThenModelDateIsSet()
    {
        // Given
        final ItemEntity entity = TestMockData.createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getDate(), is(TestMockData.FAKE_DATE));
    }

    @Test
    public void givenTransformFilledItem_ThenModelImageUrlIsFilled()
    {
        // Given
        final ItemEntity entity = TestMockData.createFakeItemEntity();

        // When
        final Item itemModel = mapper.transform(entity);

        // Then
        assertNotNull(itemModel);
        assertThat(itemModel.getImageUrl(), is(TestMockData.FAKE_IMAGE_URL));
    }

    @Test
    public void givenTransformCollection_ThenModelListCreatedOfSameLength()
    {
        // Given
        final Collection<ItemEntity> entities = new ArrayList<>();
        entities.add(TestMockData.createFakeItemEntity());
        entities.add(TestMockData.createFakeItemEntity());

        // When
        final List<Item> items = mapper.transform(entities);

        // Then
        assertThat(items.size(), is(entities.size()));
    }

}
