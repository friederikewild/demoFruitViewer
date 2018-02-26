package me.friederikewild.demo.fruits;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import me.friederikewild.demo.fruits.util.Bundler;

/**
 * Saving {@link Serializable} objects in a map for testing purposes
 */
public class TestSerializableBundler implements Bundler<Serializable>
{
    private Map<String, Serializable> storage;

    public TestSerializableBundler()
    {
        storage = new HashMap<>();
    }

    @VisibleForTesting
    public boolean isStorageEmpty()
    {
        return storage.isEmpty();
    }

    @Override
    public void put(@NonNull Bundle bundle, @NonNull String key, @Nullable Serializable value)
    {
        storage.put(key, value);
    }

    @Nullable
    @Override
    public Serializable get(@Nullable Bundle bundle, @NonNull String key)
    {
        return getSerializableOrNull(key);
    }

    @NonNull
    @Override
    public Serializable get(@Nullable Bundle bundle, @NonNull String key, @NonNull Serializable defaultValue)
    {
        Serializable loadedSerializable = getSerializableOrNull(key);

        return loadedSerializable == null ? defaultValue : loadedSerializable;
    }

    @Nullable
    private Serializable getSerializableOrNull(@NonNull String key)
    {
        Serializable loadedSerializable = null;

        if (storage.containsKey(key))
        {
            loadedSerializable = storage.get(key);
        }
        return loadedSerializable;
    }
}
