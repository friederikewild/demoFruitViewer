package me.friederikewild.demo.touchnote.overview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import me.friederikewild.demo.touchnote.domain.model.Item;
import me.friederikewild.demo.touchnote.domain.usecase.GetItemsUseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCaseHandler;

import static me.friederikewild.demo.touchnote.overview.OverviewLayoutType.GRID_LAYOUT;
import static me.friederikewild.demo.touchnote.overview.OverviewLayoutType.LIST_LAYOUT;

public class OverviewPresenter implements OverviewContract.Presenter
{
    @NonNull
    private OverviewContract.View overviewView;

    @NonNull
    private GetItemsUseCase getItemsUseCase;

    @NonNull
    private UseCaseHandler useCaseHandler;

    @NonNull
    private OverviewLayoutType currentLayoutType;

    /**
     * Keep track if data was provided to the view to keep menu icon updated
     */
    private boolean isViewCurrentlyEmpty = false;

    // Simple flag to check to keep track if ever loaded data
    private boolean isFirstLoading = true;

    OverviewPresenter(@NonNull OverviewContract.View view,
                      @NonNull UseCaseHandler handler,
                      @NonNull GetItemsUseCase getItems)
    {
        useCaseHandler = handler;
        getItemsUseCase = getItems;

        overviewView = view;
        overviewView.setPresenter(this);

        // TODO: Setup from bundle if available
        currentLayoutType = OverviewLayoutType.LIST_LAYOUT;
    }

    @Override
    public void start()
    {
        loadItems(false);

        // Ensure menu is updated according to layout state of presenter
        overviewView.updateMenuItemVisibility();
    }

    //region [OverviewContractPresenter]

    //region [LoadItems Handling]
    @Override
    public void loadItems(final boolean forceUpdate)
    {
        loadItems(forceUpdate || isFirstLoading, true);
        isFirstLoading = false;
    }

    @VisibleForTesting
    void loadItems(final boolean forceUpdate, final boolean showLoadingUI)
    {
        if (showLoadingUI && overviewView.isActive())
        {
            overviewView.setLoadingIndicator(true);
        }

        final GetItemsUseCase.RequestParams params = new GetItemsUseCase.RequestParams(forceUpdate);
        useCaseHandler.execute(getItemsUseCase,
                               params,
                               new UseCase.UseCaseCallback<GetItemsUseCase.Result>()
                               {
                                   @Override
                                   public void onSuccess(@NonNull GetItemsUseCase.Result result)
                                   {
                                       final List<Item> items = result.getItems();
                                       setIsViewCurrentlyEmpty(items);

                                       updateUiWithItems(items);
                                   }

                                   @Override
                                   public void onError()
                                   {
                                       setViewIsCurrentlyEmpty();

                                       updateUiWithLoadingError();
                                   }
                               });
    }

    @VisibleForTesting
    void setViewIsCurrentlyEmpty()
    {
        setIsViewCurrentlyEmpty(null);
    }

    @VisibleForTesting
    void setIsViewCurrentlyEmpty(@Nullable List<Item> items)
    {
        isViewCurrentlyEmpty = items == null || items.isEmpty();
    }

    private void updateUiWithItems(@NonNull List<Item> items)
    {
        // Check if view is still able to handle UI updates
        if (!overviewView.isActive())
        {
            return;
        }

        overviewView.setLoadingIndicator(false);

        if (isViewCurrentlyEmpty)
        {
            overviewView.showNoItemsAvailable();
        }
        else
        {
            overviewView.showItems(items);
        }

        // Ensure menu is updated according to items availability
        overviewView.updateMenuItemVisibility();
    }

    private void updateUiWithLoadingError()
    {
        // Check if view is still able to handle UI updates
        if (!overviewView.isActive())
        {
            return;
        }

        overviewView.setLoadingIndicator(false);

        overviewView.showLoadingItemsError();

        // Ensure menu is updated according to items availability
        overviewView.updateMenuItemVisibility();
    }
    //endregion [LoadItems Handling]

    @Override
    public void setLayoutPresentation(@NonNull OverviewLayoutType layoutType)
    {
        currentLayoutType = layoutType;

        // Update view
        switch (currentLayoutType)
        {
            case LIST_LAYOUT:
            {
                overviewView.setListLayout();
                // Menu item needs to be toggled
                overviewView.updateMenuItemVisibility();
                break;
            }

            case GRID_LAYOUT:
            {
                overviewView.setGridLayout();
                // Menu item needs to be toggled
                overviewView.updateMenuItemVisibility();
                break;
            }
        }
    }

    @Override
    public boolean isListOptionAvailable()
    {
        // Layout option is a toggle. Show the icon of the layout currently NOT shown
        final boolean showListOption = currentLayoutType == GRID_LAYOUT;

        return showListOption && isLayoutOptionVisible();
    }

    @Override
    public boolean isGridOptionAvailable()
    {
        // Layout option is a toggle. Show the icon of the layout currently NOT shown
        final boolean showGridOption = currentLayoutType == LIST_LAYOUT;

        return showGridOption && isLayoutOptionVisible();
    }

    private boolean isLayoutOptionVisible()
    {
        // Hide option when no data is shown
        return !isViewCurrentlyEmpty;
    }
    //endregion [OverviewContractPresenter]
}
