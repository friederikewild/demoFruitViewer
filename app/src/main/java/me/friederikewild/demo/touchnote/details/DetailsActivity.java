package me.friederikewild.demo.touchnote.details;

import android.os.Bundle;
import android.support.annotation.NonNull;

import me.friederikewild.demo.touchnote.ActivityWithOneFragment;
import me.friederikewild.demo.touchnote.util.Injection;

public class DetailsActivity extends ActivityWithOneFragment<DetailsFragment, DetailsContract.Presenter>
{
    public static final String EXTRA_ITEM_ID = "ITEM_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

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
        // Get the requested task id
        final String itemId = getIntent().getStringExtra(EXTRA_ITEM_ID);

        return new DetailsPresenter(fragment,
                                    itemId,
                                    Injection.provideUseCaseHandler(),
                                    Injection.provideGetItemUseCase());
    }
}
