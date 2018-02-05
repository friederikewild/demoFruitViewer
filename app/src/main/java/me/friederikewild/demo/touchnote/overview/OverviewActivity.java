package me.friederikewild.demo.touchnote.overview;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import me.friederikewild.demo.touchnote.ActivityWithOneFragment;
import me.friederikewild.demo.touchnote.R;
import me.friederikewild.demo.touchnote.util.Injection;

public class OverviewActivity
        extends ActivityWithOneFragment<OverviewContract.View, OverviewContract.Presenter>
{
    @Override
    @NonNull
    public OverviewContract.View createContentFragment()
    {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        OverviewFragment overviewFragment = (OverviewFragment) fragmentManager.findFragmentById(
                R.id.contentFrame);
        if (overviewFragment == null)
        {
            // Create new instance of fragment
            overviewFragment = OverviewFragment.newInstance();
            addFragmentToActivity(fragmentManager, overviewFragment, R.id.contentFrame);
        }
        return overviewFragment;
    }

    @Override
    @NonNull
    public OverviewContract.Presenter createPresenter(@NonNull final OverviewContract.View fragment)
    {
        return new OverviewPresenter(
                fragment,
                Injection.provideUseCaseHandler(),
                Injection.provideGetItemsUseCase(),
                Injection.provideSerializableBundler()
        );
    }
}
