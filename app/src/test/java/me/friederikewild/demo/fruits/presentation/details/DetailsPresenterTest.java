package me.friederikewild.demo.fruits.presentation.details;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.domain.usecase.GetFruitUseCase;

import static me.friederikewild.demo.fruits.TestMockData.FAKE_ID;
import static me.friederikewild.demo.fruits.TestMockData.FRUIT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link DetailsPresenter}
 */
public class DetailsPresenterTest
{
    // Presenter under test
    private DetailsPresenter presenter;

    @Mock
    private DetailsContract.View detailsViewMock;
    @Mock
    private GetFruitUseCase getFruitUseCaseMock;

    @Before
    public void setupOverviewPresenter()
    {
        MockitoAnnotations.initMocks(this);
        // View needs to be active for presenter to call callbacks
        when(detailsViewMock.isActive()).thenReturn(true);

        presenter = givenOverviewPresenter();
    }

    private DetailsPresenter givenOverviewPresenter()
    {
        return new DetailsPresenter(detailsViewMock,
                                    FAKE_ID,
                                    getFruitUseCaseMock);
    }

    @Test
    public void givenCreatePresenter_ThenViewReceivesPresenter()
    {
        // Given we start with fresh mock
        reset(detailsViewMock);

        // When
        presenter = givenOverviewPresenter();

        // Then
        verify(detailsViewMock).setPresenter(presenter);
    }

    @Test
    public void givenPresenterSubscribed_ThenViewShowsLoading()
    {
        // Given
        setUseCaseItemAvailable(FRUIT);

        // When
        presenter.subscribe();

        // Then
        verify(detailsViewMock).setLoadingIndicator(eq(true));
    }

    @Test
    public void givenPresenterReceivesItemData_ThenViewUpdatedToStartAndStopShowLoading()
    {
        // Given
        setUseCaseItemAvailable(FRUIT);

        // When
        presenter.subscribe();

        // Then first loading indicator is shown
        InOrder inOrder = inOrder(detailsViewMock);
        inOrder.verify(detailsViewMock).setLoadingIndicator(true);

        // Then loading indicator is hidden
        inOrder.verify(detailsViewMock).setLoadingIndicator(false);
    }

    @Test
    public void givenPresenterReceivesItemData_ThenViewIsUpdatedWithItemData()
    {
        // Given
        setUseCaseItemAvailable(FRUIT);

        // When
        presenter.subscribe();

        // Then
        // Then first loading indicator is shown
        InOrder inOrder = inOrder(detailsViewMock);
        inOrder.verify(detailsViewMock).setLoadingIndicator(true);

        // Then loading indicator is hidden
        inOrder.verify(detailsViewMock).showFruitImage(FRUIT.getImageUrl());
        inOrder.verify(detailsViewMock).showImageCredits(FRUIT.getImageCredits());
        inOrder.verify(detailsViewMock).showFruitTitle(FRUIT.getTitle());
        inOrder.verify(detailsViewMock).showFruitDescription(FRUIT.getDescription());
        inOrder.verify(detailsViewMock).showFruitSourceProvider(FRUIT.getSourceProvider());
        inOrder.verify(detailsViewMock).showFruitMoreLink(FRUIT.getSourceUrl());
    }

    @Test
    public void givenPresenterNotifiedOnCreditsClicked_ThenViewShowsCreditsInfo()
    {
        // Given
        final String expectedCredits = FRUIT.getImageCredits();

        // When
        presenter.onImageCreditsInfoClicked(expectedCredits);

        // Then
        verify(detailsViewMock).showImageCreditsDialog(eq(expectedCredits));
    }

    @Test
    public void givenPresenterNotifiedOnMoreActionClicked_ThenViewShowsMoreView()
    {
        // Given
        final String expectedMoreUrl = FRUIT.getSourceUrl();

        // When
        presenter.onMoreActionClicked(expectedMoreUrl);

        // Then
        verify(detailsViewMock).showMoreView(eq(expectedMoreUrl));
    }

    private void setUseCaseItemAvailable(@NonNull Fruit fruit)
    {
        when(getFruitUseCaseMock.execute(any())).thenReturn(Single.just(fruit));
    }
}