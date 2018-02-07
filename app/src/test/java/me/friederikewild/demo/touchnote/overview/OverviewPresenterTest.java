package me.friederikewild.demo.touchnote.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import me.friederikewild.demo.touchnote.TestMockData;
import me.friederikewild.demo.touchnote.TestSerializableBundler;
import me.friederikewild.demo.touchnote.TestUseCaseScheduler;
import me.friederikewild.demo.touchnote.data.GetNoDataCallback;
import me.friederikewild.demo.touchnote.domain.ItemsRepository;
import me.friederikewild.demo.touchnote.domain.model.Item;
import me.friederikewild.demo.touchnote.domain.usecase.GetItemsUseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCaseHandler;

import static me.friederikewild.demo.touchnote.TestMockData.ITEMS;
import static me.friederikewild.demo.touchnote.overview.OverviewLayoutType.GRID_LAYOUT;
import static me.friederikewild.demo.touchnote.overview.OverviewLayoutType.LIST_LAYOUT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link OverviewPresenter}
 */
public class OverviewPresenterTest
{
    // Presenter under test
    private OverviewPresenter presenter;

    private TestSerializableBundler testSerializableBundler;

    @Mock
    private OverviewContract.View overviewViewMock;
    @Mock
    private ItemsRepository repositoryMock;
    @Mock
    private Bundle bundleMock;

    @Captor
    private ArgumentCaptor<ItemsRepository.GetItemsCallback> itemsCallbackCaptor;
    @Captor
    private ArgumentCaptor<GetNoDataCallback> noDataCallbackCaptor;

    @Before
    public void setupOverviewPresenter()
    {
        MockitoAnnotations.initMocks(this);
        // View needs to be active for presenter to call callbacks
        when(overviewViewMock.isActive()).thenReturn(true);

        testSerializableBundler = new TestSerializableBundler();

        presenter = givenOverviewPresenter();
    }

    private OverviewPresenter givenOverviewPresenter()
    {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetItemsUseCase getItemsUseCase = new GetItemsUseCase(repositoryMock);

        return new OverviewPresenter(overviewViewMock,
                                     useCaseHandler,
                                     getItemsUseCase,
                                     testSerializableBundler);
    }

    @Test
    public void givenCreatePresenter_ThenViewReceivesPresenter()
    {
        // Given we start with fresh mock
        reset(overviewViewMock);

        // When
        presenter = givenOverviewPresenter();

        // Then
        verify(overviewViewMock).setPresenter(presenter);
    }

    @Test
    public void givenPresenterSubscribed_ThenViewShowsLoading()
    {
        // When
        presenter.subscribe();

        // Then
        verify(overviewViewMock).setLoadingIndicator(eq(true));
    }

    @Test
    public void givenPresenterSubscribed_ThenViewUpdatesMenuVisibility()
    {
        // When
        presenter.subscribe();

        // Then
        verify(overviewViewMock).updateMenuItemVisibility();
    }

    //region [Test LoadItems]
    @Test
    public void givenLoadItemsWithoutUiRefresh_ThenShowNoLoading()
    {
        // Given
        final boolean showLoadingUI = false;

        // When
        presenter.loadItems(true, showLoadingUI);

        // Then
        verify(overviewViewMock, never()).setLoadingIndicator(anyBoolean());
    }

    @Test
    public void givenLoadItemsReceivesData_ThenViewUpdatedToStartAndStopShowLoading()
    {
        // Given
        presenter.loadItems(true);

        // When
        setItemsRemoteAvailable(ITEMS);

        // Then first loading indicator is shown
        InOrder inOrder = inOrder(overviewViewMock);
        inOrder.verify(overviewViewMock).setLoadingIndicator(true);

        // Then loading indicator is hidden
        inOrder.verify(overviewViewMock).setLoadingIndicator(false);
    }

    @Test
    public void givenLoadItemsReceivesData_ThenViewIsUpdated()
    {
        // Given
        presenter.loadItems(true);

        // When
        setItemsRemoteAvailable(ITEMS);

        // Then
        verify(overviewViewMock).showItems(ITEMS);
    }

    @Test
    public void givenLoadItemsNoneAvailable_ThenViewShowsEmptyHint()
    {
        // Given
        presenter.loadItems(true);

        // When
        setItemsRemoteAvailable(TestMockData.EMPTY_ITEMS);

        // Then
        verify(overviewViewMock).showNoItemsAvailable();
    }

    @Test
    public void givenLoadItemsNoConnection_ThenViewShowsEmptyHint()
    {
        // Given
        presenter.loadItems(true);

        // When
        setItemsRemoteNotAvailable();

        // Then
        verify(overviewViewMock).showLoadingItemsError();
    }
    //endregion [Test LoadItems]


    //region [Test Save/Load]
    @Test
    public void givenSaveFreshPresenter_ThenNothingSaved()
    {
        // When
        presenter.saveStateToBundle(bundleMock);

        // Then
        Assert.assertTrue(testSerializableBundler.isStorageEmpty());
    }

