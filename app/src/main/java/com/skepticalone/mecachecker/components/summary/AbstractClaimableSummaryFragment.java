package com.skepticalone.mecachecker.components.summary;


import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.skepticalone.mecachecker.R;

import java.math.BigDecimal;
import java.util.Locale;

public abstract class AbstractClaimableSummaryFragment extends AbstractSummaryFragment {

    @Override
    void bindCursor(SummaryViewHolder holder, int row, @NonNull Cursor cursor) {
        switch (row) {
            case 0:
                bindCursorToItemCounts(holder, cursor);
                break;
            case 1:
                bindCursorToMoney(holder, cursor);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    int getRowCount() {
        return 2;
    }

    @StringRes
    abstract int getItemsTitle();

    abstract int getColumnIndexClaimed();

    abstract int getColumnIndexPaid();

    abstract BigDecimal getCurrentSum(@NonNull Cursor cursor);

    private void bindCursorToItemCounts(SummaryViewHolder holder, @NonNull Cursor cursor) {
        holder.titleView.setText(getItemsTitle());
        int total = 0, unclaimed = 0, claimed = 0, paid = 0;
        if (cursor.moveToFirst()) {
            do {
                if (!cursor.isNull(getColumnIndexPaid())) {
                    paid++;
                } else if (!cursor.isNull(getColumnIndexClaimed())) {
                    claimed++;
                } else {
                    unclaimed++;
                }
                total++;
            } while (cursor.moveToNext());
        }
        holder.totalView.setText(String.format(Locale.US, "%d", total));
        if (total != 0) {
            holder.unclaimedView.setText(String.format(Locale.US, "Unclaimed: %d (%d%%)", unclaimed, 100 * unclaimed / total));
            holder.claimedView.setText(String.format(Locale.US, "Claimed: %d (%d%%)", claimed, 100 * claimed / total));
            holder.paidView.setText(String.format(Locale.US, "Paid: %d (%d%%)", paid, 100 * paid / total));
            holder.pieView.set(unclaimed, claimed, paid);
            holder.subtotalsView.setVisibility(View.VISIBLE);
            holder.pieView.setVisibility(View.VISIBLE);
        } else {
            holder.subtotalsView.setVisibility(View.GONE);
            holder.pieView.setVisibility(View.GONE);
        }
    }

    private void bindCursorToMoney(SummaryViewHolder holder, @NonNull Cursor cursor) {
        holder.titleView.setText(R.string.money);
        BigDecimal total = BigDecimal.ZERO, unclaimed = BigDecimal.ZERO, claimed = BigDecimal.ZERO, paid = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            do {
                BigDecimal current = getCurrentSum(cursor);
                if (!cursor.isNull(getColumnIndexPaid())) {
                    paid = paid.add(current);
                } else if (!cursor.isNull(getColumnIndexClaimed())) {
                    claimed = claimed.add(current);
                } else {
                    unclaimed = unclaimed.add(current);
                }
                total = total.add(current);
            } while (cursor.moveToNext());
        }
        holder.totalView.setText(String.format(Locale.US, "$%.2f", total));
        if (!total.equals(BigDecimal.ZERO)) {
            holder.unclaimedView.setText(String.format(Locale.US, "Unclaimed: $%.2f (%d%%)", unclaimed, unclaimed.scaleByPowerOfTen(2).divide(total, 0, BigDecimal.ROUND_HALF_UP).intValue()));
            holder.claimedView.setText(String.format(Locale.US, "Claimed: $%.2f (%d%%)", claimed, claimed.scaleByPowerOfTen(2).divide(total, 0, BigDecimal.ROUND_HALF_UP).intValue()));
            holder.paidView.setText(String.format(Locale.US, "Paid: $%.2f (%d%%)", paid, paid.scaleByPowerOfTen(2).divide(total, 0, BigDecimal.ROUND_HALF_UP).intValue()));
            holder.pieView.set(unclaimed.longValue(), claimed.longValue(), paid.longValue());
            holder.subtotalsView.setVisibility(View.VISIBLE);
            holder.pieView.setVisibility(View.VISIBLE);
        } else {
            holder.subtotalsView.setVisibility(View.GONE);
            holder.pieView.setVisibility(View.GONE);
        }
    }


}
