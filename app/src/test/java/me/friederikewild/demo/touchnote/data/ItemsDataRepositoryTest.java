package me.friederikewild.demo.touchnote.data;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import me.friederikewild.demo.touchnote.TestMockData;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.ItemCache;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import me.friederikewild.demo.touchnote.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.touchnote.domain.model.Item;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test {@link ItemsDataRepository}
 */
public class ItemsDataRepositoryTest
{
    // Repository under test
    private ItemsDataRepository repository;

    // Directly use mapping helper
    private ItemEntityDataMapper dataMapper;

    @Mock
    private ItemsDataStore remoteMock;
    @Mock
    private ItemCache cacheMock;

    @Mock
    private ItemsDataRepository.GetItemsCallback itemsCallbackMock;
    @Mock
    private ItemsDataRepository.GetItemCallback itemCallbackMock;
    @Mock
    private GetNoDataCallback noItemCallbackMock;

    @Captor
    private ArgumentCaptor<ItemsDataStore.GetEntityItemsCallback> itemsCallbackCaptor;
    @Captor
    private ArgumentCaptor<GetNoDataCallback> noDataCallbackCaptor;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        dataMapper = new ItemEntityDataMapper();

        repository = new ItemsDataRepository(dataMapper, remoteMock, cacheMock);
    }

    @Test
    public void givenRequestItems_ThenRemoteListRequested()
    {
        // Given
        setupCacheEmpty();

        // When
        repository.getItems(itemsCallbackMock, noItemCallbackMock);

        // Then
        verify(remoteMock).getItems(any(), any());
    }

    @Test
    public void givenRequestItemsWithoutConnection_ThenNotAvailableCallbackIsNotified()
    {
        // Given
        repository.getItems(itemsCallbackMock, noItemCallbackMock);

        // When remote has no data
        setItemsRemoteNotAvailable();

        // Then
        verify(noItemCallbackMock).onNoDataAvailable();
    }

    @Test
    public void givenRequestItems_ThenItemsRetrievedFromRemote()
    {
        // Given
        setupCacheEmpty();
        repository.getItems(itemsCallbackMock, noItemCallbackMock);

        // When remote has data available
        setItemsRemoteAvailable(TestMockData.ENTITY_ITEMS);

        // Then
        final List<Item> expectedItems = dataMapper.transform(TestMockData.ENTITY_ITEMS);
        verify(itemsCallbackMock).onItemsLoaded(expectedItems);
    }

    @Test
    public void givenCacheIsEmpty_ThenRemoteListRequested()
    {
        // Given
        setupCacheEmpty();

        // When
        repository.getItem(TestMockData.FAKE_ID, itemCallbackMock, noItemCallbackMock);

        // Then
        verify(remoteMock).getItems(any(), any());
    }

    @Test
    public void givenItemIsCached_ThenCacheRequested()
    {
        // Given
        setupCachePutItem(TestMockData.FAKE_ID);

        // When
        repository.getItem(TestMockData.FAKE_ID, itemCallbackMock, noItemCallbackMock);

        // Then
        verify(cacheMock).getItem(eq(TestMockData.FAKE_ID), any(), any());
    }

    @Test
    public void givenItemNotFound_ThenNotAvailableCallbackIsNotified()
    {
        // Given
        setupCacheEmpty();

        // When
        repository.getItem(TestMockData.FAKE_ID, itemCallbackMock, noItemCallbackMock);
        setItemsRemoteNotAvailable();

        // Then
        verify(noItemCallbackMock).onNoDataAvailable();
    }

    @Test
    public void givenRemoteItemsFetched_ThenCacheUpdated()
    {
        // Given
        repository.getItems(itemsCallbackMock, noItemCallbackMock);

        // When remote has data available
        setItemsRemoteAvailable(TestMockData.ENTITY_ITEMS);

        // Then cache cleared and given amount of items saved
        verify(cacheMock).clearAll();
        verify(cacheMock, times(TestMockData.ENTITY_ITEMS.size())).putItem(any(ItemEntity.class));
    }

    @Test
    public void givenRefreshRequest_ThenCacheIsCleared()
    {
        // When
        repository.refreshData();

        // Then
        verify(cacheMock).clearAll();
    }


    private void setupCacheEmpty()
    {
        when(cacheMock.isExpired()).thenReturn(false);
        when(cacheMock.isCached(any())).thenReturn(false);
    }

    private void setupCachePutItem(@NonNull final String id)
    {
        when(cacheMock.isExpired()).thenReturn(false);
        when(cacheMock.isCached(id)).thenReturn(true);
    }

    private void setItemsRemoteAvailable(@NonNull List<ItemEntity> items)
    {
        verify(remoteMock).getItems(itemsCallbackCaptor.capture(), any());
        itemsCallbackCaptor.getValue().onItemsLoaded(items);
    }

    private void setItemsRemoteNotAvailable()
    {
        verify(remoteMock).getItems(any(), noDataCallbackCaptor.capture());
        noDataCallbackCaptor.getValue().onNoDataAvailable();
    }
}
