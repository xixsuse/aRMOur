package com.skepticalone.mecachecker.ui.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.adapter.ExpenseDetailAdapter;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelHelper;
import com.skepticalone.mecachecker.ui.dialog.CommentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.ExpensePaymentDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.ExpenseTitleDialogFragment;
import com.skepticalone.mecachecker.ui.dialog.PaymentDialogFragment;

public final class ExpenseDetailFragment
        extends PayableDetailFragment<ExpenseEntity>
        implements ExpenseDetailAdapter.Callbacks {

    private final ExpenseDetailAdapter adapter = new ExpenseDetailAdapter(this);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    PayableViewModelHelper getPayableViewModelHelper() {
        return null;
    }

    @NonNull
    @Override
    ItemViewModel<ExpenseEntity> getViewModel() {
        return null;
    }

    @NonNull
    @Override
    ItemDetailAdapter<ExpenseEntity> getAdapter() {
        return null;
    }

    @NonNull
    @Override
    PayableDetailAdapter<ExpenseEntity> createAdapter(Context context) {
        return new ExpenseDetailAdapter(this);
    }

    @NonNull
    @Override
    ExpenseViewModel createViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(ExpenseViewModel.class);
    }

    @Override
    public void changeTitle() {
        showDialogFragment(new ExpenseTitleDialogFragment());
    }

    @NonNull
    @Override
    PaymentDialogFragment getNewPaymentDialogFragment() {
        return new ExpensePaymentDialogFragment();
    }

    @NonNull
    @Override
    CommentDialogFragment getNewCommentDialogFragment() {
        return new ExpenseCommentDialogFragment();
    }

}
