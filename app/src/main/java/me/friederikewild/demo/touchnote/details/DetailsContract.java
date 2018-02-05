package me.friederikewild.demo.touchnote.details;

import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.BasePresenter;
import me.friederikewild.demo.touchnote.BaseView;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Contract between view and presenter for the details screen.
 */
public class DetailsContract
{
    interface View extends BaseView<Presenter>
    {
        void setLoadingIndicator(boolean active);

        void showItem(@NonNull Item item);

        void showLoadingItemError();
    }

    interface Presenter extends BasePresenter
    {

    }
}
