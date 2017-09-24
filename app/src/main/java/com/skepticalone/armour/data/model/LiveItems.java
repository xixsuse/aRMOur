package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.List;

abstract class LiveItems<Entity, FinalItem> extends MediatorLiveData<List<FinalItem>> {

    @NonNull
    final LiveData<List<Entity>> liveRawItems;
    @NonNull
    final LiveData<ZoneId> liveTimeZone;

    LiveItems(@NonNull Context context, @NonNull LiveData<List<Entity>> liveRawItems) {
        super();
        this.liveRawItems = liveRawItems;
        liveTimeZone = LiveTimeZone.getInstance(context);
        addSource(this.liveRawItems, new Observer<List<Entity>>() {
            @Override
            public void onChanged(@Nullable List<Entity> rawItems) {
                onUpdated(rawItems, liveTimeZone.getValue());
            }
        });
        addSource(liveTimeZone, new Observer<ZoneId>() {
            @Override
            public void onChanged(@Nullable ZoneId timeZone) {
                onUpdated(LiveItems.this.liveRawItems.getValue(), timeZone);
            }
        });
    }

    abstract void onUpdated(@Nullable List<Entity> rawItems, @Nullable ZoneId timeZone);
//
//    @NonNull
//    abstract List<FinalItem> generateItems(@NonNull List<Entity> rawItems, @NonNull ZoneId timeZone);
}