package me.friederikewild.demo.touchnote.data.entity.mapper;

import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

/**
 * Facade wrapper for {@link android.text.Html} calls to get current time.
 * This allows for easy mocking during tests.
 *
 * NOTE: Current mock server response doesn't actually contain html elements.
 * Created this setup remembering it contained line breaks, keeping it now since working.
 *
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
    public String formatHtml(@NonNull final String html)
    {
        // Also support line breaks in ascii form
        final String convertLineBreaks = html.replace("\n", "<br />");

        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        {
            result = Html.fromHtml(convertLineBreaks, Html.FROM_HTML_MODE_COMPACT);
        }
        else
        {
            result = Html.fromHtml(convertLineBreaks);
        }

        return result.toString();
    }
}
