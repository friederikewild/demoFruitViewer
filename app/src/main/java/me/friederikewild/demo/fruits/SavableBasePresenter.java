package me.friederikewild.demo.fruits;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Extended base presenter that can be saved and restored using an existing {@link Bundle}.
 */
public interface SavableBasePresenter extends BasePresenter
{
    void saveStateToBundle(@NonNull Bundle outState);

    void loadStateFromBundle(@Nullable Bundle savedState);
}
