package com.skepticalone.mecachecker.components.summary;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

import java.math.BigDecimal;
import java.util.Locale;

public class ExpensesSummaryFragment extends SummaryFragment {

    @StringRes
    static final int tabTitle = R.string.expenses;

    private static final String[] PROJECTION = {
            Contract.Expenses.COLUMN_NAME_PAYMENT,
            Contract.Expenses.COLUMN_NAME_CLAIMED,
            Contract.Expenses.COLUMN_NAME_PAID
    };
    private static final int
            COLUMN_INDEX_PAYMENT = 0,
            COLUMN_INDEX_CLAIMED = 1,
            COLUMN_INDEX_PAID = 2;

    @Override
    int getLoaderId() {
        return SummaryActivity.LOADER_ID_EXPENSES;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.expensesUri, PROJECTION, null, null, null);
    }

    @Override
    int getRowCount() {
        return 2;
    }

    @Override
    void bindViewHolderToCursor(SummaryViewHolder holder, int row, @NonNull Cursor cursor) {
        switch (row) {
            case 0:
                onBindExpenseItems(holder, cursor);
                break;
            case 1:
                onBindExpenseMoney(holder, cursor);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void onBindExpenseItems(SummaryViewHolder holder, @NonNull Cursor cursor) {
        int shiftsUnclaimedCount = 0, shiftsClaimedCount = 0, shiftsPaidCount = 0;
        if (cursor.moveToFirst()) {
            do {
                if (!cursor.isNull(COLUMN_INDEX_PAID)) {
                    shiftsPaidCount++;
                } else if (!cursor.isNull(COLUMN_INDEX_CLAIMED)) {
                    shiftsClaimedCount++;
                } else {
                    shiftsUnclaimedCount++;
                }
            } while (cursor.moveToNext());
            int shiftsTotalCount = shiftsUnclaimedCount + shiftsClaimedCount + shiftsPaidCount;
            holder.totalView.setText(String.format(Locale.US, "%d", shiftsTotalCount));
            holder.unclaimedView.setText(String.format(Locale.US, "Unclaimed: %d (%d%%)", shiftsUnclaimedCount, 100 * shiftsUnclaimedCount / shiftsTotalCount));
            holder.claimedView.setText(String.format(Locale.US, "Claimed: %d (%d%%)", shiftsClaimedCount, 100 * shiftsClaimedCount / shiftsTotalCount));
            holder.paidView.setText(String.format(Locale.US, "Paid: %d (%d%%)", shiftsPaidCount, 100 * shiftsPaidCount / shiftsTotalCount));
            holder.pieView.set(shiftsUnclaimedCount, shiftsClaimedCount, shiftsPaidCount);
            holder.titleView.setText(R.string.expenses);
            holder.itemView.setVisibility(View.VISIBLE);
            return;
        }
        holder.itemView.setVisibility(View.GONE);
    }

    private void onBindExpenseMoney(SummaryViewHolder holder, @NonNull Cursor cursor) {
        if (cursor.moveToFirst()) {
            BigDecimal moneyUnclaimedCount = BigDecimal.ZERO, moneyClaimedCount = BigDecimal.ZERO, moneyPaidCount = BigDecimal.ZERO;
            do {
                BigDecimal moneyCurrent = BigDecimal.valueOf(cursor.getInt(COLUMN_INDEX_PAYMENT), 2);
                if (!cursor.isNull(COLUMN_INDEX_PAID)) {
                    moneyPaidCount = moneyPaidCount.add(moneyCurrent);
                } else if (!cursor.isNull(COLUMN_INDEX_CLAIMED)) {
                    moneyClaimedCount = moneyClaimedCount.add(moneyCurrent);
                } else {
                    moneyUnclaimedCount = moneyUnclaimedCount.add(moneyCurrent);
                }
            } while (cursor.moveToNext());
            BigDecimal moneyTotalCount = moneyUnclaimedCount.add(moneyClaimedCount).add(moneyPaidCount);
            if (!moneyTotalCount.equals(BigDecimal.ZERO)) {
                holder.totalView.setText(String.format(Locale.US, "$%.2f", moneyTotalCount));
                holder.unclaimedView.setText(String.format(Locale.US, "Unclaimed: $%.2f (%d%%)", moneyUnclaimedCount, moneyUnclaimedCount.scaleByPowerOfTen(2).divide(moneyTotalCount, 0, BigDecimal.ROUND_HALF_UP).intValue()));
                holder.claimedView.setText(String.format(Locale.US, "Claimed: $%.2f (%d%%)", moneyClaimedCount, moneyClaimedCount.scaleByPowerOfTen(2).divide(moneyTotalCount, 0, BigDecimal.ROUND_HALF_UP).intValue()));
                holder.paidView.setText(String.format(Locale.US, "Paid: $%.2f (%d%%)", moneyPaidCount, moneyPaidCount.scaleByPowerOfTen(2).divide(moneyTotalCount, 0, BigDecimal.ROUND_HALF_UP).intValue()));
                holder.pieView.set(moneyUnclaimedCount.longValue(), moneyClaimedCount.longValue(), moneyPaidCount.longValue());
                holder.titleView.setText(R.string.money);
                holder.itemView.setVisibility(View.VISIBLE);
                return;
            }
        }
        holder.itemView.setVisibility(View.GONE);
    }


}
