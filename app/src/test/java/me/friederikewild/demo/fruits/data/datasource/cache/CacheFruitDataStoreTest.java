package me.friederikewild.demo.fruits.data.datasource.cache;

import com.google.common.base.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.subscribers.TestSubscriber;
import me.friederikewild.demo.fruits.TestMockData;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Test {@link CacheFruitDataStore}
 */
@SuppressWarnings("Guava")
public class CacheFruitDataStoreTest
{
    private static final long NOW = 1000;

    // Cache data store under test
    private CacheFruitDataStore cacheItemDataStore;

    private TestSubscriber<Optional<FruitEntity>> testSubscriber;

    @Mock
    private CurrentTimeProvider currentTimeProviderMock;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        // Default is to always return the same time millis as if no time passes
        when(currentTimeProviderMock.getCurrentTimeMillis()).thenReturn(NOW);

        testSubscriber = new TestSubscriber<>();

        cacheItemDataStore = new CacheFruitDataStore(currentTimeProviderMock);
    }

    @Test
    public void givenEmptyCache_ThenItemNotCached()
    {
        // When
        final boolean isCached = cacheItemDataStore.isCached(TestMockData.FAKE_ID);

        // Then
        assertThat(isCached, is(false));
    }

    @Test
    public void givenPutItemToCache_ThenItemIsCached()
    {
        // Given
        final FruitEntity entity = createFakeItemEntity();

        // When
        cacheItemDataStore.putFruit(entity);
        final boolean isCached = cacheItemDataStore.isCached(TestMockData.FAKE_ID);

        // Then
        assertThat(isCached, is(true));
    }

    @Test
    public void givenEmptyCache_ThenNoDataCallbackOnRequest()
    {
        // When
        cacheItemDataStore.getFruit(TestMockData.FAKE_ID).subscribe(testSubscriber);

        // Then
        testSubscriber.assertValue(Optional.absent());
    }

    @Test
    public void givenPutItemToCache_ThenItemIsEqualFromCache()
    {
        // Given
        final FruitEntity entity = createFakeItemEntity();
        cacheItemDataStore.putFruit(entity);

        // When
        cacheItemDataStore.getFruit(TestMockData.FAKE_ID).subscribe(testSubscriber);

        // Then
        final Optional<FruitEntity> expectedResult = Optional.of(entity);
        testSubscriber.assertValue(expectedResult);
    }

    @Test
    public void givenCheckExpiredImmediatelyAfterCache_ThenCacheNotExpired()
    {
        // Given
        cacheItemDataStore.putFruit(createFakeItemEntity());

        // When
        boolean isExpired = cacheItemDataStore.isExpired();

        // Then
        assertThat(isExpired, is(false));
    }

    @Test
    public void givenCheckExpiredAfterExpiration_ThenCacheExpired()
    {
        // Given
        cacheItemDataStore.putFruit(createFakeItemEntity());

        // When
        updateCurrentTime(CacheFruitDataStore.EXPIRATION_TIME + 1);
        boolean isExpired = cacheItemDataStore.isExpired();

        // Then
        assertThat(isExpired, is(true));
    }

    @Test
    public void givenCheckExpiredAfterExpiration_ThenItemNoLongerCached()
    {
        // Given
        cacheItemDataStore.putFruit(createFakeItemEntity());

        // When
        updateCurrentTime(CacheFruitDataStore.EXPIRATION_TIME + 1);
        cacheItemDataStore.isExpired();
        final boolean isCached = cacheItemDataStore.isCached(TestMockData.FAKE_ID);

        // Then
        assertThat(isCached, is(false));
    }


    private FruitEntity createFakeItemEntity()
    {
        FruitEntity entity = new FruitEntity();
        entity.setId(TestMockData.FAKE_ID);
        return entity;
    }

    private void updateCurrentTime(long addTime)
    {
        // Move time on by adding the provided time offset
        when(currentTimeProviderMock.getCurrentTimeMillis()).thenReturn(NOW + addTime);
    }
}
