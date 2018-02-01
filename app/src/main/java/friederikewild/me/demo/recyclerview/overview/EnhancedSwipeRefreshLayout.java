package friederikewild.me.demo.recyclerview.overview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Extension of {@link SwipeRefreshLayout} which supports having the scrolling view as any child.
 * Same usage, but needs configuration via {@link #setScrollableChildView(View)} to define which child view
 * needs checking on scrolling to differentiate between pull-to-refresh and scrolling.
 */
public class EnhancedSwipeRefreshLayout extends SwipeRefreshLayout
{
    private View scrollableChildView;

    public EnhancedSwipeRefreshLayout(Context context)
    {
        super(context);
    }

    public EnhancedSwipeRefreshLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp()
    {
        if (scrollableChildView != null)
        {
            return scrollableChildView.canScrollVertically(-1);
        }
        return super.canChildScrollUp();
    }

    public void setScrollableChildView(View view)
    {
        scrollableChildView = view;
    }
}
