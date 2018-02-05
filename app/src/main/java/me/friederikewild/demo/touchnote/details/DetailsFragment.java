package me.friederikewild.demo.touchnote.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.friederikewild.demo.touchnote.R;

public class DetailsFragment extends Fragment implements DetailsContract.View
{
    private DetailsContract.Presenter presenter;

    private View loadingSpinner;
    private ImageView itemImageView;

    public DetailsFragment()
    {
        // Empty
    }

    public static DetailsFragment newInstance()
    {
        return new DetailsFragment();
    }

    @Override
    public void setPresenter(@NonNull DetailsContract.Presenter presenter)
    {
        this.presenter = presenter;
    }

    //region [Fragment LifeCycle]
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        loadingSpinner = rootView.findViewById(R.id.loadingSpinner);
        itemImageView = rootView.findViewById(R.id.detailsSquareImage);

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.start();
    }
    //endregion [Fragment LifeCycle]

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

        loadingSpinner.setVisibility(active ? View.VISIBLE : View.GONE);
        itemImageView.setVisibility(active ? View.GONE : View.VISIBLE);
    }
}
