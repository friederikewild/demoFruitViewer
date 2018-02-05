package me.friederikewild.demo.touchnote.overview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.friederikewild.demo.touchnote.R;
import me.friederikewild.demo.touchnote.domain.model.Item;
import timber.log.Timber;

import static me.friederikewild.demo.touchnote.overview.OverviewLayoutType.GRID_LAYOUT;
import static me.friederikewild.demo.touchnote.overview.OverviewLayoutType.LIST_LAYOUT;

public class OverviewFragment extends Fragment implements OverviewContract.View
{
    private OverviewContract.Presenter presenter;

    private ItemsAdapter itemsAdapter;

    private RecyclerView.LayoutManager currentLayoutManager;
    private RecyclerView recyclerView;
    private TextView hintNoItemsTextView;

    public OverviewFragment()
    {
        // Empty
    }

    public static OverviewFragment newInstance()
    {
        return new OverviewFragment();
    }

    @Override
    public void setPresenter(@NonNull OverviewContract.Presenter presenter)
    {
        this.presenter = presenter;
    }

    //region [Fragment LifeCycle]
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
        hintNoItemsTextView = rootView.findViewById(R.id.overviewHintNoItems);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        EnhancedSwipeRefreshLayout refreshLayout = view.findViewById(R.id.overviewRefreshLayout);
        setupRefreshLayout(refreshLayout);

        setHasOptionsMenu(true);
    }

    private void setupRefreshLayout(@NonNull final EnhancedSwipeRefreshLayout refreshLayout)
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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);
        presenter.loadStateFromBundle(savedInstanceState);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        presenter.saveStateToBundle(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        recyclerView = null;
        hintNoItemsTextView = null;
        currentLayoutManager = null;
        presenter = null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        itemsAdapter = null;
    }
    //endregion

    //region [OptionMenu Handling]
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.overview_fragment_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);

        // Set visible layout option according to presenter
        menu.findItem(R.id.menu_show_layout_list).setVisible(presenter.isListLayoutOptionAvailable());
        menu.findItem(R.id.menu_show_layout_grid).setVisible(presenter.isGridLayoutOptionAvailable());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_show_layout_list:
                presenter.setLayoutPresentation(LIST_LAYOUT);
                break;
            case R.id.menu_show_layout_grid:
                presenter.setLayoutPresentation(GRID_LAYOUT);
                break;
        }
        return true;
    }
    //endregion

    //region [OverviewContract View]
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
    public void updateMenuItemVisibility()
    {
        // Request update of menu to toggle icon
        ActivityCompat.invalidateOptionsMenu(getActivity());
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

    @Override
    public void setListLayout()
    {
        itemsAdapter.setLayoutType(LIST_LAYOUT);

        final RecyclerView.LayoutManager newLayoutManager = new LinearLayoutManager(getActivity());
        setLayoutManagerToRecyclerView(newLayoutManager);
    }

    @Override
    public void setGridLayout()
    {
        itemsAdapter.setLayoutType(GRID_LAYOUT);

        final RecyclerView.LayoutManager newLayoutManager = new GridLayoutManager(getActivity(),
                                                                                  getRowCount());
        setLayoutManagerToRecyclerView(newLayoutManager);
    }

    private int getRowCount()
    {
        // Basic adjustment based on orientation. Advanced version could calculate optimal count from screen width
        return getActivity().getResources().getInteger(R.integer.overview_grid_row_count);
    }

    private void setLayoutManagerToRecyclerView(@NonNull RecyclerView.LayoutManager newLayoutManager)
    {
        // Get scroll position in case LayoutManager was set before
        int scrollPosition = getScrollPosition(recyclerView.getLayoutManager());

        currentLayoutManager = newLayoutManager;
        recyclerView.setLayoutManager(currentLayoutManager);

        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setHasFixedSize(true);

        // Set previous scroll position
        recyclerView.scrollToPosition(scrollPosition);
    }

    private int getScrollPosition(@Nullable RecyclerView.LayoutManager layoutManager)
    {
        int scrollPosition = 0;

        if (layoutManager != null)
        {
            // Works for both since GridLayoutManager extends LinearLayoutManager
            if (layoutManager instanceof LinearLayoutManager)
            {
                scrollPosition = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            }
        }

        return scrollPosition;
    }
    //endregion
}
