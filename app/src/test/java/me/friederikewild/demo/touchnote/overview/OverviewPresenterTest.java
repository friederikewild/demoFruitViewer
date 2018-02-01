package me.friederikewild.demo.touchnote.overview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link OverviewPresenter}
 */
public class OverviewPresenterTest
{
    // Presenter under test
    OverviewContract.Presenter presenter;

    @Mock
    OverviewContract.View overviewViewMock;

    @Before
    public void setupOverviewPresenter()
    {
        MockitoAnnotations.initMocks(this);
        presenter = givenOverviewPresenter();
    }

    @Test
    public void givenCreatePresenter_ThenViewReceivesPresenter()
    {
        // Given
        presenter = givenOverviewPresenter();

        // Then
        verify(overviewViewMock).setPresenter(presenter);
    }

    private OverviewPresenter givenOverviewPresenter()
    {
        OverviewPresenter presenter = new OverviewPresenter(overviewViewMock);
        // TODO: Create further needed mocks
        return presenter;
    }
}
