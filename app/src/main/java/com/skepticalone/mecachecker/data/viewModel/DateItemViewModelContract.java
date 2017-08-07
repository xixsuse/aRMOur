package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public interface DateItemViewModelContract<Entity> extends ItemViewModelContract<Entity> {
    void saveNewDate(@NonNull LocalDate date);
}
