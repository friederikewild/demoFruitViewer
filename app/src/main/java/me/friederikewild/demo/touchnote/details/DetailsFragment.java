package me.friederikewild.demo.touchnote.details;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import me.friederikewild.demo.touchnote.R;
import me.friederikewild.demo.touchnote.util.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class DetailsFragment extends Fragment implements DetailsContract.View
{
    private DetailsContract.Presenter presenter;

    private View loadingSpinner;
    private ImageView itemImageView;
    private TextView hintNoDataTextView;

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

        loadingSpinner = rootView.findViewById(R.id.detailsLoadingSpinner);
        itemImageView = rootView.findViewById(R.id.detailsSquareImage);
        hintNoDataTextView = rootView.findViewById(R.id.detailsHintNoItems);

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
    }

    @Override
    public void showItemImage(@NonNull String imageUrl)
    {
        // Hide error in case visible before
        hintNoDataTextView.setVisibility(View.GONE);

        GlideApp.with(getContext())
                .load(imageUrl)
                .apply(centerCropTransform().centerCrop())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder_image_square_primary))
                .transition(withCrossFade()).into(itemImageView);
    }

    @Override
    public void showItemTitle(@NonNull String title)
    {
        final ActionBar actionBar = ((DetailsActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
        {

            actionBar.setSubtitle(title);
        }
    }

    @Override
    public void showLoadingItemError()
    {
        // Set background again to make sure we are in the expected state
        setImageViewBackground();

        hintNoDataTextView.setVisibility(View.VISIBLE);
    }

    private void setImageViewBackground()
    {
        @DrawableRes final int drawableRes = R.drawable.placeholder_image_square_primary;

        final Resources res = itemImageView.getContext().getResources();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            itemImageView.setImageDrawable(res.getDrawable(drawableRes));
        }
        else
        {
            itemImageView.setImageDrawable(getResources().getDrawable(drawableRes));
        }
    }
}