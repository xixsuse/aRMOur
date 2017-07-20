package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ExpenseDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.model.Expense;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.dialog.PaymentDialogFragment;
import com.skepticalone.mecachecker.dialog.TitleDialogFragment;

import java.math.BigDecimal;

public final class ExpenseDetailFragment
        extends DetailFragment<Expense, ExpenseEntity, ExpenseViewModel>
        implements ExpenseDetailAdapter.Callbacks, TitleDialogFragment.Callbacks, PaymentDialogFragment.Callbacks {


    @NonNull
    @Override
    ItemDetailAdapter<Expense> createAdapter(Context context) {
        return new ExpenseDetailAdapter(this);
    }

    @NonNull
    @Override
    ExpenseViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(ExpenseViewModel.class);
    }

    @Override
    public void changeTitle(long id, @NonNull String currentTitle) {
        showDialogFragment(TitleDialogFragment.newInstance(id, currentTitle));
    }

    @Override
    public void saveTitle(long id, @NonNull String trimmedTitle) {
        getViewModel().setTitle(id, trimmedTitle);
    }

    @Override
    public void onPaymentClicked(long id, @NonNull BigDecimal payment) {
        showDialogFragment(PaymentDialogFragment.newInstance(id, payment, R.string.payment));
    }

    @Override
    public void savePayment(long id, @NonNull BigDecimal payment) {
        getViewModel().getPayableModel().setPayment(id, payment);
    }

    @Override
    public void onClaimedToggled(long id, boolean claimed) {
        getViewModel().getPayableModel().setClaimed(id, claimed);
    }

    @Override
    public void onPaidToggled(long id, boolean paid) {
        getViewModel().getPayableModel().setPaid(id, paid);
    }

}
