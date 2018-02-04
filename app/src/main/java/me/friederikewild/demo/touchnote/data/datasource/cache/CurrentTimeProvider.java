package me.friederikewild.demo.touchnote.data.datasource.cache;

/**
 * Facade wrapper for System call to get current time.
 * This allows for easy mocking during tests.
 * Setup as a singleton.
 */
public class CurrentTimeProvider
{
    private static CurrentTimeProvider INSTANCE;

    private CurrentTimeProvider()
    {
        // Empty
    }

    public static CurrentTimeProvider getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CurrentTimeProvider();
        }
        return INSTANCE;
    }

    public long getCurrentTimeMillis()
    {
        return System.currentTimeMillis();
    }
}
