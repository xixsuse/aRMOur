package com.skepticalone.mecachecker.components;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

public class ExpenseDetailFragment extends SinglePaymentItemDetailFragment implements TitleData.Callbacks {

    private static final String[] PROJECTION = {
            Contract.Expenses.COLUMN_NAME_TITLE,
            Contract.Expenses.COLUMN_NAME_PAYMENT,
            Contract.Expenses.COLUMN_NAME_COMMENT,
            Contract.Expenses.COLUMN_NAME_CLAIMED,
            Contract.Expenses.COLUMN_NAME_PAID
    };

    private static final int
            COLUMN_INDEX_TITLE = 0,
            COLUMN_INDEX_PAYMENT = 1,
            COLUMN_INDEX_COMMENT = 2,
            COLUMN_INDEX_CLAIMED = 3,
            COLUMN_INDEX_PAID = 4,
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private final TitleData mTitleData = new TitleData(this);

    static ExpenseDetailFragment create(long id) {
        ExpenseDetailFragment fragment = new ExpenseDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }

    @NonNull
    @Override
    AbstractData getData() {
        return mTitleData;
    }

    @Override
    int getTitle() {
        return R.string.expense;
    }

    @Override
    int getLoaderId() {
        return LifecycleConstants.LOADER_ID_EXPENSES_DETAIL;
    }

    @Override
    public Uri getContentUri() {
        return Provider.expenseUri(getItemId());
    }

    @Nullable
    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @NonNull
    @Override
    public String getColumnNameTitle() {
        return Contract.Expenses.COLUMN_NAME_TITLE;
    }

    @NonNull
    @Override
    public String getColumnNameMoney() {
        return Contract.Expenses.COLUMN_NAME_PAYMENT;
    }

    @NonNull
    @Override
    public String getColumnNameComment() {
        return Contract.Expenses.COLUMN_NAME_COMMENT;
    }

    @NonNull
    @Override
    public String getColumnNameClaimed() {
        return Contract.Expenses.COLUMN_NAME_CLAIMED;
    }

    @NonNull
    @Override
    public String getColumnNamePaid() {
        return Contract.Expenses.COLUMN_NAME_PAID;
    }

    @Override
    public int getColumnIndexTitle() {
        return COLUMN_INDEX_TITLE;
    }

    @Override
    public int getColumnIndexMoney() {
        return COLUMN_INDEX_PAYMENT;
    }

    @Override
    public int getColumnIndexComment() {
        return COLUMN_INDEX_COMMENT;
    }

    @Override
    public int getColumnIndexClaimed() {
        return COLUMN_INDEX_CLAIMED;
    }

    @Override
    public int getColumnIndexPaid() {
        return COLUMN_INDEX_PAID;
    }

    @Override
    public int getRowNumberTitle() {
        return ROW_NUMBER_TITLE;
    }

    @Override
    public int getRowNumberMoney() {
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
    int getRowCountIfPaid() {
        return ROW_COUNT;
    }

}
