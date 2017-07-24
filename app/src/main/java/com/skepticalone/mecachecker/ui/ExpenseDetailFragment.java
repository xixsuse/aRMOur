package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.NewExpenseViewModel;
import com.skepticalone.mecachecker.dialog.ExpensePaymentDialogFragment;
import com.skepticalone.mecachecker.dialog.ExpenseTitleDialogFragment;

public final class ExpenseDetailFragment
        extends DetailFragment<ExpenseEntity, NewExpenseViewModel>
        implements ExpenseDetailAdapter.Callbacks {

    @NonNull
    @Override
    ItemDetailAdapter<ExpenseEntity> createAdapter(Context context) {
        return new ExpenseDetailAdapter(this);
    }

    @NonNull
    @Override
    NewExpenseViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(NewExpenseViewModel.class);
    }

    @Override
    public void changeTitle() {
        showDialogFragment(new ExpenseTitleDialogFragment());
    }

    @Override
    public void changePayment() {
        showDialogFragment(new ExpensePaymentDialogFragment());
    }

    @Override
    public void setClaimed(boolean claimed) {
        getViewModel().setClaimed(claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        getViewModel().setPaid(paid);
    }

    //
//    @Override
//    public void changeTitle(long id, @NonNull String currentTitle) {
//        showDialogFragment(TitleDialogFragment.newInstance(id, currentTitle));
//    }
//
//    @Override
//    public void saveTitle(long id, @NonNull String trimmedTitle) {
//        getViewModel().setTitle(id, trimmedTitle);
//    }
//
//    @Override
//    public void onPaymentClicked(long id, @NonNull BigDecimal payment) {
//        showDialogFragment(PaymentDialogFragment.newInstance(id, payment, R.string.payment));
//    }
//
//    @Override
//    public void savePayment(long id, @NonNull BigDecimal payment) {
//        getViewModel().getPayableModel().setPayment(id, payment);
//    }
//
//    @Override
//    public void onClaimedToggled(long id, boolean claimed) {
//        getViewModel().getPayableModel().setClaimed(id, claimed);
//    }
//
//    @Override
//    public void onPaidToggled(long id, boolean paid) {
//        getViewModel().getPayableModel().setPaid(id, paid);
//    }

}
