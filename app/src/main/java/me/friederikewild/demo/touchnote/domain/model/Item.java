package me.friederikewild.demo.touchnote.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable model class representing an Item.
 * For simplicity used in the domain layer as well as the view layer since identical.
 * Includes all available fields to make it easy to show more details on the view.
 * NOTE: If {@link date} was to be presented it could be converted into a Date object and formatted.
 */
@SuppressWarnings("SimplifiableIfStatement")
public class Item
{
    @NonNull
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String date;
    @NonNull
    private List<String> tags;
    @NonNull
    private String imageUrl;

    /**
     * Special testing constructor for simplified test object creation
     * @param testString Test string for all string fields
     */
    @VisibleForTesting
    public Item(@NonNull String testString)
    {
        this.id = testString;
        this.title = testString;
        this.description = testString;
        this.date = testString;
        this.tags = new ArrayList<>();
        this.imageUrl = testString;
    }

    public Item(@NonNull String id, @NonNull String title, @NonNull String description, @NonNull String date,
                @NonNull List<String> tags, @NonNull String imageUrl)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.tags = tags;
        this.imageUrl = imageUrl;
    }

    @NonNull
    public String getId()
    {
        return id;
    }

    @NonNull
    public String getTitle()
    {
        return title;
    }

    @NonNull
    public String getDescription()
    {
        return description;
    }

    @NonNull
    public String getDate()
    {
        return date;
    }

    @NonNull
    public List<String> getTags()
    {
        return tags;
    }

    @NonNull
    public String getImageUrl()
    {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Item))
        {
            return false;
        }

        Item item = (Item) o;

        if (!id.equals(item.id))
        {
            return false;
        }
        if (!title.equals(item.title))
        {
            return false;
        }
        if (!description.equals(item.description))
        {
            return false;
        }
        if (!date.equals(item.date))
        {
            return false;
        }
        if (!tags.equals(item.tags))
        {
            return false;
        }
        return imageUrl.equals(item.imageUrl);
    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + tags.hashCode();
        result = 31 * result + imageUrl.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Item{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
