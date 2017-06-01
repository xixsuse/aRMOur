package com.skepticalone.mecachecker.components.summary;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import java.math.BigDecimal;
import java.util.Locale;

public class AdditionalSummaryFragment extends SummaryFragment {

    @StringRes
    static final int tabTitle = R.string.additional_shifts;

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
    int getLoaderId() {
        return SummaryActivity.LOADER_ID_ADDITIONAL;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.additionalShiftsUri, PROJECTION, null, null, Contract.AdditionalShifts.COLUMN_NAME_START);
    }

    @Override
    SummaryAdapter getNewAdapter() {
        return new Adapter();
    }

    private class Adapter extends SummaryAdapter {

        @Override
        public void onBindViewHolder(SummaryViewHolder holder, int position) {
            switch (position) {
                case 0:
                    onBindShiftNumber(holder);
                    break;
                case 1:
                    onBindShiftDuration(holder);
                    break;
                case 2:
                    onBindShiftMoney(holder);
                    break;
                default:
                    throw new IllegalStateException();
            }
        }

        private void onBindShiftNumber(SummaryViewHolder holder) {
            int shiftsUnclaimedCount = 0, shiftsClaimedCount = 0, shiftsPaidCount = 0;
            if (mCursor != null && mCursor.moveToFirst()) {
                do {
                    if (!mCursor.isNull(COLUMN_INDEX_PAID)) {
                        shiftsPaidCount++;
                    } else if (!mCursor.isNull(COLUMN_INDEX_CLAIMED)) {
                        shiftsClaimedCount++;
                    } else {
                        shiftsUnclaimedCount++;
                    }
                } while (mCursor.moveToNext());
                int shiftsTotalCount = shiftsUnclaimedCount + shiftsClaimedCount + shiftsPaidCount;
                holder.totalView.setText(String.format(Locale.US, "%d", shiftsTotalCount));
                holder.progressBar.setMax(shiftsTotalCount);
                holder.progressBar.setProgress(shiftsPaidCount);
                holder.unclaimedView.setText(String.format(Locale.US, "Unclaimed: %d (%d%%)", shiftsUnclaimedCount, 100 * shiftsUnclaimedCount / shiftsTotalCount));
                holder.claimedView.setText(String.format(Locale.US, "Claimed: %d (%d%%)", shiftsClaimedCount, 100 * shiftsClaimedCount / shiftsTotalCount));
                holder.paidView.setText(String.format(Locale.US, "Paid: %d (%d%%)", shiftsPaidCount, 100 * shiftsPaidCount / shiftsTotalCount));
                holder.pieView.set(shiftsUnclaimedCount, shiftsClaimedCount, shiftsPaidCount);
                holder.titleView.setText(R.string.shifts);
                holder.itemView.setVisibility(View.VISIBLE);
                return;
            }
            holder.itemView.setVisibility(View.GONE);
        }

        private void onBindShiftDuration(SummaryViewHolder holder) {
            Duration durationUnclaimed = Duration.ZERO, durationClaimed = Duration.ZERO, durationPaid = Duration.ZERO;
            if (mCursor != null && mCursor.moveToFirst()) {
                do {
                    Duration durationCurrent = new Duration(mCursor.getLong(COLUMN_INDEX_START), mCursor.getLong(COLUMN_INDEX_END));
                    if (!mCursor.isNull(COLUMN_INDEX_PAID)) {
                        durationPaid = durationPaid.plus(durationCurrent);
                    } else if (!mCursor.isNull(COLUMN_INDEX_CLAIMED)) {
                        durationClaimed = durationClaimed.plus(durationCurrent);
                    } else {
                        durationUnclaimed = durationUnclaimed.plus(durationCurrent);
                    }
                } while (mCursor.moveToNext());
                Duration durationTotal = durationUnclaimed.plus(durationClaimed).plus(durationPaid);
                if (!durationTotal.isEqual(Duration.ZERO)) {
                    holder.totalView.setText(String.format(Locale.US, "%.1f", (float) durationTotal.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR));
                    holder.progressBar.setMax(100);
                    holder.progressBar.setProgress((int) (100 * durationPaid.getMillis() / durationTotal.getMillis()));
                    holder.unclaimedView.setText(String.format(Locale.US, "Unclaimed: %.1f (%d%%)", (float) durationUnclaimed.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * durationUnclaimed.getMillis() / durationTotal.getMillis()));
                    holder.claimedView.setText(String.format(Locale.US, "Claimed: %.1f (%d%%)", (float) durationClaimed.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * durationClaimed.getMillis() / durationTotal.getMillis()));
                    holder.paidView.setText(String.format(Locale.US, "Paid: %.1f (%d%%)", (float) durationPaid.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * durationPaid.getMillis() / durationTotal.getMillis()));
                    holder.pieView.set(durationUnclaimed.getMillis(), durationClaimed.getMillis(), durationPaid.getMillis());
                    holder.titleView.setText(R.string.hours);
                    holder.itemView.setVisibility(View.VISIBLE);
                    return;
                }
            }
            holder.itemView.setVisibility(View.GONE);
        }

        private void onBindShiftMoney(SummaryViewHolder holder) {
            if (mCursor != null && mCursor.moveToFirst()) {
                BigDecimal moneyUnclaimedCount = BigDecimal.ZERO, moneyClaimedCount = BigDecimal.ZERO, moneyPaidCount = BigDecimal.ZERO;
                do {
                    Duration durationCurrent = new Duration(mCursor.getLong(COLUMN_INDEX_START), mCursor.getLong(COLUMN_INDEX_END));
                    BigDecimal moneyCurrent = BigDecimal.valueOf(mCursor.getInt(COLUMN_INDEX_RATE), 2)
                            .multiply(BigDecimal.valueOf(durationCurrent.getMillis()))
                            .divide(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_HOUR), 2, BigDecimal.ROUND_HALF_UP);
                    if (!mCursor.isNull(COLUMN_INDEX_PAID)) {
                        moneyPaidCount = moneyPaidCount.add(moneyCurrent);
                    } else if (!mCursor.isNull(COLUMN_INDEX_CLAIMED)) {
                        moneyClaimedCount = moneyClaimedCount.add(moneyCurrent);
                    } else {
                        moneyUnclaimedCount = moneyUnclaimedCount.add(moneyCurrent);
                    }
                } while (mCursor.moveToNext());
                BigDecimal moneyTotalCount = moneyUnclaimedCount.add(moneyClaimedCount).add(moneyPaidCount);
                if (!moneyTotalCount.equals(BigDecimal.ZERO)) {
                    holder.totalView.setText(String.format(Locale.US, "$%.2f", moneyTotalCount));

                    holder.progressBar.setMax(moneyTotalCount.unscaledValue().intValue());
                    holder.progressBar.setProgress(moneyPaidCount.unscaledValue().intValue());
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

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : 3;
        }

    }

}
