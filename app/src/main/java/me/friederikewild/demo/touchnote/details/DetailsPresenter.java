package me.friederikewild.demo.touchnote.details;

import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.domain.model.Item;
import me.friederikewild.demo.touchnote.domain.usecase.GetItemUseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCase;
import me.friederikewild.demo.touchnote.domain.usecase.UseCaseHandler;

public class DetailsPresenter implements DetailsContract.Presenter
{
    private DetailsContract.View detailsView;

    @NonNull
    private GetItemUseCase getItemUseCase;

    @NonNull
    private UseCaseHandler useCaseHandler;

    @NonNull
    private final String itemId;

    DetailsPresenter(@NonNull final DetailsContract.View view,
                     @NonNull final String id,
                     @NonNull final UseCaseHandler handler,
                     @NonNull final GetItemUseCase getItem)
    {
        itemId = id;

        useCaseHandler = handler;
        getItemUseCase = getItem;

        detailsView = view;
        detailsView.setPresenter(this);
    }

    @Override
    public void start()
    {
        openItem();
    }

    private void openItem()
    {
        if (detailsView.isActive())
        {
            detailsView.setLoadingIndicator(true);
        }

        final GetItemUseCase.RequestParams params = new GetItemUseCase.RequestParams(itemId);
        useCaseHandler.execute(getItemUseCase,
                               params,
                               new UseCase.UseCaseCallback<GetItemUseCase.Result>()
                               {
                                   @Override
                                   public void onSuccess(@NonNull GetItemUseCase.Result result)
                                   {
                                       final Item item = result.getItem();
                                       updateViewWithItem(item);
                                   }

                                   @Override
                                   public void onError()
                                   {
                                       updateViewWithError();
                                   }
                               });
    }

    private void updateViewWithItem(@NonNull Item item)
    {
        // Check if view is still able to handle UI updates
        if (!detailsView.isActive())
        {
            return;
        }

        detailsView.setLoadingIndicator(false);

        detailsView.showItem(item);
    }

    private void updateViewWithError()
    {
        // Check if view is still able to handle UI updates
        if (!detailsView.isActive())
        {
            return;
        }

        detailsView.setLoadingIndicator(false);

        detailsView.showLoadingItemError();
    }
}
