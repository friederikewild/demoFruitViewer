package me.friederikewild.demo.fruits.presentation.details;

import android.support.annotation.NonNull;

import me.friederikewild.demo.fruits.BasePresenter;
import me.friederikewild.demo.fruits.BaseView;

/**
 * Contract between view and presenter for the details screen.
 */
public class DetailsContract
{
    interface View extends BaseView<Presenter>
    {
        void setLoadingIndicator(boolean active);

        void showFruitImage(@NonNull String imageUrl);

        void showFruitTitle(@NonNull String title);

        void showLoadingFruitError();
    }

    interface Presenter extends BasePresenter
    {
        // Nothing atm
    }
}
