package me.friederikewild.demo.touchnote.overview;

import android.support.annotation.NonNull;

import java.util.List;

import me.friederikewild.demo.touchnote.BasePresenter;
import me.friederikewild.demo.touchnote.BaseView;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Contract between view and presenter for the overview screen.
 */
public interface OverviewContract
{
    interface View extends BaseView<Presenter>
    {
        boolean isActive();

        void setLoadingIndicator(boolean active);

        void updateMenuItemVisibility();

        void showItems(@NonNull List<Item> items);

        void showNoItemsAvailable();

        void showLoadingItemsError();

        void setListLayout();

        void setGridLayout();
    }

    interface Presenter extends BasePresenter
    {
        void loadItems(boolean forceUpdate);

        void setLayoutPresentation(@NonNull OverviewLayoutType layoutType);

        boolean isListOptionAvailable();

        boolean isGridOptionAvailable();
    }
}
