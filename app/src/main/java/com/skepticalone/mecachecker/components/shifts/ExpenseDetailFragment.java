package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

public class ExpenseDetailFragment extends SinglePaymentItemDetailFragment {

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
            COLUMN_INDEX_PAID = 4;

    private static final int
            ROW_NUMBER_TITLE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;


    private static final String TITLE_DIALOG = "TITLE_DIALOG";

    private String mTitle;

    private final View.OnClickListener mTitleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PlainTextDialogFragment.newInstance(mTitle, R.string.title, getContentUri(), Contract.Expenses.COLUMN_NAME_TITLE)
                    .show(getFragmentManager(), TITLE_DIALOG);
        }
    };

    public ExpenseDetailFragment() {
    }

    static ExpenseDetailFragment create(long id) {
        ExpenseDetailFragment fragment = new ExpenseDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }

    @Override
    int getTitle() {
        return R.string.expense;
    }

    @Override
    int getLoaderId() {
        return MainActivity.LOADER_ID_EXPENSES_DETAIL;
    }

    @Override
    Uri getContentUri() {
        return Provider.expenseUri(getItemId());
    }

    @Nullable
    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @NonNull
    @Override
    String getColumnNamePayment() {
        return Contract.Expenses.COLUMN_NAME_PAYMENT;
    }

    @NonNull
    @Override
    String getColumnNameComment() {
        return Contract.Expenses.COLUMN_NAME_COMMENT;
    }

    @NonNull
    @Override
    String getColumnNameClaimed() {
        return Contract.Expenses.COLUMN_NAME_CLAIMED;
    }

    @NonNull
    @Override
    String getColumnNamePaid() {
        return Contract.Expenses.COLUMN_NAME_PAID;
    }

    @Override
    int getRowNumberPayment() {
        return ROW_NUMBER_PAYMENT;
    }

    @Override
    int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getRowNumberClaimed() {
        return ROW_NUMBER_CLAIMED;
    }

    @Override
    int getRowNumberPaid() {
        return ROW_NUMBER_PAID;
    }

    @Override
    int getRowCountIfLoaded() {
        return ROW_COUNT;
    }

    @Override
    int getColumnIndexPayment() {
        return COLUMN_INDEX_PAYMENT;
    }

    @Override
    int getColumnIndexComment() {
        return COLUMN_INDEX_COMMENT;
    }

    @Override
    int getColumnIndexClaimed() {
        return COLUMN_INDEX_CLAIMED;
    }

    @Override
    int getColumnIndexPaid() {
        return COLUMN_INDEX_PAID;
    }

    @Override
    void readFromPositionedCursor(@NonNull Cursor cursor) {
        super.readFromPositionedCursor(cursor);
        mTitle = cursor.getString(COLUMN_INDEX_TITLE);
    }

    @Override
    void onBindViewHolder(PlainListItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_TITLE) {
            onBindViewHolder(holder, R.drawable.ic_title_black_24dp, R.string.title, mTitle, mTitleListener);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    boolean isSwitchType(int position) {
        return position != ROW_NUMBER_TITLE && super.isSwitchType(position);
    }
}
