package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;

abstract class AbstractPaymentItemListFragment extends AbstractItemListFragment {


    abstract int getColumnIndexClaimed();

    abstract int getColumnIndexPaid();

    @Override
    void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor) {
        holder.secondaryIcon.setImageResource(cursor.isNull(getColumnIndexPaid()) ? cursor.isNull(getColumnIndexClaimed()) ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp);
    }

}