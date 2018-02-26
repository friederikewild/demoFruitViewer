package me.friederikewild.demo.fruits.presentation.overview;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import me.friederikewild.demo.fruits.R;

/**
 * Define the possible overview layout styles.
 */
enum OverviewLayoutType
{
    /**
     * Show items as list with picture, title and description
     */
    LIST_LAYOUT,

    /**
     * Show items as grid with picture and title
     */
    GRID_LAYOUT,

    INVALID_TYPE;

    /**
     * Receive a stable unique id per type.
     * Can be used as Adapter.ViewType
     * @return Resource id for current type
     */
    @IdRes
    public int getUniqueId()
    {
        switch (this)
        {
            case LIST_LAYOUT:
                return R.id.overview_view_type_list;
            case GRID_LAYOUT:
                return R.id.overview_view_type_grid;
            default:
                return R.id.overview_view_type_invalid;
        }
    }

    /**
     * Create layout type from a {@link #ordinal()}.
     *
     * @param ordinal
     * @return
     */
    @NonNull
    public static OverviewLayoutType fromOrdinal(int ordinal)
    {
        if (ordinal >= values().length)
        {
            return INVALID_TYPE;
        }
        return values()[ordinal];
    }
}
