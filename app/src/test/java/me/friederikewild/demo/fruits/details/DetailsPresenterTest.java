package me.friederikewild.demo.fruits.details;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Single;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.domain.usecase.GetItemUseCase;

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
    private GetItemUseCase getItemUseCaseMock;

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
                                    getItemUseCaseMock);
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
        inOrder.verify(detailsViewMock).showItemImage(FRUIT.getImageUrl());
        inOrder.verify(detailsViewMock).showItemTitle(FRUIT.getTitle());
    }

    private void setUseCaseItemAvailable(@NonNull Fruit fruit)
    {
        when(getItemUseCaseMock.execute(any())).thenReturn(Single.just(fruit));
    }
}