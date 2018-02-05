package me.friederikewild.demo.touchnote.details;

import android.support.annotation.NonNull;

public class DetailsPresenter implements DetailsContract.Presenter
{
    private DetailsContract.View detailsView;

    DetailsPresenter(@NonNull final DetailsContract.View view)
    {
        detailsView = view;
        detailsView.setPresenter(this);
    }

    @Override
    public void start()
    {

    }
}
