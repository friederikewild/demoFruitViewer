package friederikewild.me.demo.recyclerview.overview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import friederikewild.me.demo.recyclerview.R;

public class OverviewFragment extends Fragment
{
    public static OverviewFragment newInstance()
    {
        return new OverviewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_overview, container, false);

        final RecyclerView recyclerView = root.findViewById(R.id.itemsList);

        final EnhancedSwipeRefreshLayout refreshLayout = root.findViewById(R.id.refreshLayout);
        refreshLayout.setScrollableChildView(recyclerView);
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark),
                ContextCompat.getColor(getActivity(), R.color.colorAccent)
        );

        // TODO: This will be controlled by the presenter
        refreshLayout.setRefreshing(true);

        return root;
    }
}
