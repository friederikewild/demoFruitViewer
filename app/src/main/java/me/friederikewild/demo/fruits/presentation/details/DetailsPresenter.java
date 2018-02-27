package me.friederikewild.demo.fruits.presentation.details;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.domain.usecase.GetFruitUseCase;

public class DetailsPresenter implements DetailsContract.Presenter
{
    private DetailsContract.View detailsView;

    @NonNull
    private GetFruitUseCase getFruitUseCase;

    @NonNull
    private CompositeDisposable compositeDisposable;

    @NonNull
    private final String fruitId;

    DetailsPresenter(@NonNull final DetailsContract.View view,
                     @NonNull final String id,
                     @NonNull final GetFruitUseCase getFruit)
    {
        fruitId = id;
        getFruitUseCase = getFruit;

        compositeDisposable = new CompositeDisposable();

        detailsView = view;
        detailsView.setPresenter(this);
    }

    @Override
    public void subscribe()
    {
        openFruitItem();
    }

    @Override
    public void unsubscribe()
    {
        compositeDisposable.clear();
    }

    @SuppressWarnings("Convert2MethodRef")
    private void openFruitItem()
    {
        if (detailsView.isActive())
        {
            detailsView.setLoadingIndicator(true);
        }

        final GetFruitUseCase.RequestParams params = new GetFruitUseCase.RequestParams(fruitId);

        compositeDisposable.clear();

        Disposable disposable = getFruitUseCase
                .execute(params)
                .subscribe(
                        // onNext
                        resultFruit -> updateViewWithFruit(resultFruit),
                        // onError
                        throwable -> updateViewWithError()
                );
        compositeDisposable.add(disposable);
    }

    private void updateViewWithFruit(@NonNull Fruit fruit)
    {
        // Check if view is still able to handle UI updates
        if (!detailsView.isActive())
        {
            return;
        }

        detailsView.setLoadingIndicator(false);

        detailsView.showFruitImage(fruit.getImageUrl());
        detailsView.showFruitTitle(fruit.getTitle());
    }

    private void updateViewWithError()
    {
        // Check if view is still able to handle UI updates
        if (!detailsView.isActive())
        {
            return;
        }

        detailsView.setLoadingIndicator(false);

        detailsView.showLoadingFruitError();
    }
}
