package com.skepticalone.armour.data.viewModel;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public interface DateViewModelContract<Entity> extends ItemViewModelContract<Entity> {
    void saveNewDate(@NonNull LocalDate date);
}
