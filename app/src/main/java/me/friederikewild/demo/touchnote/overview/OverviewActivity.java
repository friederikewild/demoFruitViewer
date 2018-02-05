package me.friederikewild.demo.touchnote.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.friederikewild.demo.touchnote.R;
import me.friederikewild.demo.touchnote.util.Injection;

public class OverviewActivity extends AppCompatActivity
{
    private OverviewContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_fragment);

        // Setup toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViewAndPresenter();
    }

    private void setupViewAndPresenter()
    {
        OverviewFragment overviewFragment = initContentFragment();
        presenter = createPresenter(overviewFragment);
    }

    @NonNull
    private OverviewFragment initContentFragment()
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

    @NonNull
    private OverviewPresenter createPresenter(OverviewFragment overviewFragment)
    {
        return new OverviewPresenter(
                overviewFragment,
                Injection.provideUseCaseHandler(),
                Injection.provideGetItemsUseCase(),
                Injection.provideSerializableBundler()
        );
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        deletePresenter();
    }

    private void deletePresenter()
    {
        presenter = null;
    }

    // TODO: Make reusable for detail activity
    private void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment, int frameId)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
