package com.skepticalone.armour.data.viewModel;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

public interface DateViewModelContract<FinalItem> extends ItemViewModelContract<FinalItem> {
    void saveNewDate(@NonNull LocalDate date);
}
