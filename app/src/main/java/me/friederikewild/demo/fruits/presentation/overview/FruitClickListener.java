package me.friederikewild.demo.fruits.presentation.overview;

import android.support.annotation.NonNull;

import me.friederikewild.demo.fruits.domain.model.Fruit;

public interface FruitClickListener
{
    void onFruitItemClicked(@NonNull Fruit fruit);

    void onMoreActionClicked(@NonNull Fruit fruit);
}
