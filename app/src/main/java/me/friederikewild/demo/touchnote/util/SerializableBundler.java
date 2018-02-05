package me.friederikewild.demo.touchnote.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Concrete android {@link Bundler} to save/restore {@link Serializable} objects.
 */
public class SerializableBundler implements Bundler<Serializable>
{
    @Override
    public void put(@NonNull Bundle bundle, @NonNull String key, Serializable value)
    {
        bundle.putSerializable(key, value);
    }

    @Nullable
    @Override
    public Serializable get(@Nullable Bundle bundle, @NonNull String key)
    {
        return getSerializableOrNull(bundle, key);
    }

    @NonNull
    @Override
    public Serializable get(@Nullable Bundle bundle, @NonNull String key, @NonNull Serializable defaultValue)
    {
        Serializable loadedSerializable = getSerializableOrNull(bundle, key);

        return loadedSerializable == null ? defaultValue : loadedSerializable;
    }

    private Serializable getSerializableOrNull(@Nullable Bundle bundle, @NonNull String key)
    {
        Serializable loadedSerializable = null;
        if (bundle != null && bundle.containsKey(key))
        {
            loadedSerializable = bundle.getSerializable(key);
        }
        return loadedSerializable;
    }
}
