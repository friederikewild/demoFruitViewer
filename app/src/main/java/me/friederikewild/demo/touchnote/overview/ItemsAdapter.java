package me.friederikewild.demo.touchnote.overview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.base.Strings;

import java.util.List;

import me.friederikewild.demo.touchnote.R;
import me.friederikewild.demo.touchnote.domain.model.Item;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.circleCropTransform;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * RecycleView adapter handling {@link me.friederikewild.demo.touchnote.domain.model.Item}
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>
{
    private List<Item> items;

    public ItemsAdapter(@NonNull List<Item> items)
    {
        setList(items);
    }

    public void replaceData(@NonNull List<Item> items)
    {
        setList(items);
        notifyDataSetChanged();
    }

    private void setList(@NonNull List<Item> items)
    {
        this.items = checkNotNull(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_col_layout_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final Item item = getItem(position);

        final Context context = holder.rootView.getContext();
        Glide.with(context)
                .load(item.getImageUrl())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder_thumbnail_circle_primary))
                .apply(circleCropTransform().circleCrop())
                .transition(withCrossFade())
                .into(holder.imageView);

        holder.titleTextView.setText(item.getTitle());

        // Description can be empty, hide view in that case
        boolean isDescriptionEmpty = Strings.isNullOrEmpty(item.getDescription());
        holder.descriptionTextView.setVisibility(isDescriptionEmpty ? View.GONE : View.VISIBLE);
        if (!isDescriptionEmpty)
        {
            holder.descriptionTextView.setText(item.getDescription());
        }
    }

    @NonNull
    private Item getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }

    /**
     * View Holder per item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        final View rootView;
        final TextView titleTextView;
        final TextView descriptionTextView;
        final ImageView imageView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            rootView = itemView;
            titleTextView = itemView.findViewById(R.id.overviewItemTitle);
            descriptionTextView = itemView.findViewById(R.id.overviewItemDescriptionText);
            imageView = itemView.findViewById(R.id.overviewItemImage);
        }
    }
}
