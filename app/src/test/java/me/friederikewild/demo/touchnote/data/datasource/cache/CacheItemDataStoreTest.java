package me.friederikewild.demo.touchnote.data.datasource.cache;

import com.google.common.base.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.subscribers.TestSubscriber;
import me.friederikewild.demo.touchnote.TestMockData;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Test {@link CacheItemDataStore}
 */
@SuppressWarnings("Guava")
public class CacheItemDataStoreTest
{
    private static final long NOW = 1000;

    // Cache data store under test
    private CacheItemDataStore cacheItemDataStore;

    private TestSubscriber<Optional<ItemEntity>> testSubscriber;

    @Mock
    private CurrentTimeProvider currentTimeProviderMock;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        // Default is to always return the same time millis as if no time passes
        when(currentTimeProviderMock.getCurrentTimeMillis()).thenReturn(NOW);

        testSubscriber = new TestSubscriber<>();

        cacheItemDataStore = new CacheItemDataStore(currentTimeProviderMock);
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
        final ItemEntity entity = createFakeItemEntity();

        // When
        cacheItemDataStore.putItem(entity);
        final boolean isCached = cacheItemDataStore.isCached(TestMockData.FAKE_ID);

        // Then
        assertThat(isCached, is(true));
    }

    @Test
    public void givenEmptyCache_ThenNoDataCallbackOnRequest()
    {
        // When
        cacheItemDataStore.getItem(TestMockData.FAKE_ID).subscribe(testSubscriber);

        // Then
        testSubscriber.assertValue(Optional.absent());
    }

    @Test
    public void givenPutItemToCache_ThenItemIsEqualFromCache()
    {
        // Given
        final ItemEntity entity = createFakeItemEntity();
        cacheItemDataStore.putItem(entity);

        // When
        cacheItemDataStore.getItem(TestMockData.FAKE_ID).subscribe(testSubscriber);

        // Then
        final Optional<ItemEntity> expectedResult = Optional.of(entity);
        testSubscriber.assertValue(expectedResult);
    }

    @Test
    public void givenCheckExpiredImmediatelyAfterCache_ThenCacheNotExpired()
    {
        // Given
        cacheItemDataStore.putItem(createFakeItemEntity());

        // When
        boolean isExpired = cacheItemDataStore.isExpired();

        // Then
        assertThat(isExpired, is(false));
    }

    @Test
    public void givenCheckExpiredAfterExpiration_ThenCacheExpired()
    {
        // Given
        cacheItemDataStore.putItem(createFakeItemEntity());

        // When
        updateCurrentTime(CacheItemDataStore.EXPIRATION_TIME + 1);
        boolean isExpired = cacheItemDataStore.isExpired();

        // Then
        assertThat(isExpired, is(true));
    }

    @Test
    public void givenCheckExpiredAfterExpiration_ThenItemNoLongerCached()
    {
        // Given
        cacheItemDataStore.putItem(createFakeItemEntity());

        // When
        updateCurrentTime(CacheItemDataStore.EXPIRATION_TIME + 1);
        cacheItemDataStore.isExpired();
        final boolean isCached = cacheItemDataStore.isCached(TestMockData.FAKE_ID);

        // Then
        assertThat(isCached, is(false));
    }


    private ItemEntity createFakeItemEntity()
    {
        ItemEntity entity = new ItemEntity();
        entity.setId(TestMockData.FAKE_ID);
        return entity;
    }

    private void updateCurrentTime(long addTime)
    {
        // Move time on by adding the provided time offset
        when(currentTimeProviderMock.getCurrentTimeMillis()).thenReturn(NOW + addTime);
    }
}
