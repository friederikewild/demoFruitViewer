package me.friederikewild.demo.touchnote.overview;

import android.os.Bundle;
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
    @VisibleForTesting
    static final String KEY_BUNDLE_LAYOUT_TYPE = "KEY_BUNDLE_LAYOUT_TYPE";

    @NonNull
    private OverviewContract.View overviewView;

    @NonNull
    private GetItemsUseCase getItemsUseCase;

    @NonNull
    private UseCaseHandler useCaseHandler;

    // LateInit @NonNull
    private OverviewLayoutType currentLayoutType;

    /**
     * Keep track if data was provided to the view to keep menu icon updated
     */
    private boolean isViewCurrentlyEmpty = true;

    // Simple flag to check to keep track if ever loaded data
    private boolean isFirstLoading = true;

    OverviewPresenter(@NonNull final OverviewContract.View view,
                      @NonNull final UseCaseHandler handler,
                      @NonNull final GetItemsUseCase getItems)
    {
        useCaseHandler = handler;
        getItemsUseCase = getItems;

        overviewView = view;
        overviewView.setPresenter(this);
    }

    //region [Savable Presenter]
    @Override
    public void saveStateToBundle(@NonNull final Bundle outState)
    {
        if (currentLayoutType != null)
        {
            outState.putSerializable(KEY_BUNDLE_LAYOUT_TYPE, currentLayoutType);
        }
    }

    @Override
    public void loadStateFromBundle(@Nullable final Bundle savedState)
    {
        OverviewLayoutType loadedLayoutType = null;

        if (savedState != null)
        {
            loadedLayoutType = (OverviewLayoutType) savedState.getSerializable(KEY_BUNDLE_LAYOUT_TYPE);
        }

        // Use List style as default
        setLayoutPresentation(loadedLayoutType == null ? LIST_LAYOUT : loadedLayoutType, true);
    }
    //endregion

    @Override
    public void start()
    {
        loadItems(false);

        // Ensure menu is updated according to state of presenter and hidden until items are available
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
                                   public void onSuccess(@NonNull final GetItemsUseCase.Result result)
                                   {
                                       final List<Item> items = result.getItems();
                                       setIsViewCurrentlyEmpty(items);

                                       updateViewWithItems(items);
                                   }

                                   @Override
                                   public void onError()
                                   {
                                       setViewIsCurrentlyEmpty();

                                       updateViewWithLoadingError();
                                   }
                               });
    }

    @VisibleForTesting
    void setViewIsCurrentlyEmpty()
    {
        setIsViewCurrentlyEmpty(null);
    }

    @VisibleForTesting
    void setIsViewCurrentlyEmpty(@Nullable final List<Item> items)
    {
        isViewCurrentlyEmpty = items == null || items.isEmpty();
    }

    private void updateViewWithItems(@NonNull final List<Item> items)
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

    private void updateViewWithLoadingError()
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
    public void setLayoutPresentation(@NonNull final OverviewLayoutType layoutType)
    {
        setLayoutPresentation(layoutType, true);
    }

    private void setLayoutPresentation(@NonNull final OverviewLayoutType layoutType,
                                       final boolean refreshView)
    {
        currentLayoutType = layoutType;

        if (refreshView)
        {
            updateViewSetCurrentLayoutType();
        }
    }

    private void updateViewSetCurrentLayoutType()
    {
        // Check if view is still able to handle UI updates
        if (!overviewView.isActive())
        {
            return;
        }

        // Update view
        switch (currentLayoutType)
        {
            case LIST_LAYOUT:
            {
                overviewView.setListLayout();
                break;
            }

            case GRID_LAYOUT:
            {
                overviewView.setGridLayout();
                break;
            }
        }

        // Menu item needs to be toggled
        overviewView.updateMenuItemVisibility();
    }

    @Override
    public boolean isListLayoutOptionAvailable()
    {
        // Layout option is a toggle. Show the icon of the layout currently NOT shown
        final boolean showListOption = currentLayoutType == GRID_LAYOUT;

        return showListOption && isLayoutOptionVisible();
    }

    @Override
    public boolean isGridLayoutOptionAvailable()
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

    @VisibleForTesting
    OverviewLayoutType getCurrentLayoutType()
    {
        return currentLayoutType;
    }

    //endregion [OverviewContractPresenter]
}
