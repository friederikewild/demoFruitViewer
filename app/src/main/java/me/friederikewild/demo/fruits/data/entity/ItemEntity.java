package me.friederikewild.demo.fruits.data.entity;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Item Entity used in the data layer
 */
@SuppressWarnings("SimplifiableIfStatement")
public class ItemEntity
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("image")
    @Expose
    private String imageUrl;

    public ItemEntity()
    {
        // Nothing
    }

    /**
     * Special testing constructor for simplified test object creation
     *
     * @param testString Test string for all string fields
     */
    @VisibleForTesting
    public ItemEntity(@NonNull String testString)
    {
        this.id = testString;
        this.title = testString;
        this.description = testString;
        this.date = testString;
        this.tags = new ArrayList<>();
        this.imageUrl = testString;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public List<String> getTags()
    {
        return tags;
    }

    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof ItemEntity))
        {
            return false;
        }

        ItemEntity entity = (ItemEntity) o;

        if (!id.equals(entity.id))
        {
            return false;
        }
        if (!title.equals(entity.title))
        {
            return false;
        }
        if (!description.equals(entity.description))
        {
            return false;
        }
        if (!date.equals(entity.date))
        {
            return false;
        }
        if (!tags.equals(entity.tags))
        {
            return false;
        }
        return imageUrl.equals(entity.imageUrl);
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
        return "ItemEntity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
