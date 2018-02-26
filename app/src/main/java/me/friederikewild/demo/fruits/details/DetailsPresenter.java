package me.friederikewild.demo.fruits.details;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.domain.usecase.GetItemUseCase;

public class DetailsPresenter implements DetailsContract.Presenter
{
    private DetailsContract.View detailsView;

    @NonNull
    private GetItemUseCase getItemUseCase;

    @NonNull
    private CompositeDisposable compositeDisposable;

    @NonNull
    private final String itemId;

    DetailsPresenter(@NonNull final DetailsContract.View view,
                     @NonNull final String id,
                     @NonNull final GetItemUseCase getItem)
    {
        itemId = id;
        getItemUseCase = getItem;

        compositeDisposable = new CompositeDisposable();

        detailsView = view;
        detailsView.setPresenter(this);
    }

    @Override
    public void subscribe()
    {
        openItem();
    }

    @Override
    public void unsubscribe()
    {
        compositeDisposable.clear();
    }

    @SuppressWarnings("Convert2MethodRef")
    private void openItem()
    {
        if (detailsView.isActive())
        {
            detailsView.setLoadingIndicator(true);
        }

        final GetItemUseCase.RequestParams params = new GetItemUseCase.RequestParams(itemId);

        compositeDisposable.clear();

        Disposable disposable = getItemUseCase
                .execute(params)
                .subscribe(
                        // onNext
                        resultItem -> updateViewWithItem(resultItem),
                        // onError
                        throwable -> updateViewWithError()
                );
        compositeDisposable.add(disposable);
    }

    private void updateViewWithItem(@NonNull Fruit fruit)
    {
        // Check if view is still able to handle UI updates
        if (!detailsView.isActive())
        {
            return;
        }

        detailsView.setLoadingIndicator(false);

        detailsView.showItemImage(fruit.getImageUrl());
        detailsView.showItemTitle(fruit.getTitle());
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
