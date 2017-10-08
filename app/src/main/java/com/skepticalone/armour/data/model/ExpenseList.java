package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class ExpenseList extends ItemList<ExpenseEntity, Expense> {

    public ExpenseList(@NonNull Context context, @NonNull LiveData<List<ExpenseEntity>> liveRawExpenses) {
        super(context, liveRawExpenses);
    }

    @Override
    void onUpdated(@Nullable List<ExpenseEntity> rawExpenses, @Nullable ZoneId timeZone) {
        if (rawExpenses != null && timeZone != null) {
            List<Expense> expenses = new ArrayList<>(rawExpenses.size());
            for (ExpenseEntity rawExpense : rawExpenses) {
                expenses.add(new Expense(rawExpense, timeZone));
            }
            setValue(expenses);
        }
    }

}
