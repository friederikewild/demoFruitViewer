package me.friederikewild.demo.fruits.domain.model;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable model class representing a Fruit entry.
 * For simplicity used in the domain layer as well as the view layer since identical.
 * Includes all available fields to make it easy to show more details on the view.
 * NOTE: If {@link date} was to be presented it could be converted into a Date object and formatted.
 */
@SuppressWarnings("SimplifiableIfStatement")
public class Fruit
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
    private String sourceProvider;
    @NonNull
    private String sourceUrl;
    @NonNull
    private String imageUrl;
    @NonNull
    private String imageCredits;

    /**
     * Special testing constructor for simplified test object creation
     * @param testString Test string for all string fields
     */
    @VisibleForTesting
    public Fruit(@NonNull String testString)
    {
        this.id = testString;
        this.title = testString;
        this.description = testString;
        this.date = testString;
        this.tags = new ArrayList<>();
        this.sourceProvider = testString;
        this.sourceUrl = "sourceUrl" + testString;
        this.imageUrl = testString;
        this.imageCredits = testString;
    }

    public Fruit(@NonNull String id, @NonNull String title, @NonNull String description,
                 @NonNull String date, @NonNull List<String> tags,
                 @NonNull String sourceProvider, @NonNull String sourceUrl,
                 @NonNull String imageUrl, @NonNull String imageCredits)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.tags = tags;
        this.sourceProvider = sourceProvider;
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
        this.imageCredits = imageCredits;
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
    public String getSourceProvider()
    {
        return sourceProvider;
    }

    @NonNull
    public String getSourceUrl()
    {
        return sourceUrl;
    }

    @NonNull
    public String getImageUrl()
    {
        return imageUrl;
    }

    @NonNull
    public String getImageCredits()
    {
        return imageCredits;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Fruit))
        {
            return false;
        }

        Fruit fruit = (Fruit) o;

        if (!id.equals(fruit.id))
        {
            return false;
        }
        if (!title.equals(fruit.title))
        {
            return false;
        }
        if (!description.equals(fruit.description))
        {
            return false;
        }
        if (!date.equals(fruit.date))
        {
            return false;
        }
        if (!tags.equals(fruit.tags))
        {
            return false;
        }
        if (!sourceProvider.equals(fruit.sourceProvider))
        {
            return false;
        }
        if (!sourceUrl.equals(fruit.sourceUrl))
        {
            return false;
        }
        if (!imageUrl.equals(fruit.imageUrl))
        {
            return false;
        }
        return imageCredits.equals(fruit.imageCredits);
    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + tags.hashCode();
        result = 31 * result + sourceProvider.hashCode();
        result = 31 * result + sourceUrl.hashCode();
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + imageCredits.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Fruit{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
