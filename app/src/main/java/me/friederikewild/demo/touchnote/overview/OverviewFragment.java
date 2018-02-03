package me.friederikewild.demo.touchnote.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.friederikewild.demo.touchnote.R;

public class OverviewFragment extends Fragment implements OverviewContract.View
{
    private OverviewContract.Presenter presenter;

    private ItemsAdapter itemsAdapter;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager currentLayoutManager;

    private EnhancedSwipeRefreshLayout refreshLayout;

    public static OverviewFragment newInstance()
    {
        return new OverviewFragment();
    }

    @Override
    public void setPresenter(OverviewContract.Presenter presenter)
    {
        this.presenter = presenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        itemsAdapter = new ItemsAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        recyclerView = rootView.findViewById(R.id.itemsList);
        setupRecyclerView();

        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        setupRefreshLayout();

        return rootView;
    }

    private void setupRecyclerView()
    {
        // Use list style as default
        currentLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(currentLayoutManager);

        recyclerView.setAdapter(itemsAdapter);
    }

    private void setupRefreshLayout()
    {
        // Set the actual scrollable child view
        refreshLayout.setScrollableChildView(recyclerView);

        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark),
                ContextCompat.getColor(getActivity(), R.color.colorAccent)
        );

        refreshLayout.setOnRefreshListener(() -> presenter.loadItems(false));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.start();
    }

    @Override
    public boolean isActive()
    {
        return isAdded();
    }

    @Override
    public void setLoadingIndicator(boolean active)
    {
        if (refreshLayout != null)
        {
            refreshLayout.setRefreshing(true);
        }
    }
}
