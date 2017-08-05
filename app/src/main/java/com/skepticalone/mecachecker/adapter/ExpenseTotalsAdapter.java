package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Expense;

public final class ExpenseTotalsAdapter extends PayableTotalsAdapter<Expense> {

    private static final int
        ROW_NUMBER_TOTAL_NUMBER = 0,
        ROW_NUMBER_TOTAL_PAYMENT = 1,
        ROW_COUNT = 2;

    ExpenseTotalsAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    int getRowNumberTotalNumber() {
        return ROW_NUMBER_TOTAL_NUMBER;
    }

    @Override
    int getRowNumberTotalPayment() {
        return ROW_NUMBER_TOTAL_PAYMENT;
    }

    @Override
    int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    int getTitle() {
        return R.string.expenses;
    }

}
