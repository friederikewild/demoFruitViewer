package me.friederikewild.demo.touchnote.data.datasource.cache;

/**
 * Facade wrapper for System call to get current time.
 * This allows for easy mocking during tests.
 */
public class CurrentTimeProvider
{
    public long getCurrentTimeMillis()
    {
        return System.currentTimeMillis();
    }
}
