package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.entity.ExpenseEntity;

public interface TitleViewModel extends BaseViewModel<ExpenseEntity> {
    void saveNewTitle(@NonNull String newTitle);
}
