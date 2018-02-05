package me.friederikewild.demo.touchnote.overview;

import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.ActivityWithOneFragment;
import me.friederikewild.demo.touchnote.util.Injection;

public class OverviewActivity
        extends ActivityWithOneFragment<OverviewFragment, OverviewContract.Presenter>
{
    @Override
    @NonNull
    public OverviewFragment createContentFragment()
    {
        return OverviewFragment.newInstance();
    }

    @Override
    @NonNull
    public OverviewContract.Presenter createPresenter(@NonNull final OverviewFragment fragment)
    {
        return new OverviewPresenter(
                fragment,
                Injection.provideUseCaseHandler(),
                Injection.provideGetItemsUseCase(),
                Injection.provideSerializableBundler()
        );
    }
}
