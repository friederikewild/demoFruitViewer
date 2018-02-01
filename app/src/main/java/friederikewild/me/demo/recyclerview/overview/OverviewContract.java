package friederikewild.me.demo.recyclerview.overview;

import friederikewild.me.demo.recyclerview.BasePresenter;
import friederikewild.me.demo.recyclerview.BaseView;

/**
 * Contract between view and presenter for the overview screen.
 */
public interface OverviewContract
{
    interface View extends BaseView<Presenter>
    {

    }

    interface Presenter extends BasePresenter
    {

    }
}
