package me.friederikewild.demo.touchnote.overview;

import android.support.annotation.NonNull;

import java.util.List;

import me.friederikewild.demo.touchnote.BaseView;
import me.friederikewild.demo.touchnote.SavableBasePresenter;
import me.friederikewild.demo.touchnote.domain.model.Item;

/**
 * Contract between view and presenter for the overview screen.
 */
public interface OverviewContract
{
    interface View extends BaseView<Presenter>
    {
        void setLoadingIndicator(boolean active);

        void updateMenuItemVisibility();

        void showItems(@NonNull List<Item> items);

        void showDetailsForItem(@NonNull String itemId);

        void showNoItemsAvailable();

        void showLoadingItemsError();

        void setListLayout();

        void setGridLayout();
    }

    interface Presenter extends SavableBasePresenter
    {
        void loadItems(boolean forceUpdate);

        void setLayoutPresentation(@NonNull OverviewLayoutType layoutType);

        boolean isListLayoutOptionAvailable();

        boolean isGridLayoutOptionAvailable();
    }
}
