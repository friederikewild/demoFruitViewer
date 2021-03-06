package me.friederikewild.demo.fruits.data.datasource.remote;

import android.support.annotation.NonNull;

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
import me.friederikewild.demo.fruits.data.entity.FruitEntity;

import static org.mockito.Mockito.when;

/**
 * CTest {@link RemoteFruitsDataStore}
 */
public class RemoteFruitsDataStoreTest
{
    // Class under test
    private RemoteFruitsDataStore remoteItemsDataStore;

    private TestSubscriber<List<FruitEntity>> testSubscriber;

    @Mock
    private FruitsApiProvider fruitsApiProviderMock;
    @Mock
    private FruitsApi fruitsApiMock;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        when(fruitsApiProviderMock.getFruitsApi()).thenReturn(fruitsApiMock);

        testSubscriber = new TestSubscriber<>();

        remoteItemsDataStore = new RemoteFruitsDataStore(fruitsApiProviderMock);
    }

    @Test
    public void givenRemoteItemsRequestedWithoutConnection_ThenEmptyList()
    {
        // Given when no data available
        setItemsRemoteNotAvailable();

        // When
        remoteItemsDataStore.getFruits().subscribe(testSubscriber);

        // Then 1 empty list received
        Assert.assertEquals(1, testSubscriber.values().size());
        Assert.assertEquals(0, testSubscriber.values().get(0).size());
    }

    @Test
    public void givenRemoteItemsRequestedAndReceived_ThenItemsCallbackNotified()
    {
        // Given when remote has data available
        setItemsRemoteAvailable(TestMockData.ENTITY_FRUITS);

        // When
        remoteItemsDataStore.getFruits().subscribe(testSubscriber);

        // Then 1 list received with items
        Assert.assertEquals(1, testSubscriber.values().size());
        testSubscriber.assertValue(TestMockData.ENTITY_FRUITS);
    }

    private void setItemsRemoteAvailable(@NonNull List<FruitEntity> items)
    {
        when(fruitsApiMock.getFruits()).thenReturn(Flowable.just(items));
    }

    private void setItemsRemoteNotAvailable()
    {
        when(fruitsApiMock.getFruits()).thenReturn(Flowable.just(Collections.emptyList()));
    }
}
