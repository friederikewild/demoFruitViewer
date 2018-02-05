package me.friederikewild.demo.touchnote.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Wrapper for handling saving/loading Android {@link Bundle}s.
 */
public interface Bundler<T>
{
    void put(@NonNull Bundle bundle, @NonNull String key, @Nullable T value);

    @Nullable
    T get(@Nullable Bundle bundle, @NonNull String key);

    @NonNull
    T get(@Nullable Bundle bundle, @NonNull String key, @NonNull T defaultValue);
}
