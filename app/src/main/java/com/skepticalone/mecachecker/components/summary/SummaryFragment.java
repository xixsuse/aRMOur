package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import java.math.BigDecimal;
import java.util.Locale;

public class SummaryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

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

    private TextView
            shiftsUnclaimed, shiftsClaimed, shiftsPaid, shiftsTotal,
            hoursUnclaimed, hoursClaimed, hoursPaid, hoursTotal,
            moneyUnclaimed, moneyClaimed, moneyPaid, moneyTotal;
    private PieView
            shiftsPie, hoursPie, moneyPie;
    private ProgressBar
            shiftsProgress, hoursProgress, moneyProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.summary_list_fragment, container, false);
        View shiftsLayout = layout.findViewById(R.id.shifts);
        ((TextView) shiftsLayout.findViewById(R.id.title)).setText(R.string.shifts);
        shiftsUnclaimed = (TextView) shiftsLayout.findViewById(R.id.unclaimed);
        shiftsClaimed = (TextView) shiftsLayout.findViewById(R.id.claimed);
        shiftsPaid = (TextView) shiftsLayout.findViewById(R.id.paid);
        shiftsTotal = (TextView) shiftsLayout.findViewById(R.id.total);
        shiftsPie = (PieView) shiftsLayout.findViewById(R.id.pie);
        shiftsProgress = (ProgressBar) shiftsLayout.findViewById(R.id.progress);
        View hoursLayout = layout.findViewById(R.id.hours);
        ((TextView) hoursLayout.findViewById(R.id.title)).setText(R.string.hours);
        hoursUnclaimed = (TextView) hoursLayout.findViewById(R.id.unclaimed);
        hoursClaimed = (TextView) hoursLayout.findViewById(R.id.claimed);
        hoursPaid = (TextView) hoursLayout.findViewById(R.id.paid);
        hoursTotal = (TextView) hoursLayout.findViewById(R.id.total);
        hoursPie = (PieView) hoursLayout.findViewById(R.id.pie);
        hoursProgress = (ProgressBar) hoursLayout.findViewById(R.id.progress);
        View moneyLayout = layout.findViewById(R.id.money);
        ((TextView) moneyLayout.findViewById(R.id.title)).setText(R.string.money);
        moneyUnclaimed = (TextView) moneyLayout.findViewById(R.id.unclaimed);
        moneyClaimed = (TextView) moneyLayout.findViewById(R.id.claimed);
        moneyPaid = (TextView) moneyLayout.findViewById(R.id.paid);
        moneyTotal = (TextView) moneyLayout.findViewById(R.id.total);
        moneyPie = (PieView) moneyLayout.findViewById(R.id.pie);
        moneyProgress = (ProgressBar) moneyLayout.findViewById(R.id.progress);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(SummaryActivity.LOADER_ID_ADDITIONAL, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
//            case SummaryActivity.LOADER_ID_ROSTERED:
//                break;
            case SummaryActivity.LOADER_ID_ADDITIONAL:
                return new CursorLoader(getActivity(), Provider.additionalShiftsUri, PROJECTION, null, null, Contract.AdditionalShifts.COLUMN_NAME_START);
//            case SummaryActivity.LOADER_ID_CROSS_COVER:
//                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case SummaryActivity.LOADER_ID_ADDITIONAL:
                int
                        shiftsUnclaimedCount = 0, shiftsClaimedCount = 0, shiftsPaidCount = 0;
                BigDecimal
                        moneyUnclaimedCount = BigDecimal.ZERO, moneyClaimedCount = BigDecimal.ZERO, moneyPaidCount = BigDecimal.ZERO;
                Duration
                        durationUnclaimed = Duration.ZERO, durationClaimed = Duration.ZERO, durationPaid = Duration.ZERO;

                if (cursor.moveToFirst()) {
                    do {
                        Duration durationCurrent = new Duration(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END));
                        BigDecimal moneyCurrent = BigDecimal.valueOf(cursor.getInt(COLUMN_INDEX_RATE), 2)
                                .multiply(BigDecimal.valueOf(durationCurrent.getMillis()))
                                .divide(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_HOUR), 2, BigDecimal.ROUND_HALF_UP);
                        if (!cursor.isNull(COLUMN_INDEX_PAID)) {
                            shiftsPaidCount++;
                            durationPaid = durationPaid.plus(durationCurrent);
                            moneyPaidCount = moneyPaidCount.add(moneyCurrent);
                        } else if (!cursor.isNull(COLUMN_INDEX_CLAIMED)) {
                            shiftsClaimedCount++;
                            durationClaimed = durationClaimed.plus(durationCurrent);
                            moneyClaimedCount = moneyClaimedCount.add(moneyCurrent);
                        } else {
                            shiftsUnclaimedCount++;
                            durationUnclaimed = durationUnclaimed.plus(durationCurrent);
                            moneyUnclaimedCount = moneyUnclaimedCount.add(moneyCurrent);
                        }
                    } while (cursor.moveToNext());
                }

                int shiftsTotalCount = shiftsUnclaimedCount + shiftsClaimedCount + shiftsPaidCount;
                shiftsTotal.setText(String.format(Locale.US, "%d", shiftsTotalCount));
                if (shiftsTotalCount != 0) {
                    shiftsProgress.setMax(shiftsTotalCount);
                    shiftsProgress.setProgress(shiftsPaidCount);
                    shiftsUnclaimed.setText(String.format(Locale.US, "Unclaimed: %d (%d%%)", shiftsUnclaimedCount, 100 * shiftsUnclaimedCount / shiftsTotalCount));
                    shiftsClaimed.setText(String.format(Locale.US, "Claimed: %d (%d%%)", shiftsClaimedCount, 100 * shiftsClaimedCount / shiftsTotalCount));
                    shiftsPaid.setText(String.format(Locale.US, "Paid: %d (%d%%)", shiftsPaidCount, 100 * shiftsPaidCount / shiftsTotalCount));
                    shiftsPie.set(shiftsUnclaimedCount, shiftsClaimedCount, shiftsPaidCount);
                }

                Duration durationTotal = durationUnclaimed.plus(durationClaimed).plus(durationPaid);
                hoursTotal.setText(String.format(Locale.US, "%.1f", (float) durationTotal.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR));
                if (!durationTotal.isEqual(Duration.ZERO)) {
                    hoursProgress.setMax(100);
                    hoursProgress.setProgress((int) (100 * durationPaid.getMillis() / durationTotal.getMillis()));
                    hoursUnclaimed.setText(String.format(Locale.US, "Unclaimed: %.1f (%d%%)", (float) durationUnclaimed.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * durationUnclaimed.getMillis() / durationTotal.getMillis()));
                    hoursClaimed.setText(String.format(Locale.US, "Claimed: %.1f (%d%%)", (float) durationClaimed.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * durationClaimed.getMillis() / durationTotal.getMillis()));
                    hoursPaid.setText(String.format(Locale.US, "Paid: %.1f (%d%%)", (float) durationPaid.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * durationPaid.getMillis() / durationTotal.getMillis()));
                    hoursPie.set(durationUnclaimed.getMillis(), durationClaimed.getMillis(), durationPaid.getMillis());
                }

                BigDecimal moneyTotalCount = moneyUnclaimedCount.add(moneyClaimedCount).add(moneyPaidCount);
                moneyTotal.setText(String.format(Locale.US, "$%.2f", moneyTotalCount));
                if (!moneyTotalCount.equals(BigDecimal.ZERO)) {
                    moneyProgress.setMax(moneyTotalCount.unscaledValue().intValue());
                    moneyProgress.setProgress(moneyPaidCount.unscaledValue().intValue());
                    moneyUnclaimed.setText(String.format(Locale.US, "Unclaimed: $%.2f (%d%%)", moneyUnclaimedCount, moneyUnclaimedCount.scaleByPowerOfTen(2).divide(moneyTotalCount, 0, BigDecimal.ROUND_HALF_UP).intValue()));
                    moneyClaimed.setText(String.format(Locale.US, "Claimed: $%.2f (%d%%)", moneyClaimedCount, moneyClaimedCount.scaleByPowerOfTen(2).divide(moneyTotalCount, 0, BigDecimal.ROUND_HALF_UP).intValue()));
                    moneyPaid.setText(String.format(Locale.US, "Paid: $%.2f (%d%%)", moneyPaidCount, moneyPaidCount.scaleByPowerOfTen(2).divide(moneyTotalCount, 0, BigDecimal.ROUND_HALF_UP).intValue()));
                    moneyPie.set(moneyUnclaimedCount.longValue(), moneyClaimedCount.longValue(), moneyPaidCount.longValue());
                }

                break;
            default:
                throw new IllegalArgumentException();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}