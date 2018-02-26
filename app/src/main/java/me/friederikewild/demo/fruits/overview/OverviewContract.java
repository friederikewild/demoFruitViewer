package me.friederikewild.demo.fruits.overview;

import android.support.annotation.NonNull;

import java.util.List;

import me.friederikewild.demo.fruits.BaseView;
import me.friederikewild.demo.fruits.SavableBasePresenter;
import me.friederikewild.demo.fruits.domain.model.Item;

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

        void onItemClicked(@NonNull Item item);

        void setLayoutPresentation(@NonNull OverviewLayoutType layoutType);

        boolean isListLayoutOptionAvailable();

        boolean isGridLayoutOptionAvailable();

        int getRequestCodeForDetail();

        void onReturnFromRequest(int requestCode);
    }
}
