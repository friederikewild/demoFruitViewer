package me.friederikewild.demo.touchnote.overview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import me.friederikewild.demo.touchnote.TestUseCaseScheduler;
import me.friederikewild.demo.touchnote.domain.ItemsRepository;
import me.friederikewild.demo.touchnote.domain.usecase.GetItemsUseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCaseHandler;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
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

    @Mock
    private OverviewContract.View overviewViewMock;
    @Mock
    private ItemsRepository repositoryMock;

    @Before
    public void setupOverviewPresenter()
    {
        MockitoAnnotations.initMocks(this);
        // View needs to be active for presenter to call callbacks
        when(overviewViewMock.isActive()).thenReturn(true);

        presenter = givenOverviewPresenter();
    }

    private OverviewPresenter givenOverviewPresenter()
    {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetItemsUseCase getItemsUseCase = new GetItemsUseCase(repositoryMock);

        return new OverviewPresenter(overviewViewMock,
                                     useCaseHandler,
                                     getItemsUseCase);
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
    public void givenStartPresenter_ThenViewShowsLoading()
    {
        // When
        presenter.start();

        // Then
        verify(overviewViewMock).setLoadingIndicator(eq(true));
    }

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
}
