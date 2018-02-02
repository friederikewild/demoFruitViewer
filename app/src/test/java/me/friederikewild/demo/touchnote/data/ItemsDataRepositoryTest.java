package me.friederikewild.demo.touchnote.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.datasource.cache.CurrentTimeProvider;
import me.friederikewild.demo.touchnote.data.datasource.cache.ItemCache;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;
import me.friederikewild.demo.touchnote.data.entity.mapper.ItemEntityDataMapper;
import me.friederikewild.demo.touchnote.domain.model.Item;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test {@link ItemsDataRepository}
 */
public class ItemsDataRepositoryTest
{
    private static final String FAKE_ID = "123";
    private static final String FAKE_ID2 = "456";
    private static final String FAKE_ID3 = "789";

    private static List<Item> ITEMS = Lists.newArrayList(
            new Item(FAKE_ID),
            new Item(FAKE_ID2),
            new Item(FAKE_ID3));

    // Repository under test
    private ItemsDataRepository repository;

    @Mock
    private ItemEntityDataMapper mapperMock;
    @Mock
    private ItemsDataStore remoteMock;
    @Mock
    private ItemCache cacheMock;

    @Mock
    private ItemsDataRepository.GetItemsCallback itemsCallbackMock;
    @Mock
    private GetNoDataCallback noItemsCallbackMock;
    @Mock
    private ItemsDataRepository.GetItemCallback itemCallbackMock;
    @Mock
    private GetNoDataCallback noItemCallbackMock;

    @Mock
    private CurrentTimeProvider currentTimeProviderMock;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);

        repository = new ItemsDataRepository(mapperMock, remoteMock, cacheMock);
    }

    @Test
    public void givenCacheIsEmpty_ThenRemoteRequested()
    {
        // Given
        setupCacheEmpty();

        // When
        repository.getItem(FAKE_ID, itemCallbackMock, noItemCallbackMock);

        // Then
        verify(remoteMock).getItem(eq(FAKE_ID), any(), any());
    }

    @Test
    public void givenItemIsCached_ThenCacheRequested()
    {
        // Given
        setupCachePutItem(FAKE_ID);

        // When
        repository.getItem(FAKE_ID, itemCallbackMock, noItemCallbackMock);

        // Then
        verify(cacheMock).getItem(eq(FAKE_ID), any(), any());
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

    private Item createFakeItem(final String id)
    {
        // Using special test constructor
        return new Item(id);
    }

    private ItemEntity createFakeItemEntity(@NonNull final String id)
    {
        ItemEntity entity = new ItemEntity();
        entity.setId(id);
        return entity;
    }
}
