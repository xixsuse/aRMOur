package com.skepticalone.mecachecker.components.shifts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

public class ExpensesListFragment extends SinglePaymentItemListFragment {

    private static final String[] PROJECTION = {
            Contract.Expenses._ID,
            Contract.Expenses.COLUMN_NAME_TITLE,
            Contract.Expenses.COLUMN_NAME_CLAIMED,
            Contract.Expenses.COLUMN_NAME_PAID,
            Contract.Expenses.COLUMN_NAME_COMMENT,
    };

    private static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_TITLE = 1,
            COLUMN_INDEX_CLAIMED = 2,
            COLUMN_INDEX_PAID = 3,
            COLUMN_INDEX_COMMENT = 4;

    private Listener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }

    @Override
    int getTitle() {
        return R.string.expenses;
    }

    @Override
    int getLoaderId() {
        return MainActivity.LOADER_ID_EXPENSES_LIST;
    }

    @Override
    int getColumnIndexId() {
        return COLUMN_INDEX_ID;
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
    Uri getContentUri() {
        return Provider.expensesUri;
    }

    @Override
    Uri getItemUri(long id) {
        return Provider.expenseUri(id);
    }

    @Nullable
    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Cursor cursor) {
        return cursor.getString(COLUMN_INDEX_TITLE);
    }

    @Nullable
    @Override
    String getSecondLine(@NonNull Cursor cursor) {
        return null;
    }

    @Override
    void addItem() {
        mListener.addExpense();
    }

    @Override
    void onItemClicked(long id) {
        mListener.onExpenseClicked(id);
    }

    interface Listener {
        void onExpenseClicked(long id);

        void addExpense();
    }

}
