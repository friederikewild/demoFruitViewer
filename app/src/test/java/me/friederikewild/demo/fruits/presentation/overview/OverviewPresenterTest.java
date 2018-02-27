package me.friederikewild.demo.fruits.presentation.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import me.friederikewild.demo.fruits.TestSerializableBundler;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.domain.usecase.GetFruitsUseCase;

import static me.friederikewild.demo.fruits.TestMockData.FRUIT;
import static me.friederikewild.demo.fruits.TestMockData.FRUITS;
import static me.friederikewild.demo.fruits.presentation.overview.OverviewLayoutType.GRID_LAYOUT;
import static me.friederikewild.demo.fruits.presentation.overview.OverviewLayoutType.LIST_LAYOUT;
import static org.mockito.ArgumentMatchers.any;
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
    private GetFruitsUseCase getFruitsUseCaseMock;
    @Mock
    private Bundle bundleMock;

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
        return new OverviewPresenter(overviewViewMock,
                                     getFruitsUseCaseMock,
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
        // Given use case is set up
        setUseCaseItemsEmptyList();

        // When
        presenter.subscribe();

        // Then
        verify(overviewViewMock).setLoadingIndicator(eq(true));
    }

    @Test
    public void givenPresenterSubscribed_ThenViewUpdatesMenuVisibility()
    {
        // Given use case is set up
        setUseCaseItemsEmptyList();
        // And view not active on data received
        when(overviewViewMock.isActive()).thenReturn(false);

        // When
        presenter.subscribe();

        // Then menu updated twice for initial start and on data
        verify(overviewViewMock).updateMenuItemVisibility();
    }

    //region [Test LoadItems]
    @Test
    public void givenLoadItemsWithoutUiRefresh_ThenShowNoLoading()
    {
        // Given
        setUseCaseItemsEmptyList();
        final boolean showLoadingUI = false;

        // When
        presenter.loadFruits(true, showLoadingUI);

        // Then
        verify(overviewViewMock, never()).setLoadingIndicator(eq(true));
    }

    @Test
    public void givenLoadItemsReceivesData_ThenViewUpdatedToStartAndStopShowLoading()
    {
        // Given
        setUseCaseItemsAvailable(FRUITS);

        // When
        presenter.loadFruits(true);

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
        setUseCaseItemsAvailable(FRUITS);

        // When
        presenter.loadFruits(true);

        // Then
        verify(overviewViewMock).showFruits(FRUITS);
    }

    @Test
    public void givenLoadItemsNoneAvailable_ThenViewShowsEmptyHint()
    {
        // Given
        setUseCaseItemsEmptyList();

        // When
        presenter.loadFruits(true);

        // Then
        verify(overviewViewMock).showNoFruitsAvailable();
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
        presenter.setIsViewCurrentlyEmpty(FRUITS);
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
        presenter.setIsViewCurrentlyEmpty(FRUITS);
        presenter.setLayoutPresentation(GRID_LAYOUT);

        // When view requests if list menu item is visible
        final boolean isListOptionAvailable = presenter.isListLayoutOptionAvailable();

        // Then
        Assert.assertTrue("List option available", isListOptionAvailable);
    }
    //endregion [Test Menu Visibility]

    //region [Test Actions]
    @Test
    public void givenPresenterInformedAboutClick_ThenViewShowsDetails()
    {
        // Given
        reset(overviewViewMock);

        // When
        presenter.onFruitItemClicked(FRUIT);

        // Then
        verify(overviewViewMock).showDetailsForFruit(FRUIT.getId());
    }

    @Test
    public void givenPresenterInformedAboutMoreAction_ThenViewShowsMore()
    {
        // Given
        reset(overviewViewMock);

        // When
        presenter.onFruitActionMore(FRUIT);

        // Then
        verify(overviewViewMock).showMoreView(FRUIT.getSourceUrl());
    }
    //endregion [Test Actions]

    private void setUseCaseItemsAvailable(@NonNull List<Fruit> fruits)
    {
        when(getFruitsUseCaseMock.execute(any())).thenReturn(Single.just(fruits));
    }

    private void setUseCaseItemsEmptyList()
    {
        when(getFruitsUseCaseMock.execute(any())).thenReturn(Single.just(Collections.emptyList()));
    }
}
