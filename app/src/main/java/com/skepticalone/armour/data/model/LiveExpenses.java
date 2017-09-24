package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class LiveExpenses extends LiveItems<RawExpenseEntity, Expense> {

    LiveExpenses(@NonNull Context context, @NonNull LiveData<List<RawExpenseEntity>> liveRawExpenses) {
        super(context, liveRawExpenses);
    }

    @Override
    void onUpdated(@Nullable List<RawExpenseEntity> rawExpenses, @Nullable ZoneId timeZone) {
        if (rawExpenses != null && timeZone != null) {
            List<Expense> expenses = new ArrayList<>(rawExpenses.size());
            for (RawExpenseEntity rawExpense : rawExpenses) {
                expenses.add(new Expense(rawExpense, timeZone));
            }
            setValue(expenses);
        }
    }

}
