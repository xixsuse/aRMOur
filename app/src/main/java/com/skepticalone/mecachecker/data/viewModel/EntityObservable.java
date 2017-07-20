package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.Nullable;

import java.util.Observable;

public final class EntityObservable<Entity> extends Observable {

    @Nullable
    private Entity item;

    EntityObservable() {
        super();
    }

    @Nullable
    final Entity getItem() {
        return item;
    }

    final void setItem(@Nullable Entity item) {
        this.item = item;
        setChanged();
        notifyObservers();
    }

}
