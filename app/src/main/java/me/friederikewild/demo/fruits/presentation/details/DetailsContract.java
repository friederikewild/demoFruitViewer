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

        void hideImageCredits();

        void showImageCredits(@NonNull String imageCredits);

        void showFruitImage(@NonNull String imageUrl);

        void showFruitTitle(@NonNull String title);

        void showFruitDescription(@NonNull String description);

        void showFruitSourceProvider(@NonNull String provider);

        void showFruitMoreLink(@NonNull String sourceUrl);

        void showImageCreditsDialog(@NonNull String imageCredits);

        void showMoreView(@NonNull String moreUrl);

        void showLoadingFruitError();
    }

    interface Presenter extends BasePresenter
    {
        void onImageCreditsInfoClicked(@NonNull String imageCredits);

        void onMoreActionClicked(@NonNull String sourceUrl);
    }
}
