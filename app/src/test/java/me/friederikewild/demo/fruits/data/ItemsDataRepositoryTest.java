package me.friederikewild.demo.fruits.data;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import me.friederikewild.demo.fruits.TestMockData;
import me.friederikewild.demo.fruits.data.datasource.FruitsDataStore;
import me.friederikewild.demo.fruits.data.datasource.cache.FruitCache;
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test {@link FruitsDataRepository}
 */
@SuppressWarnings("Guava")
public class ItemsDataRepositoryTest
{
    // Repository under test
    private FruitsDataRepository repository;

    private TestSubscriber<List<FruitEntity>> testListSubscriber;
    private TestSubscriber<Optional<FruitEntity>> testItemSubscriber;

    @Mock
    private FruitsDataStore remoteMock;
    @Mock
    private FruitCache cacheMock;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);

        testListSubscriber = new TestSubscriber<>();
        testItemSubscriber = new TestSubscriber<>();

        repository = new FruitsDataRepository(remoteMock, cacheMock);
    }

    @Test
    public void givenRequestItems_ThenRemoteListRequested()
    {
        // Given
        setupCacheEmpty();
        setItemsRemoteEmptyList();

        // When
        repository.getFruits().subscribe(testListSubscriber);

        // Then
        verify(remoteMock).getFruits();
    }

    @Test
    public void givenRequestItemsWithoutData_ThenListIsEmpty()
    {
        // Given remote has no data
        setupCacheEmpty();
        setItemsRemoteEmptyList();

        // When
        repository.getFruits().subscribe(testListSubscriber);

        // Then
        Assert.assertEquals(0, testListSubscriber.values().get(0).size());
    }

    @Test
    public void givenRequestItems_ThenItemsRetrievedFromRemote()
    {
        // Given remote data available
        setupCacheEmpty();
        setItemsRemoteAvailable(TestMockData.ENTITY_FRUITS);

        // When
        repository.getFruits().subscribe(testListSubscriber);

        // Then
        testListSubscriber.assertValue(TestMockData.ENTITY_FRUITS);
    }

    @Test
    public void givenItemIsCached_ThenCacheRequested()
    {
        // Given
        final Optional<FruitEntity> expected = setupCachePutItem(TestMockData.FAKE_ID);
        setItemsRemoteEmptyList();

        // When
        repository.getFruit(TestMockData.FAKE_ID).subscribe(testItemSubscriber);

        // Then
        testItemSubscriber.assertValue(expected);
    }

    @Test
    public void givenItemNotFoundWithEmptyListFromServer_ThenItemAbsent()
    {
        // Given
        final Optional<FruitEntity> expectedAbsent = setupCacheEmpty();
        setItemsRemoteEmptyList();

        // When
        repository.getFruit(TestMockData.FAKE_ID).subscribe(testItemSubscriber);

        // Then
        testItemSubscriber.assertValue(expectedAbsent);
    }

    @Test
    public void givenRemoteItemsFetched_ThenCacheUpdated()
    {
        // Given remote has data available
        setItemsRemoteAvailable(TestMockData.ENTITY_FRUITS);

        // When
        repository.getFruits().subscribe(testListSubscriber);

        // Then given amount of items saved to cache
        verify(cacheMock, times(TestMockData.ENTITY_FRUITS.size())).putFruit(any(FruitEntity.class));
    }

    @Test
    public void givenRefreshRequest_ThenCacheIsCleared()
    {
        // When
        repository.refreshData();

        // Then
        verify(cacheMock).clearAll();
    }

    private Optional<FruitEntity> setupCacheEmpty()
    {
        final Optional<FruitEntity> entityAbsent = Optional.absent();
        when(cacheMock.getFruit(any())).thenReturn(Flowable.just(entityAbsent));
        return entityAbsent;
    }

    private Optional<FruitEntity> setupCachePutItem(@NonNull final String id)
    {
        final Optional<FruitEntity> entityOptional = Optional.of(new FruitEntity(id));
        when(cacheMock.getFruit(id)).thenReturn(Flowable.just(entityOptional));
        return entityOptional;
    }

    private void setItemsRemoteAvailable(@NonNull List<FruitEntity> items)
    {
        when(remoteMock.getFruits()).thenReturn(Flowable.just(items));
    }

    private void setItemsRemoteEmptyList()
    {
        when(remoteMock.getFruits()).thenReturn(Flowable.just(Collections.emptyList()));
    }
}
