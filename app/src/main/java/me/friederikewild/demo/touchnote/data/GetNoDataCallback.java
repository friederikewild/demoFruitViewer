package me.friederikewild.demo.touchnote.data;

/**
 * Common callback for no data / error
 */
public interface GetNoDataCallback
{
    /**
     * Callback method when no data was available for the linked request
     */
    void onNoDataAvailable();
}
