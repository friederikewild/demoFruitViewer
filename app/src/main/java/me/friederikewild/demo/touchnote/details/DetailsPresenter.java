package me.friederikewild.demo.touchnote.details;

import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.domain.usecase.GetItemUseCase;
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
        detailsView.setLoadingIndicator(true);

    }
}
