package me.friederikewild.demo.touchnote.details;

import me.friederikewild.demo.touchnote.BasePresenter;
import me.friederikewild.demo.touchnote.BaseView;

/**
 * Contract between view and presenter for the details screen.
 */
public class DetailsContract
{
    interface View extends BaseView<Presenter>
    {
        void setLoadingIndicator(boolean active);


    }

    interface Presenter extends BasePresenter
    {
    }
}
