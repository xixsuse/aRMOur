package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.Nullable;

import java.util.Observable;
import java.util.Observer;


public abstract class EntityObserver<Entity> implements Observer {

    @Override
    public final void update(Observable observable, Object o) {
        //noinspection unchecked
        update(((EntityObservable<Entity>) observable).getItem());
    }

    public abstract void update(@Nullable Entity item);

}
