package me.friederikewild.demo.touchnote;

/**
 * Reusable base presenter that can subscribe and unsubscribe,
 * following its view lifecycle.
 */
public interface BasePresenter
{
    void subscribe();

    void unsubscribe();
}
