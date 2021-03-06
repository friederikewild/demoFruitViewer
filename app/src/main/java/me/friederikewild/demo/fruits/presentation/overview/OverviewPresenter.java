package me.friederikewild.demo.fruits.presentation.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.io.Serializable;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.domain.usecase.GetFruitsUseCase;
import me.friederikewild.demo.fruits.util.Bundler;

import static me.friederikewild.demo.fruits.presentation.overview.OverviewLayoutType.GRID_LAYOUT;
import static me.friederikewild.demo.fruits.presentation.overview.OverviewLayoutType.LIST_LAYOUT;

public class OverviewPresenter implements OverviewContract.Presenter
{
    private static final String KEY_BUNDLE_LAYOUT_TYPE = "KEY_BUNDLE_LAYOUT_TYPE";

    @NonNull
    private OverviewContract.View overviewView;

    @NonNull
    private GetFruitsUseCase getFruitsUseCase;

    @NonNull
    private Bundler<Serializable> serializableBundler;

    @NonNull
    private CompositeDisposable compositeDisposable;

    // LateInit @NonNull
    private OverviewLayoutType currentLayoutType;

    /**
     * This is the Layout Type received as result back from the details screen.
     * When present this is used instead of Bundle
     */
    @Nullable
    private OverviewLayoutType previousLayoutTypeReturnedFromRequest;

    /**
     * Keep track if data was provided to the view to keep menu icon updated
     */
    private boolean isViewCurrentlyEmpty = true;

    // Simple flag to check to keep track if ever loaded data
    private boolean isFirstLoading = true;

    OverviewPresenter(@NonNull final OverviewContract.View view,
                      @NonNull final GetFruitsUseCase getFruits,
                      @NonNull final Bundler<Serializable> bundler)
    {
        getFruitsUseCase = getFruits;
        serializableBundler = bundler;

        compositeDisposable = new CompositeDisposable();

        overviewView = view;
        overviewView.setPresenter(this);
    }

    //region [Savable Presenter]
    @Override
    public void saveStateToBundle(@NonNull final Bundle outState)
    {
        if (currentLayoutType != null)
        {
            serializableBundler.put(outState, KEY_BUNDLE_LAYOUT_TYPE, currentLayoutType);
        }
    }

    @Override
    public void loadStateFromBundle(@Nullable final Bundle savedState)
    {
        OverviewLayoutType newLayoutType = OverviewLayoutType.INVALID_TYPE;
        if (previousLayoutTypeReturnedFromRequest != null)
        {
            newLayoutType = previousLayoutTypeReturnedFromRequest;
            previousLayoutTypeReturnedFromRequest = null;
        }

        if (newLayoutType == OverviewLayoutType.INVALID_TYPE)
        {
            // NOTE: Use List style as default here sets up presenter + view in correct initial state
            newLayoutType = (OverviewLayoutType) serializableBundler.get(savedState,
                                                                         KEY_BUNDLE_LAYOUT_TYPE,
                                                                         LIST_LAYOUT);
        }

        // NOTE: Refreshing view for first adapter update
        setLayoutPresentation(newLayoutType, true);
    }
    //endregion

    @Override
    public void subscribe()
    {
        loadFruits(false);

        // Ensure menu is updated according to state of presenter and hidden until fruit items are available
        overviewView.updateMenuItemVisibility();
    }

    @Override
    public void unsubscribe()
    {
        compositeDisposable.clear();
    }

    //region [OverviewContractPresenter]

    //region [LoadFruits Handling]
    @Override
    public void loadFruits(final boolean forceUpdate)
    {
        loadFruits(forceUpdate || isFirstLoading, true);
        isFirstLoading = false;
    }

    @VisibleForTesting
    void loadFruits(final boolean forceUpdate, final boolean showLoadingUI)
    {
        if (showLoadingUI && overviewView.isActive())
        {
            overviewView.setLoadingIndicator(true);
        }

        compositeDisposable.clear();
        final GetFruitsUseCase.RequestParams params = new GetFruitsUseCase.RequestParams(forceUpdate);
        Disposable disposable = getFruitsUseCase
                .execute(params)
                .subscribe(
                        // onNext
                        resultFruits ->
                        {
                            setIsViewCurrentlyEmpty(resultFruits);
                            updateViewWithFruits(resultFruits);
                        },
                        // onError
                        throwable ->
                        {
                            setViewIsCurrentlyEmpty();
                            updateViewWithLoadingError();
                        }
                );
        compositeDisposable.add(disposable);
    }

    @VisibleForTesting
    void setViewIsCurrentlyEmpty()
    {
        setIsViewCurrentlyEmpty(null);
    }

    @VisibleForTesting
    void setIsViewCurrentlyEmpty(@Nullable final List<Fruit> fruits)
    {
        isViewCurrentlyEmpty = fruits == null || fruits.isEmpty();
    }

    private void updateViewWithFruits(@NonNull final List<Fruit> fruits)
    {
        // Check if view is still able to handle UI updates
        if (!overviewView.isActive())
        {
            return;
        }

        overviewView.setLoadingIndicator(false);

        if (isViewCurrentlyEmpty)
        {
            overviewView.showNoFruitsAvailable();
        }
        else
        {
            overviewView.showFruits(fruits);
        }

        // Ensure menu is updated according to fruits availability
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

        overviewView.showLoadingFruitsError();

        // Ensure menu is updated according to items availability
        overviewView.updateMenuItemVisibility();
    }
    //endregion [LoadFruits Handling]

    @Override
    public void onFruitItemClicked(@NonNull Fruit fruit)
    {
        overviewView.showDetailsForFruit(fruit.getId());
    }

    @Override
    public void onFruitActionMore(@NonNull Fruit fruit)
    {
        overviewView.showMoreView(fruit.getSourceUrl());
    }

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

    //endregion [OverviewContractPresenter]


    //region [Handle requests to switch screens]
    @Override
    public int getRequestCodeForDetail()
    {
        // Use our current layout as request code - this way presenter receives it back before next createFromBundle
        return currentLayoutType.ordinal();
    }

    @Override
    public void onReturnFromRequest(int requestCode)
    {
        // Fetch the encoded layout type
        previousLayoutTypeReturnedFromRequest = OverviewLayoutType.fromOrdinal(requestCode);
    }
    //endregion


    @VisibleForTesting
    OverviewLayoutType getCurrentLayoutType()
    {
        return currentLayoutType;
    }
}
