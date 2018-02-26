package me.friederikewild.demo.fruits.presentation.overview;

import android.support.annotation.NonNull;

import me.friederikewild.demo.fruits.ActivityWithOneFragment;
import me.friederikewild.demo.fruits.util.Injection;

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
                Injection.provideGetFruitsUseCase(),
                Injection.provideSerializableBundler()
        );
    }
}
