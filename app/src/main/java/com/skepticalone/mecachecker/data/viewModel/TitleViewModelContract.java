package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.ExpenseEntity;

public interface TitleViewModelContract extends ViewModelContract<ExpenseEntity> {
    void saveNewTitle(@NonNull String newTitle);
}
