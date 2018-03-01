package me.friederikewild.demo.fruits.presentation.details;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.google.common.base.Strings;

import me.friederikewild.demo.fruits.R;
import me.friederikewild.demo.fruits.util.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class DetailsFragment extends Fragment implements DetailsContract.View
{
    private DetailsContract.Presenter presenter;

    private View loadingSpinner;
    private ImageView fruitImageView;
    private TextView descriptionTextView;
    private TextView sourceProviderTextView;
    private View moreActionView;
    private TextView hintNoDataTextView;
    private View imageCreditsAction;

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
        fruitImageView = rootView.findViewById(R.id.detailsSquareImage);
        descriptionTextView = rootView.findViewById(R.id.detailsFruitDescriptionText);
        sourceProviderTextView = rootView.findViewById(R.id.detailsFruitSourceProvider);
        moreActionView = rootView.findViewById(R.id.detailsFruitMoreAction);
        hintNoDataTextView = rootView.findViewById(R.id.detailsHintNoFruits);
        imageCreditsAction = rootView.findViewById(R.id.detailsImageCreditsAction);

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        presenter.unsubscribe();
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
    public void hideImageCredits()
    {
        imageCreditsAction.setVisibility(View.GONE);
    }

    @Override
    public void showImageCredits(@NonNull final String imageCredits)
    {
        imageCreditsAction.setVisibility(View.VISIBLE);
        imageCreditsAction.setOnClickListener(view -> presenter.onImageCreditsInfoClicked(imageCredits));
    }

    @Override
    public void showFruitImage(@NonNull String imageUrl)
    {
        // Hide error in case visible before
        hintNoDataTextView.setVisibility(View.GONE);

        GlideApp.with(getContext())
                .load(imageUrl)
                .apply(centerCropTransform().centerCrop())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder_image_square_primary))
                .transition(withCrossFade()).into(fruitImageView);
    }

    @Override
    public void showFruitTitle(@NonNull String title)
    {
        final ActionBar actionBar = ((DetailsActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setTitle(title);
        }
    }

    @Override
    public void showFruitDescription(@NonNull String description)
    {
        descriptionTextView.setText(description);
    }

    @Override
    public void showFruitSourceProvider(@NonNull String provider)
    {
        // Hide view if no source link provided
        boolean isSourceEmpty = Strings.isNullOrEmpty(provider);
        sourceProviderTextView.setVisibility(isSourceEmpty ? View.GONE : View.VISIBLE);
        if (!isSourceEmpty)
        {
            sourceProviderTextView.setText(
                    getResources().getString(R.string.fruit_source_provider, provider));
        }
    }

    @Override
    public void showFruitMoreLink(@NonNull String sourceUrl)
    {
        // Hide view if no source link provided
        boolean isSourceUrlEmpty = Strings.isNullOrEmpty(sourceUrl);
        moreActionView.setVisibility(isSourceUrlEmpty ? View.GONE : View.VISIBLE);
        if (!isSourceUrlEmpty)
        {
            moreActionView.setOnClickListener(
                    view -> presenter.onMoreActionClicked(sourceUrl));
        }
    }

    @Override
    public void showImageCreditsDialog(@NonNull String imageCredits)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(getResources().getString(R.string.details_dialog_photo_title));
        builder.setMessage(imageCredits);
        builder.setPositiveButton(getResources().getString(android.R.string.ok), null);
        builder.show();
    }

    @Override
    public void showMoreView(@NonNull String moreUrl)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(moreUrl));
        startActivity(i);
    }

    @Override
    public void showLoadingFruitError()
    {
        // Set background again to make sure we are in the expected state
        setImageViewBackground();

        hintNoDataTextView.setVisibility(View.VISIBLE);
    }

    private void setImageViewBackground()
    {
        @DrawableRes final int drawableRes = R.drawable.placeholder_image_square_primary;

        final Resources res = fruitImageView.getContext().getResources();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            fruitImageView.setImageDrawable(res.getDrawable(drawableRes));
        }
        else
        {
            fruitImageView.setImageDrawable(getResources().getDrawable(drawableRes));
        }
    }
}
