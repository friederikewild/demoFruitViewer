package me.friederikewild.demo.fruits.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import me.friederikewild.demo.fruits.ActivityWithOneFragment;
import me.friederikewild.demo.fruits.util.Injection;

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

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            // Finish Activity since called with forResult.

            Intent result = new Intent((String) null);
            setResult(RESULT_OK, result);
            finish();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
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
                                    Injection.provideGetFruitUseCase());
    }
}
