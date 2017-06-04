package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.components.ShiftListActivity;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

public class ExpensesListFragment extends AbstractSinglePaymentItemListFragment {

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

    @Override
    int getTitle() {
        return R.string.expenses;
    }

    @Override
    int getLoaderId() {
        return ShiftListActivity.LOADER_ID_EXPENSES_LIST;
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
    String[] getProjection() {
        return PROJECTION;
    }

    @Nullable
    @Override
    String getSortOrder() {
        return null;
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

}
