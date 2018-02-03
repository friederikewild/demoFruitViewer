package me.friederikewild.demo.touchnote.overview;

import me.friederikewild.demo.touchnote.BasePresenter;
import me.friederikewild.demo.touchnote.BaseView;

/**
 * Contract between view and presenter for the overview screen.
 */
public interface OverviewContract
{
    interface View extends BaseView<Presenter>
    {
        boolean isActive();

        void setLoadingIndicator(boolean active);
    }

    interface Presenter extends BasePresenter
    {
        void loadItems(boolean forceUpdate);
    }
}
