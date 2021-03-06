package me.friederikewild.demo.fruits;

/**
 * Reusable view interface linked to a matching presenter.
 * To be implemented by the Fragments.
 */
public interface BaseView<T extends BasePresenter>
{
    void setPresenter(T presenter);

    boolean isActive();
}
