package me.friederikewild.demo.fruits.data.entity;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Fruit Entity representation used in the data layer.
 *
 * Generated from json using http://www.jsonschema2pojo.org/
 */
public class FruitEntity
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
    @SerializedName("sourceProvider")
    @Expose
    private String sourceProvider;
    @SerializedName("imageId")
    @Expose
    private String imageId;
    @SerializedName("imageProvider")
    @Expose
    private String imageProvider;


    public FruitEntity()
    {
        // Nothing
    }

    /**
     * Special testing constructor for simplified test object creation
     *
     * @param testString Test string for all string fields
     */
    @VisibleForTesting
    public FruitEntity(@NonNull String testString)
    {
        this.id = testString;
        this.title = testString;
        this.description = testString;
        this.date = testString;
        this.tags = new ArrayList<>();
        this.sourceProvider = testString;
        this.imageId = testString;
        this.imageProvider = testString;
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

    public String getSourceProvider()
    {
        return sourceProvider;
    }

    public void setSourceProvider(String sourceProvider)
    {
        this.sourceProvider = sourceProvider;
    }

    public String getImageId()
    {
        return imageId;
    }

    public void setImageId(String imageId)
    {
        this.imageId = imageId;
    }

    public String getImageProvider()
    {
        return imageProvider;
    }

    public void setImageProvider(String imageProvider)
    {
        this.imageProvider = imageProvider;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof FruitEntity))
        {
            return false;
        }

        FruitEntity that = (FruitEntity) o;

        if (!id.equals(that.id))
        {
            return false;
        }
        if (!title.equals(that.title))
        {
            return false;
        }
        if (!description.equals(that.description))
        {
            return false;
        }
        if (!date.equals(that.date))
        {
            return false;
        }
        if (!tags.equals(that.tags))
        {
            return false;
        }
        if (!sourceProvider.equals(that.sourceProvider))
        {
            return false;
        }
        if (!imageId.equals(that.imageId))
        {
            return false;
        }
        return imageProvider.equals(that.imageProvider);
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
        result = 31 * result + imageId.hashCode();
        result = 31 * result + imageProvider.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "FruitEntity{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
