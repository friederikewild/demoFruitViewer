package me.friederikewild.demo.touchnote.details;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import me.friederikewild.demo.touchnote.TestUseCaseScheduler;
import me.friederikewild.demo.touchnote.domain.ItemsRepository;
import me.friederikewild.demo.touchnote.domain.model.Item;
import me.friederikewild.demo.touchnote.domain.usecase.GetItemUseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCaseHandler;

import static me.friederikewild.demo.touchnote.TestMockData.FAKE_ID;
import static me.friederikewild.demo.touchnote.TestMockData.ITEM;
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
    private ItemsRepository repositoryMock;

    @Captor
    private ArgumentCaptor<ItemsRepository.GetItemCallback> itemCallbackCaptor;

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
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetItemUseCase getItemUseCase = new GetItemUseCase(repositoryMock);

        return new DetailsPresenter(detailsViewMock,
                                    FAKE_ID,
                                    useCaseHandler,
                                    getItemUseCase);
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
        // When
        presenter.subscribe();

        // Then
        verify(detailsViewMock).setLoadingIndicator(eq(true));
    }

    @Test
    public void givenPresenterReceivesItemData_ThenViewUpdatedToStartAndStopShowLoading()
    {
        // Given
        presenter.subscribe();

        // When
        setItemRemoteAvailable(ITEM);

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
        presenter.subscribe();

        // When
        setItemRemoteAvailable(ITEM);

        // Then
        // Then first loading indicator is shown
        InOrder inOrder = inOrder(detailsViewMock);
        inOrder.verify(detailsViewMock).setLoadingIndicator(true);

        // Then loading indicator is hidden
        inOrder.verify(detailsViewMock).showItemImage(ITEM.getImageUrl());
        inOrder.verify(detailsViewMock).showItemTitle(ITEM.getTitle());
    }

    private void setItemRemoteAvailable(@NonNull Item item)
    {
        verify(repositoryMock).getItem(eq(item.getId()), itemCallbackCaptor.capture(), any());
        itemCallbackCaptor.getValue().onItemLoaded(item);
    }

}