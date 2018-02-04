package me.friederikewild.demo.touchnote.data.entity.mapper;

import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

/**
 * Facade wrapper for {@link android.text.Html} calls to get current time.
 * This allows for easy mocking during tests.
 * Setup as a singleton.
 */
public class HtmlStringFormatter
{
    private static HtmlStringFormatter INSTANCE;

    private HtmlStringFormatter()
    {
        // Empty
    }

    public static HtmlStringFormatter getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new HtmlStringFormatter();
        }
        return INSTANCE;
    }

    /**
     * Convert provided HTML string into displayable styled text.
     * Note: Only provides String representation. So image tags are not supported.
     *
     * @param html Provided HTML string
     * @return String representation with displayable styled text
     */
    @SuppressWarnings("deprecation")
    public String formatHtml(@NonNull String html)
    {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        {
            // also test with FROM_HTML_MODE_COMPACT
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        }
        else
        {
            result = Html.fromHtml(html);
        }

        return result.toString();
    }
}
