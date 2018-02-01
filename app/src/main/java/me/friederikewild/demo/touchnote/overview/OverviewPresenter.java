package me.friederikewild.demo.touchnote.overview;

import android.support.annotation.NonNull;

public class OverviewPresenter implements OverviewContract.Presenter
{
    OverviewContract.View overviewView;

    public OverviewPresenter(@NonNull OverviewContract.View view)
    {
        overviewView = view;
        overviewView.setPresenter(this);
    }

    @Override
    public void start()
    {
        // TODO: Start loading
    }
}
