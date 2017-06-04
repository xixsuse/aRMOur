package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public abstract class AbstractSinglePaymentItemListFragment extends AbstractPaymentItemListFragment {

    @NonNull
    abstract String getFirstLine(@NonNull Cursor cursor);

    @Nullable
    abstract String getSecondLine(@NonNull Cursor cursor);

    @Override
    final void onViewHolderCreated(ListItemViewHolder holder) {
        holder.primaryIcon.setVisibility(View.GONE);
    }

    @Override
    final void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor) {
        super.bindViewHolderToCursor(holder, cursor);
        holder.setText(getFirstLine(cursor), getSecondLine(cursor));
    }

}