package me.friederikewild.demo.touchnote.overview;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import me.friederikewild.demo.touchnote.domain.model.Item;
import me.friederikewild.demo.touchnote.domain.usecase.GetItemsUseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCaseHandler;

public class OverviewPresenter implements OverviewContract.Presenter
{
    @NonNull
    private OverviewContract.View overviewView;

    @NonNull
    private GetItemsUseCase getItemsUseCase;

    @NonNull
    private UseCaseHandler useCaseHandler;

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
    }

    @Override
    public void start()
    {
        loadItems(false);
    }

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
                                       updateUiWithItems(items);
                                   }

                                   @Override
                                   public void onError()
                                   {
                                       updateUiWithLoadingError();
                                   }
                               });
    }

    private void updateUiWithItems(@NonNull List<Item> items)
    {
        // Check if view is still able to handle UI updates
        if (!overviewView.isActive())
        {
            return;
        }

        overviewView.setLoadingIndicator(false);

        if (items.isEmpty())
        {
            overviewView.showNoItemsAvailable();
        }
        else
        {
            overviewView.showItems(items);
        }
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
    }
}
