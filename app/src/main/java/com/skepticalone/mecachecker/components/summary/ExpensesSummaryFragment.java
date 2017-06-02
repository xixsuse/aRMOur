package com.skepticalone.mecachecker.components.summary;

import android.net.Uri;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

public class ExpensesSummaryFragment extends PaymentsSummaryFragment {

    @StringRes
    static final int tabTitle = R.string.expenses;

    @Override
    int getLoaderId() {
        return SummaryActivity.LOADER_ID_EXPENSES;
    }

    @Override
    int getItemsTitle() {
        return R.string.expenses;
    }

    @Override
    Uri getContentUri() {
        return Provider.expensesUri;
    }

    @Override
    String getColumnNamePayment() {
        return Contract.Expenses.COLUMN_NAME_PAYMENT;
    }

    @Override
    String getColumnNameClaimed() {
        return Contract.Expenses.COLUMN_NAME_CLAIMED;
    }

    @Override
    String getColumnNamePaid() {
        return Contract.Expenses.COLUMN_NAME_PAID;
    }

}
