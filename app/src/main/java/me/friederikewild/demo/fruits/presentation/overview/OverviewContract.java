package me.friederikewild.demo.fruits.presentation.overview;

import android.support.annotation.NonNull;

import java.util.List;

import me.friederikewild.demo.fruits.BaseView;
import me.friederikewild.demo.fruits.SavableBasePresenter;
import me.friederikewild.demo.fruits.domain.model.Fruit;

/**
 * Contract between view and presenter for the overview screen.
 */
public interface OverviewContract
{
    interface View extends BaseView<Presenter>
    {
        void setLoadingIndicator(boolean active);

        void updateMenuItemVisibility();

        void showFruits(@NonNull List<Fruit> fruits);

        void showDetailsForFruit(@NonNull String fruitId);

        void showMoreView(@NonNull String moreUrl);

        void showNoFruitsAvailable();

        void showLoadingFruitsError();

        void setListLayout();

        void setGridLayout();
    }

    interface Presenter extends SavableBasePresenter
    {
        void loadFruits(boolean forceUpdate);

        void onFruitItemClicked(@NonNull Fruit fruit);

        void onFruitActionMore(@NonNull Fruit fruit);

        void setLayoutPresentation(@NonNull OverviewLayoutType layoutType);

        boolean isListLayoutOptionAvailable();

        boolean isGridLayoutOptionAvailable();

        int getRequestCodeForDetail();

        void onReturnFromRequest(int requestCode);
    }
}
