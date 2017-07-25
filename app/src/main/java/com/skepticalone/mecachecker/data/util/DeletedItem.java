package com.skepticalone.mecachecker.data.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class DeletedItem {

    public static abstract class Observer<T> implements java.util.Observer {
        @Override
        public final void update(java.util.Observable observable, Object o) {
            //noinspection unchecked
            Observable<T> deletedItemObservable = (Observable<T>) observable;
            if (deletedItemObservable.item != null) {
                update(deletedItemObservable.item);
            }
        }
        public abstract void update(@NonNull T deletedItem);
    }

    public static final class Observable<T> extends java.util.Observable {
        @Nullable
        private T item;
        public void setItem(@Nullable T item) {
            this.item = item;
            setChanged();
            notifyObservers();
        }
    }
}