    @Test
    public void givenSavePresenterWithGridLayout_ThenBundlerNotEmpty()
    {
        // Given
        presenter.setLayoutPresentation(GRID_LAYOUT);

        // When
        presenter.saveStateToBundle(bundleMock);

        // Then
        Assert.assertFalse(testSerializableBundler.isStorageEmpty());
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Test
    public void givenFreshPresenterInitFromBundle_ThenListLayoutSetAsDefault()
    {
        // Given
        final OverviewLayoutType expectedLayout = LIST_LAYOUT;

        // When
        presenter.loadStateFromBundle(bundleMock);

        // Then
        Assert.assertEquals(expectedLayout, presenter.getCurrentLayoutType());
    }

    @Test
    public void givenPresenterWithListSavedToBundle_ThenListLayoutRestoredFromBundle()
    {
        // Given
        final OverviewLayoutType expectedLayout = LIST_LAYOUT;
        presenter.setLayoutPresentation(expectedLayout);

        // When
        presenter.saveStateToBundle(bundleMock);
        presenter.loadStateFromBundle(bundleMock);

        // Then
        Assert.assertEquals(expectedLayout, presenter.getCurrentLayoutType());
    }

    @Test
    public void givenPresenterWithGridSavedToBundle_ThenGridLayoutRestoredFromBundle()
    {
        // Given
        final OverviewLayoutType expectedLayout = GRID_LAYOUT;
        presenter.setLayoutPresentation(expectedLayout);

        // When
        presenter.saveStateToBundle(bundleMock);
        presenter.loadStateFromBundle(bundleMock);

        // Then
        Assert.assertEquals(expectedLayout, presenter.getCurrentLayoutType());
    }
    //endregion [Test Save/Load]

    //region [Test Set Correct Presentation]
    @Test
    public void givenSetLayoutToList_ThenViewPresentedAsList()
    {
        // When
        presenter.setLayoutPresentation(LIST_LAYOUT);

        // Then
        verify(overviewViewMock).setListLayout();
    }

    @Test
    public void givenSetLayoutToList_ThenViewMenuUpdated()
    {
        // When
        presenter.setLayoutPresentation(LIST_LAYOUT);

        // Then
        verify(overviewViewMock).updateMenuItemVisibility();
    }

    @Test
    public void givenSetLayoutToGrid_ThenViewPresentedAsGrid()
    {
        // When
        presenter.setLayoutPresentation(GRID_LAYOUT);

        // Then
        verify(overviewViewMock).setGridLayout();
    }

    @Test
    public void givenSetLayoutToGrid_ThenViewMenuUpdated()
    {
        // When
        presenter.setLayoutPresentation(GRID_LAYOUT);

        // Then
        verify(overviewViewMock).updateMenuItemVisibility();
    }
    //endregion [Test Set Correct Presentation]

    //region [Test Menu Visibility]
    @Test
    public void givenPresenterHasNoData_ThenListLayoutMenuIsInvisible()
    {
        // Given
        presenter.setViewIsCurrentlyEmpty();

        // When view requests if list menu item is visible
        final boolean isListOptionAvailable = presenter.isListLayoutOptionAvailable();

        // Then
        Assert.assertFalse("List option not available", isListOptionAvailable);
    }

    @Test
    public void givenPresenterHasNoData_ThenGridLayoutMenuIsInvisible()
    {
        // Given
        presenter.setViewIsCurrentlyEmpty();

        // When view requests if grid menu item is visible
        final boolean isGridOptionAvailable = presenter.isGridLayoutOptionAvailable();

        // Then
        Assert.assertFalse("Grid option not available", isGridOptionAvailable);
    }

    @Test
    public void givenPresenterHasDataAndSetListLayout_ThenGridLayoutMenuIsVisible()
    {
        // Given
        presenter.setIsViewCurrentlyEmpty(ITEMS);
        presenter.setLayoutPresentation(LIST_LAYOUT);

        // When view requests if grid menu item is visible
        final boolean isGridOptionAvailable = presenter.isGridLayoutOptionAvailable();

        // Then
        Assert.assertTrue("Grid option available", isGridOptionAvailable);
    }

    @Test
    public void givenPresenterHasDataAndSetGridLayout_ThenListLayoutMenuIsVisible()
    {
        // Given
        presenter.setIsViewCurrentlyEmpty(ITEMS);
        presenter.setLayoutPresentation(GRID_LAYOUT);

        // When view requests if list menu item is visible
        final boolean isListOptionAvailable = presenter.isListLayoutOptionAvailable();

        // Then
        Assert.assertTrue("List option available", isListOptionAvailable);
    }
    //endregion [Test Menu Visibility]


    private void setItemsRemoteAvailable(@NonNull List<Item> items)
    {
        verify(repositoryMock).getItems(itemsCallbackCaptor.capture(), any());
        itemsCallbackCaptor.getValue().onItemsLoaded(items);
    }

    private void setItemsRemoteNotAvailable()
    {
        verify(repositoryMock).getItems(any(), noDataCallbackCaptor.capture());
        noDataCallbackCaptor.getValue().onNoDataAvailable();
    }
}
