package me.friederikewild.demo.touchnote.overview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.friederikewild.demo.touchnote.R;

/**
 * RecycleView adapter handling {@link me.friederikewild.demo.touchnote.domain.model.Item}
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>
{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // TODO:
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // TODO: Add full binding
    }

    @Override
    public int getItemCount()
    {
        // TODO: Return size of data
        return 0;
    }

    /**
     * View Holder per item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.overviewItemTitle);

            // TODO: Find all views
        }
    }
}
