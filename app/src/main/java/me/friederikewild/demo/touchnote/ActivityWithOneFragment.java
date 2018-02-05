package me.friederikewild.demo.touchnote;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Common activity setup that can be shared between overview and detail screen.
 */
public abstract class ActivityWithOneFragment<V extends Fragment & BaseView, P extends BasePresenter>
        extends AppCompatActivity
{
    private P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_fragment);

        // Setup toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = setupPresenterWithView();
    }

    @NonNull
    private P setupPresenterWithView()
    {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        V viewFragment = findExistingContentFragment(fragmentManager);

        if (viewFragment == null)
        {
            // Create new instance of fragment
            viewFragment = createContentFragment();
            addFragmentToActivity(fragmentManager, viewFragment, R.id.contentFrame);
        }

        // Create presenter and provide the view
        return createPresenter(viewFragment);
    }

    @SuppressWarnings("unchecked")
    private V findExistingContentFragment(@NonNull final FragmentManager fragmentManager)
    {
        return (V) fragmentManager.findFragmentById(R.id.contentFrame);
    }

    @NonNull
    public abstract V createContentFragment();

    @NonNull
    public abstract P createPresenter(@NonNull V fragment);

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        presenter = null;
    }

    public void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                      @NonNull Fragment fragment, int frameId)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

}
