package me.friederikewild.demo.fruits.presentation.overview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.base.Strings;

import java.util.List;

import me.friederikewild.demo.fruits.BuildConfig;
import me.friederikewild.demo.fruits.R;
import me.friederikewild.demo.fruits.domain.model.Fruit;
import me.friederikewild.demo.fruits.util.GlideApp;
import timber.log.Timber;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.circleCropTransform;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * RecycleView adapter handling {@link Fruit}.
 * Layout of elements is different for list and grid.
 * Look can be switched with {@link #setLayoutType(OverviewLayoutType)}.
 * Internally using two different ViewType to represent the different look when preparing the views.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>
{
    private List<Fruit> fruits;
    @NonNull
    private ItemClickListener itemClickListener;

    @IdRes
    private int currentViewType = OverviewLayoutType.INVALID_TYPE.getUniqueId();

    ItemsAdapter(@NonNull List<Fruit> fruits,
                 @NonNull ItemClickListener listener)
    {
        itemClickListener = listener;
        setList(fruits);

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position)
    {
        // Simplified usage instead of converting the item.id
        // Data set is currently read only and always the same size once received
        return position;
    }

    void replaceData(@NonNull List<Fruit> fruits)
    {
        setList(fruits);
        notifyDataSetChanged();
    }

    private void setList(@NonNull List<Fruit> fruits)
    {
        this.fruits = checkNotNull(fruits);
    }

    @Override
    public int getItemViewType(int position)
    {
        assertCurrentViewTypeIsValid();

        // All fruits always have the same viewType
        return currentViewType;
    }

    private void assertCurrentViewTypeIsValid()
    {
        if (currentViewType == OverviewLayoutType.INVALID_TYPE.getUniqueId())
        {
            final IllegalStateException exception = new IllegalStateException(
                    "ItemsAdapter needs initial configuration of current LayoutType before usage!");
            if (BuildConfig.DEBUG)
            {
                throw exception;
            }
            else
            {
                Timber.e(exception);
            }
        }
    }

    /**
     * Change the layout style of the adapter between list and grid
     *
     * @param layoutType Requested display style
     */
    void setLayoutType(@NonNull OverviewLayoutType layoutType)
    {
        currentViewType = layoutType.getUniqueId();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @IdRes int viewType)
    {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(getItemLayoutForViewType(viewType), parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        final @IdRes int viewType = getItemViewType(position);
        final Fruit fruit = getItem(position);

        bindImageView(holder, fruit, viewType);
        bindTitleView(holder, fruit);
        bindDescriptionView(holder, fruit);

        // Make full card clickable
        holder.rootView.setOnClickListener(view -> itemClickListener.onItemClicked(fruit));
    }

    private void bindImageView(@NonNull ViewHolder holder, @NonNull Fruit fruit, @IdRes int viewType)
    {
        final Context context = holder.rootView.getContext();
        final @DrawableRes int placeholderRes = getPlaceholderForViewType(viewType);

        RequestBuilder<Drawable> imageLoaderRequest = GlideApp.with(context)
                .load(fruit.getImageUrl())
                .apply(new RequestOptions().placeholder(placeholderRes))
                .transition(withCrossFade());

        if (viewType == R.id.overview_view_type_list)
        {
            imageLoaderRequest = imageLoaderRequest.apply(circleCropTransform().circleCrop());
        }

        imageLoaderRequest.into(holder.imageView);
    }

    private void bindTitleView(@NonNull ViewHolder holder, @NonNull Fruit fruit)
    {
        holder.titleTextView.setText(fruit.getTitle());
    }

    private void bindDescriptionView(@NonNull ViewHolder holder, @NonNull Fruit fruit)
    {
        // Description view is optional and not available in grid mode
        if (holder.descriptionTextView != null)
        {
            // Description can be empty, hide view in that case
            boolean isDescriptionEmpty = Strings.isNullOrEmpty(fruit.getDescription());
            holder.descriptionTextView.setVisibility(isDescriptionEmpty ? View.GONE : View.VISIBLE);
            if (!isDescriptionEmpty)
            {
                holder.descriptionTextView.setText(fruit.getDescription());
            }
        }
    }

    @LayoutRes
    private int getItemLayoutForViewType(@IdRes int viewType)
    {
        switch (viewType)
        {
            case R.id.overview_view_type_grid:
                return R.layout.item_col_layout_grid;

            case R.id.overview_view_type_list:
            default:
                return R.layout.item_col_layout_list;
        }
    }

    @DrawableRes
    private int getPlaceholderForViewType(@IdRes int viewType)
    {
        switch (viewType)
        {
            case R.id.overview_view_type_grid:
                return R.drawable.placeholder_image_square_primary;

            case R.id.overview_view_type_list:
            default:
                return R.drawable.placeholder_thumbnail_circle_primary;
        }
    }

    @NonNull
    private Fruit getItem(int position)
    {
        return fruits.get(position);
    }

    @Override
    public int getItemCount()
    {
        return fruits.size();
    }

    /**
     * View Holder per item
     */
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        final View rootView;
        final TextView titleTextView;
        final TextView descriptionTextView;
        final ImageView imageView;

        ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            rootView = itemView;
            titleTextView = itemView.findViewById(R.id.overviewItemTitle);
            descriptionTextView = itemView.findViewById(R.id.overviewItemDescriptionText);
            imageView = itemView.findViewById(R.id.overviewItemImage);
        }
    }
}
