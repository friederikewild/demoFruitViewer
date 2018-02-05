package me.friederikewild.demo.touchnote.details;

import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.ActivityWithOneFragment;

public class DetailsActivity extends ActivityWithOneFragment<DetailsFragment, DetailsContract.Presenter>
{
    @NonNull
    @Override
    public DetailsFragment createContentFragment()
    {
        return DetailsFragment.newInstance();
    }

    @NonNull
    @Override
    public DetailsContract.Presenter createPresenter(@NonNull DetailsFragment fragment)
    {
        return new DetailsPresenter(fragment);
    }
}
