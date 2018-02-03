package me.friederikewild.demo.touchnote.data.datasource.remote;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.data.datasource.ItemsDataStore;
import me.friederikewild.demo.touchnote.data.entity.ItemEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * CTest {@link RemoteItemsDataStore}
 */
public class RemoteItemsDataStoreTest
{
    private static final String FAKE_ID = "123";
    private static final String FAKE_ID2 = "456";
    private static final String FAKE_ID3 = "789";

    private static List<ItemEntity> ITEMS = Lists.newArrayList(
            new ItemEntity(FAKE_ID),
            new ItemEntity(FAKE_ID2),
            new ItemEntity(FAKE_ID3));

    // Class under test
    private RemoteItemsDataStore remoteItemsDataStore;

    @Mock
    private ItemsApiProvider itemsApiProviderMock;

    @Mock
    private ItemsDataStore.GetEntityItemsCallback itemsCallbackMock;
    @Mock
    private GetNoDataCallback noDataCallbackMock;

    @Captor
    private ArgumentCaptor<ItemsDataStore.GetEntityItemsCallback> itemsCallbackCaptor;
    @Captor
    private ArgumentCaptor<GetNoDataCallback> noDataCallbackCaptor;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        remoteItemsDataStore = new RemoteItemsDataStore(itemsApiProviderMock);
    }

    @Test
    public void givenRemoteItemsRequested_ThenProviderIsAskedForThem()
    {
        // When
        remoteItemsDataStore.getItems(itemsCallbackMock, noDataCallbackMock);

        // Then
        verify(itemsApiProviderMock).enqueueGetItems(itemsCallbackMock, noDataCallbackMock);
    }

    @Test
    public void givenRemoteItemsRequestedWithoutConnection_ThenNoDataCallbackNotified()
    {
        // Given
        remoteItemsDataStore.getItems(itemsCallbackMock, noDataCallbackMock);

        // When remote has no data
        setItemsRemoteNotAvailable();

        // Then
        verify(noDataCallbackMock).onNoDataAvailable();
    }

    @Test
    public void givenRemoteItemsRequestedAndReceived_ThenItemsCallbackNotified()
    {
        // Given
        remoteItemsDataStore.getItems(itemsCallbackMock, noDataCallbackMock);

        // When remote has data available
        setItemsRemoteAvailable(ITEMS);

        // Then
        verify(itemsCallbackMock).onItemsLoaded(ITEMS);
    }

    private void setItemsRemoteAvailable(@NonNull List<ItemEntity> items)
    {
        verify(itemsApiProviderMock).enqueueGetItems(itemsCallbackCaptor.capture(), any());
        itemsCallbackCaptor.getValue().onItemsLoaded(items);
    }

    private void setItemsRemoteNotAvailable()
    {
        verify(itemsApiProviderMock).enqueueGetItems(any(), noDataCallbackCaptor.capture());
        noDataCallbackCaptor.getValue().onNoDataAvailable();
    }
}
