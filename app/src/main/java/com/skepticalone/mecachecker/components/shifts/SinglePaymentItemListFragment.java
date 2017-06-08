package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.skepticalone.mecachecker.R;

abstract class SinglePaymentItemListFragment extends ListFragment {

    abstract int getColumnIndexClaimed();

    abstract int getColumnIndexPaid();

    @Override
    final int getMenu() {
        return R.menu.menu_with_add;
    }

    abstract void addItem();

    @Override
    public final boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            addItem();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    abstract String getFirstLine(@NonNull Cursor cursor);

    @Nullable
    abstract String getSecondLine(@NonNull Cursor cursor);

    @Override
    final void onViewHolderCreated(PlainListItemViewHolder holder) {
        holder.primaryIcon.setVisibility(View.GONE);
    }

    @Override
    final void bindViewHolderToCursor(PlainListItemViewHolder holder, @NonNull Cursor cursor) {
        holder.setText(getFirstLine(cursor), getSecondLine(cursor));
        holder.secondaryIcon.setImageResource(getClaimStatusIcon(cursor, getColumnIndexClaimed(), getColumnIndexPaid()));
    }

}