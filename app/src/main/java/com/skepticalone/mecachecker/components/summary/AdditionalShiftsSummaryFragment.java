package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import java.math.BigDecimal;
import java.util.Locale;

public class AdditionalShiftsSummaryFragment extends AbstractClaimableSummaryFragment {

    @StringRes
    static final int tabTitle = R.string.additional;

    private static final String[] PROJECTION = {
            Contract.AdditionalShifts.COLUMN_NAME_START,
            Contract.AdditionalShifts.COLUMN_NAME_END,
            Contract.AdditionalShifts.COLUMN_NAME_RATE,
            Contract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            Contract.AdditionalShifts.COLUMN_NAME_PAID,
    };

    private static final int
            COLUMN_INDEX_START = 0,
            COLUMN_INDEX_END = 1,
            COLUMN_INDEX_RATE = 2,
            COLUMN_INDEX_CLAIMED = 3,
            COLUMN_INDEX_PAID = 4;

    @Override
    int getItemsTitle() {
        return R.string.additional_shifts;
    }

    @Override
    Uri getContentUri() {
        return Provider.additionalShiftsUri;
    }

    @Override
    int getLoaderId() {
        return SummaryActivity.LOADER_ID_ADDITIONAL;
    }

    private Duration getCurrentDuration(@NonNull Cursor cursor) {
        return new Duration(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END));
    }

    @Override
    BigDecimal getCurrentSum(@NonNull Cursor cursor) {
        return BigDecimal.valueOf(cursor.getInt(COLUMN_INDEX_RATE), 2)
                .multiply(BigDecimal.valueOf(getCurrentDuration(cursor).getMillis()))
                .divide(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_HOUR), 2, BigDecimal.ROUND_HALF_UP);
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
    int getRowCount() {
        return super.getRowCount() + 1;
    }

    @Override
    void bindCursor(SummaryViewHolder holder, int row, @NonNull Cursor cursor) {
        if (row == getRowCount() - 1) {
            holder.titleView.setText(R.string.hours);
            Duration total = Duration.ZERO, unclaimed = Duration.ZERO, claimed = Duration.ZERO, paid = Duration.ZERO;
            if (cursor.moveToFirst()) {
                do {
                    Duration current = getCurrentDuration(cursor);
                    if (!cursor.isNull(getColumnIndexPaid())) {
                        paid = paid.plus(current);
                    } else if (!cursor.isNull(getColumnIndexClaimed())) {
                        claimed = claimed.plus(current);
                    } else {
                        unclaimed = unclaimed.plus(current);
                    }
                    total = total.plus(current);
                } while (cursor.moveToNext());
            }
            holder.totalView.setText(String.format(Locale.US, "%.1f", (float) total.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR));
            if (!total.isEqual(Duration.ZERO)) {
                holder.unclaimedView.setText(String.format(Locale.US, "Unclaimed: %.1f (%d%%)", (float) unclaimed.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * unclaimed.getMillis() / total.getMillis()));
                holder.claimedView.setText(String.format(Locale.US, "Claimed: %.1f (%d%%)", (float) claimed.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * claimed.getMillis() / total.getMillis()));
                holder.paidView.setText(String.format(Locale.US, "Paid: %.1f (%d%%)", (float) paid.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * paid.getMillis() / total.getMillis()));
                holder.pieView.set(unclaimed.getMillis(), claimed.getMillis(), paid.getMillis());
                holder.subtotalsView.setVisibility(View.VISIBLE);
                holder.pieView.setVisibility(View.VISIBLE);
            } else {
                holder.subtotalsView.setVisibility(View.GONE);
                holder.pieView.setVisibility(View.GONE);
            }
        } else {
            super.bindCursor(holder, row, cursor);
        }
    }

    @Override
    String[] getProjection() {
        return PROJECTION;
    }
}
