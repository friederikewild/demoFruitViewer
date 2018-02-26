package me.friederikewild.demo.fruits.details;

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

        void showItemImage(@NonNull String imageUrl);

        void showItemTitle(@NonNull String title);

        void showLoadingItemError();
    }

    interface Presenter extends BasePresenter
    {
        // Nothing atm
    }
}
