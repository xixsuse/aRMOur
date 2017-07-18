package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ExpenseDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.dialog.PaymentDialogFragment;
import com.skepticalone.mecachecker.dialog.TitleDialogFragment;
import com.skepticalone.mecachecker.model.Expense;

import java.math.BigDecimal;

public final class ExpenseDetailFragment
        extends DetailFragment<Expense, ExpenseEntity>
        implements ExpenseDetailAdapter.Callbacks, TitleDialogFragment.Callbacks, PaymentDialogFragment.Callbacks {

    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private ExpenseViewModel model;

    @NonNull
    @Override
    ItemDetailAdapter<Expense> onCreateAdapter(Context context) {
        return new ExpenseDetailAdapter(this);
    }

    @NonNull
    @Override
    Model<ExpenseEntity> onCreateViewModel() {
        model = ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
        return model;
    }

    @Override
    public void changeTitle(long id, @NonNull String currentTitle) {
        showDialogFragment(TitleDialogFragment.newInstance(id, currentTitle));
    }

    @Override
    public void saveTitle(long itemId, @NonNull String trimmedTitle) {
        model.setTitle(itemId, trimmedTitle);
    }

    @Override
    public void onPaymentClicked(long id, @NonNull BigDecimal payment) {
        showDialogFragment(PaymentDialogFragment.newInstance(id, payment, R.string.payment));
    }

    @Override
    public void savePayment(long id, @NonNull BigDecimal payment) {
        model.setPayment(id, payment);
    }

    @Override
    public void onClaimedToggled(long id, boolean claimed) {
        model.setClaimed(id, claimed);
    }

    @Override
    public void onPaidToggled(long id, boolean paid) {
        model.setPaid(id, paid);
    }

    @Override
    public int getRowNumberTitle() {
        return ROW_NUMBER_TITLE;
    }

    @Override
    public int getRowNumberPayment() {
        return ROW_NUMBER_PAYMENT;
    }

    @Override
    public int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    public int getRowNumberClaimed() {
        return ROW_NUMBER_CLAIMED;
    }

    @Override
    public int getRowNumberPaid() {
        return ROW_NUMBER_PAID;
    }

    @Override
    public int getRowCount(@NonNull Expense item) {
        return ROW_COUNT;
    }
}
