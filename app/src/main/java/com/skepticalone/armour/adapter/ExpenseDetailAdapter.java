package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Expense;

import java.util.ArrayList;
import java.util.List;

public final class ExpenseDetailAdapter extends ItemDetailAdapter<Expense> {

    @NonNull
    private final Callbacks callbacks;

    public ExpenseDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    List<ItemViewHolder.Binder> getNewList(@NonNull Expense expense) {
        List<ItemViewHolder.Binder> list = new ArrayList<>(5);
        list.add(new TitleBinder(callbacks, expense.getTitle()));
        list.add(new PaymentBinder(callbacks, expense.getPaymentData().getPayment(), R.drawable.ic_dollar_black_24dp, R.string.payment));
        list.add(new CommentBinder(callbacks, expense.getComment()));
        list.add(new ClaimedSwitchBinder(callbacks, expense.getPaymentData()));
        list.add(new PaidSwitchBinder(callbacks, expense.getPaymentData()));
        return list;
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, TitleBinder.Callbacks, PaymentBinder.Callbacks, ClaimedSwitchBinder.Callbacks, PaidSwitchBinder.Callbacks {
    }

}
