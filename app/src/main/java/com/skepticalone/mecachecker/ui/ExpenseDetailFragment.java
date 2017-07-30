package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ExpenseDetailAdapter;
import com.skepticalone.mecachecker.adapter.PayableDetailAdapter;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.PayableViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

public final class ExpenseDetailFragment
        extends PayableDetailFragment<ExpenseEntity, ExpenseViewModel>
        implements ExpenseDetailAdapter.Callbacks {

    @NonNull
    @Override
    PayableDetailAdapter<ExpenseEntity> createAdapter(Context context) {
        return new ExpenseDetailAdapter(this);
    }

    @NonNull
    @Override
    ExpenseViewModel createViewModel(ViewModelProvider provider) {
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

    public static final class ExpenseCommentDialogFragment extends CommentDialogFragment<ExpenseEntity> {

        @NonNull
        @Override
        ViewModelContract<ExpenseEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(ExpenseViewModel.class);
        }

    }

    public static final class ExpensePaymentDialogFragment extends PaymentDialogFragment<ExpenseEntity> {

        @Override
        int getTitle() {
            return R.string.payment;
        }

        @NonNull
        @Override
        PayableViewModelContract<ExpenseEntity> onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(ExpenseViewModel.class);
        }

    }

    public static final class ExpenseTitleDialogFragment extends PlainTextDialogFragment<ExpenseEntity, ExpenseViewModel> {

        @Override
        int getTitle() {
            return R.string.title;
        }

        @NonNull
        @Override
        ExpenseViewModel onCreateViewModel(@NonNull ViewModelProvider viewModelProvider) {
            return viewModelProvider.get(ExpenseViewModel.class);
        }

        @Override
        String getTextForDisplay(@NonNull ExpenseEntity item) {
            return item.getTitle();
        }

        @Override
        void saveText(@Nullable String title) {
            if (title == null) {
                showSnackbar(R.string.title_required);
            } else {
                getViewModel().saveNewTitle(getCurrentItem().getId(), title);
            }
        }
    }
}
