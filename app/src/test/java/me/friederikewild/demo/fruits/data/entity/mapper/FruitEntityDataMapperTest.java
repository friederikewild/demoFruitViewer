package me.friederikewild.demo.fruits.data.entity.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import me.friederikewild.demo.fruits.TestMockData;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;
import me.friederikewild.demo.fruits.domain.model.Fruit;

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
public class FruitEntityDataMapperTest
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
        final FruitEntity entity = TestMockData.createFakeFruitEntity();

        // When
        final Fruit fruitModel = mapper.transform(entity);

        // Then
        assertNotNull(fruitModel);
        assertThat(fruitModel.getId(), is(TestMockData.FAKE_ID));
    }

    @Test
    public void givenTransformFilledItem_ThenModelTitleIsSet()
    {
        // Given
        final FruitEntity entity = TestMockData.createFakeFruitEntity();

        // When
        final Fruit fruitModel = mapper.transform(entity);

        // Then
        assertNotNull(fruitModel);
        assertThat(fruitModel.getTitle(), is(TestMockData.FAKE_TITLE));
    }

    @Test
    public void givenTransformFilledItem_ThenModelTitleIsHtmlFormatted()
    {
        // Given
        final FruitEntity entity = TestMockData.createFakeFruitEntityWithTitleOnly();

        // When
        final Fruit fruitModel = mapper.transform(entity);

        // Then
        verify(htmlStringFormatterMock).formatHtml(TestMockData.FAKE_TITLE);
    }

    @Test
    public void givenTransformFilledItem_ThenModelDescriptionIsSet()
    {
        // Given
        final FruitEntity entity = TestMockData.createFakeFruitEntity();

        // When
        final Fruit fruitModel = mapper.transform(entity);

        // Then
        assertNotNull(fruitModel);
        assertThat(fruitModel.getDescription(), is(TestMockData.FAKE_DESCRIPTION));
    }

    @Test
    public void givenTransformFilledItem_ThenModelDescriptionIsHtmlFormatted()
    {
        // Given
        final FruitEntity entity = TestMockData.createFakeFruitEntityWithDescriptionOnly();

        // When
        final Fruit fruitModel = mapper.transform(entity);

        // Then
        assertNotNull(fruitModel);
        assertThat(fruitModel.getDescription(), is(TestMockData.FAKE_DESCRIPTION));
    }

    @Test
    public void givenTransformFilledItem_ThenModelDateIsSet()
    {
        // Given
        final FruitEntity entity = TestMockData.createFakeFruitEntity();

        // When
        final Fruit fruitModel = mapper.transform(entity);

        // Then
        assertNotNull(fruitModel);
        assertThat(fruitModel.getDate(), is(TestMockData.FAKE_DATE));
    }

    @Test
    public void givenTransformFilledItem_ThenModelImageUrlIsFilled()
    {
        // Given
        final FruitEntity entity = TestMockData.createFakeFruitEntity();

        // When
        final Fruit fruitModel = mapper.transform(entity);

        // Then
        assertNotNull(fruitModel);
        assertThat(fruitModel.getImageUrl(), is(TestMockData.FAKE_IMAGE_URL));
    }
}
