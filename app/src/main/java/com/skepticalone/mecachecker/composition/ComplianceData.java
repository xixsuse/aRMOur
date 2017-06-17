package com.skepticalone.mecachecker.composition;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.WithCompliance;
import com.skepticalone.mecachecker.components.ListItemViewHolder;
import com.skepticalone.mecachecker.data.Compliance;
import com.skepticalone.mecachecker.dialog.MessageDialogFragment;
import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.LifecycleConstants;

import org.joda.time.Duration;
import org.joda.time.LocalDate;

public class ComplianceData extends AbstractData {

    private final Callbacks mCallbacks;
    @NonNull
    private final View.OnClickListener
            mIntervalBetweenShiftsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showMessage(mCallbacks.getMecaIntervalBetweenShifts());
        }
    },
            mDurationOverDayListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMessage(mCallbacks.getMecaDurationOverDay());
                }
            },
            mDurationOverWeekListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMessage(mCallbacks.getMecaDurationOverWeek());
                }
            },
            mDurationOverFortnightListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMessage(mCallbacks.getMecaDurationOverFortnight());
                }
            },
            mPreviousWeekendListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMessage(mCallbacks.getMecaPreviousWeekend());
                }
            };
    @Nullable
    private LocalDate mPreviousWeekend;
    @Nullable
    private Duration mDurationBetweenShifts;
    private Duration mDurationOverDay, mDurationOverWeek, mDurationOverFortnight;
    private boolean mConsecutiveWeekendsWorked;
    private boolean mIsWeekend;

    public ComplianceData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    private void showMessage(@NonNull String message) {
        mCallbacks.showDialogFragment(MessageDialogFragment.newInstance(message), LifecycleConstants.MESSAGE_DIALOG);
    }

    public boolean isWeekend() {
        return mIsWeekend;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor originalCursor) {
        Compliance.Wrapper cursor = new Compliance.Wrapper(originalCursor);
        mDurationBetweenShifts = cursor.getDurationBetweenShifts();
        mDurationOverDay = cursor.getDurationOverDay();
        mDurationOverWeek = cursor.getDurationOverWeek();
        mDurationOverFortnight = cursor.getDurationOverFortnight();
        mIsWeekend = cursor.isWeekend();
        mPreviousWeekend = mIsWeekend ? cursor.getPreviousWeekend() : null;
        mConsecutiveWeekendsWorked = mIsWeekend && cursor.consecutiveWeekendsWorked();
    }

    @Override
    public boolean bindToHolder(Context context, ListItemViewHolder holder, int position) {
        int primaryIconRes;
        int keyRes;
        String value;
        View.OnClickListener listener;
        boolean hasError;
        if (position == mCallbacks.getRowNumberIntervalBetweenShifts()) {
            primaryIconRes = R.drawable.ic_sleep_black_24dp;
            keyRes = R.string.time_between_shifts;
            value = mDurationBetweenShifts == null ? context.getString(R.string.not_applicable) : DateTimeUtils.getPeriodString(mDurationBetweenShifts);
            listener = mIntervalBetweenShiftsListener;
            hasError = AppConstants.hasInsufficientDurationBetweenShifts(mDurationBetweenShifts);
        } else if (position == mCallbacks.getRowNumberDurationWorkedOverDay()) {
            primaryIconRes = R.drawable.ic_duration_black_24dp;
            keyRes = R.string.duration_worked_over_day;
            value = DateTimeUtils.getPeriodString(mDurationOverDay);
            listener = mDurationOverDayListener;
            hasError = AppConstants.exceedsDurationOverDay(mDurationOverDay);
        } else if (position == mCallbacks.getRowNumberDurationWorkedOverWeek()) {
            primaryIconRes = R.drawable.ic_week_black_24dp;
            keyRes = R.string.duration_worked_over_week;
            value = DateTimeUtils.getPeriodString(mDurationOverWeek);
            listener = mDurationOverWeekListener;
            hasError = AppConstants.exceedsDurationOverWeek(mDurationOverWeek);
        } else if (position == mCallbacks.getRowNumberDurationWorkedOverFortnight()) {
            primaryIconRes = R.drawable.ic_fortnight_black_24dp;
            keyRes = R.string.duration_worked_over_fortnight;
            value = DateTimeUtils.getPeriodString(mDurationOverFortnight);
            listener = mDurationOverFortnightListener;
            hasError = AppConstants.exceedsDurationOverFortnight(mDurationOverFortnight);
        } else if (position == mCallbacks.getRowNumberLastWeekendWorked()) {
            primaryIconRes = R.drawable.ic_weekend_black_24dp;
            keyRes = R.string.last_weekend_worked;
            value = mPreviousWeekend == null ? context.getString(R.string.not_applicable) : DateTimeUtils.getDateSpanString(mPreviousWeekend, mPreviousWeekend.plusDays(1));
            listener = mPreviousWeekendListener;
            hasError = mConsecutiveWeekendsWorked;
        } else return false;
        holder.bindPlain(primaryIconRes, context.getString(keyRes), value, null, hasError ? R.drawable.ic_cancel_red_24dp : R.drawable.ic_check_black_24dp);
        holder.itemView.setOnClickListener(listener);
        return true;
    }

    public interface Callbacks extends DetailFragmentBehaviour, WithCompliance {
    }
}
