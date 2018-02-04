package me.friederikewild.demo.touchnote.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.friederikewild.demo.touchnote.R;
import me.friederikewild.demo.touchnote.domain.model.Item;
import timber.log.Timber;

public class OverviewFragment extends Fragment implements OverviewContract.View
{
    private OverviewContract.Presenter presenter;

    private ItemsAdapter itemsAdapter;

    private RecyclerView.LayoutManager currentLayoutManager;
    private RecyclerView recyclerView;
    private TextView hintNoItemsTextView;

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
        itemsAdapter = new ItemsAdapter(new ArrayList<>());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_overview, container, false);

        recyclerView = rootView.findViewById(R.id.overviewItemsList);
        setupRecyclerView();

        EnhancedSwipeRefreshLayout refreshLayout = rootView.findViewById(R.id.overviewRefreshLayout);
        setupRefreshLayout(refreshLayout);

        hintNoItemsTextView = rootView.findViewById(R.id.overviewHintNoItems);

        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        recyclerView = null;
        hintNoItemsTextView = null;
    }

    private void setupRecyclerView()
    {
        // Use list style as default
        currentLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(currentLayoutManager);

        recyclerView.setAdapter(itemsAdapter);
    }

    private void setupRefreshLayout(EnhancedSwipeRefreshLayout refreshLayout)
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
        if (getView() == null)
        {
            return;
        }

        final EnhancedSwipeRefreshLayout refreshLayout = getView().findViewById(R.id.overviewRefreshLayout);

        // Ensure to call setRefreshing after layout is done
        refreshLayout.post(() -> refreshLayout.setRefreshing(active));
    }

    @Override
    public void showItems(@NonNull List<Item> items)
    {
        Timber.i("View - Show %d items %s", items.size(), items);

        itemsAdapter.replaceData(items);

        hintNoItemsTextView.setVisibility(View.GONE);

        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoItemsAvailable()
    {
        recyclerView.setVisibility(View.GONE);

        hintNoItemsTextView.setVisibility(View.VISIBLE);
        hintNoItemsTextView.setText(R.string.overview_hint_empty);
    }

    @Override
    public void showLoadingItemsError()
    {
        recyclerView.setVisibility(View.GONE);

        hintNoItemsTextView.setVisibility(View.VISIBLE);
        hintNoItemsTextView.setText(R.string.overview_hint_error);
    }
}
