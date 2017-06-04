package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;

public abstract class AbstractSinglePaymentItemListFragment extends AbstractItemListFragment {

    abstract CharSequence getText(@NonNull Cursor cursor);

    abstract int getColumnIndexClaimed();

    abstract int getColumnIndexPaid();

    @Override
    final void onViewHolderCreated(ListItemViewHolder holder) {
        holder.secondaryIcon.setVisibility(View.GONE);
    }

    @Override
    final void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor) {
        holder.text.setText(getText(cursor));
        holder.primaryIcon.setImageResource(cursor.isNull(getColumnIndexPaid()) ? cursor.isNull(getColumnIndexClaimed()) ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp);
    }

}
