package friederikewild.me.demo.recyclerview.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import friederikewild.me.demo.recyclerview.R;

public class OverviewActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Setup toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        OverviewFragment overviewFragment = (OverviewFragment) fragmentManager.findFragmentById(
                R.id.contentFrame);
        if (overviewFragment == null)
        {
            // Create new instance of fragment
            overviewFragment = OverviewFragment.newInstance();
            addFragmentToActivity(fragmentManager, overviewFragment, R.id.contentFrame);
        }
    }

    public void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                      @NonNull Fragment fragment, int frameId)
    {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
