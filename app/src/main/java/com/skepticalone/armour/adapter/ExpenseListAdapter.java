package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.util.Comparators;

public final class ExpenseListAdapter extends ItemListAdapter<Expense> {

    public ExpenseListAdapter(@NonNull Context context, @NonNull Callbacks callbacks, @NonNull MultiSelector.ModelCallbacks modelCallbacks) {
        super(context, callbacks, modelCallbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull Expense expense1, @NonNull Expense expense2) {
        return expense1.getTitle().equals(expense2.getTitle()) &&
                expense1.getTotalPayment().equals(expense2.getTotalPayment()) &&
                Comparators.equalStrings(expense1.getComment(), expense2.getComment()) &&
                expense1.getPaymentData().getIcon() == expense2.getPaymentData().getIcon();
    }

    @Override
    int getPrimaryIcon(@NonNull Expense expense) {
        return R.drawable.ic_dollar_black_24dp;
    }

    @Override
    int getSecondaryIcon(@NonNull Expense expense) {
        return expense.getPaymentData().getIcon();
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Expense expense) {
        return expense.getTitle();
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Expense expense) {
        return getPaymentString(expense.getTotalPayment());
    }

}
