package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
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
            Contract.Expenses.COLUMN_NAME_COMMENT,
            Contract.Expenses.COLUMN_NAME_CLAIMED,
            Contract.Expenses.COLUMN_NAME_PAID
    };

    private static final String SORT_ORDER = Contract.Expenses.COLUMN_NAME_PAID + " IS NULL, " + Contract.Expenses.COLUMN_NAME_CLAIMED + " IS NULL, IFNULL(" + Contract.Expenses.COLUMN_NAME_PAID + ", " + Contract.Expenses.COLUMN_NAME_CLAIMED + ")";

    private static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_TITLE = 1,
            COLUMN_INDEX_COMMENT = 2,
            COLUMN_INDEX_CLAIMED = 3,
            COLUMN_INDEX_PAID = 4;

    @Override
    int getTitle() {
        return R.string.expenses;
    }

    @Override
    int getLoaderId() {
        return LifecycleConstants.LOADER_ID_EXPENSES_LIST;
    }

    @Override
    int getColumnIndexId() {
        return COLUMN_INDEX_ID;
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

    @Nullable
    @Override
    String getSortOrder() {
        return SORT_ORDER;
    }

    @Override
    public Uri getReadContentUri() {
        return Provider.expensesUri;
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

    @Override
    void addItem() {
        // TODO: 11/06/17 
        ContentValues values = new ContentValues();
        values.put(Contract.Expenses.COLUMN_NAME_TITLE, "New item");
        values.put(Contract.Expenses.COLUMN_NAME_PAYMENT, 7900);
        insert(values);
    }

    @Override
    void onItemClicked(long id) {
        listCallbacks.launch(LifecycleConstants.ITEM_TYPE_EXPENSE, id);
    }

}
