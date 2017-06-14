package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.WithCompliance;
import com.skepticalone.mecachecker.data.Compliance;
import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.Duration;
import org.joda.time.Interval;

class ComplianceData extends AbstractData {

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
    private Interval mIntervalBetweenShifts, mPreviousWeekend;
    private Duration mDurationOverDay, mDurationOverWeek, mDurationOverFortnight;
    private boolean mConsecutiveWeekendsWorked;
    private boolean mIsWeekend;

    ComplianceData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    private void showMessage(@NonNull String message) {
        mCallbacks.showDialogFragment(MessageFragment.newInstance(message), LifecycleConstants.MESSAGE_DIALOG);
    }

    @Nullable
    @Override
    ViewHolderType getViewHolderType(int position) {
        if (position == mCallbacks.getRowNumberIntervalBetweenShifts() || position == mCallbacks.getRowNumberDurationWorkedOverDay() || position == mCallbacks.getRowNumberDurationWorkedOverWeek() || position == mCallbacks.getRowNumberDurationWorkedOverFortnight() || position == mCallbacks.getRowNumberLastWeekendWorked()) {
            return ViewHolderType.PLAIN;
        } else {
            return null;
        }
    }

    boolean isWeekend() {
        return mIsWeekend;
    }

    @Override
    void readFromPositionedCursor(@NonNull Cursor originalCursor) {
        Compliance.Wrapper cursor = new Compliance.Wrapper(originalCursor);
        mIntervalBetweenShifts = cursor.getIntervalBetweenShifts();
        mDurationOverDay = cursor.getDurationOverDay();
        mDurationOverWeek = cursor.getDurationOverWeek();
        mDurationOverFortnight = cursor.getDurationOverFortnight();
        mPreviousWeekend = cursor.getPreviousWeekend();
        mConsecutiveWeekendsWorked = cursor.consecutiveWeekendsWorked();
        mIsWeekend = cursor.getCurrentWeekend() != null;
    }

    @Override
    boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        boolean hasError;
        if (position == mCallbacks.getRowNumberIntervalBetweenShifts()) {
            holder.rootBind(context, 0, R.string.time_between_shifts, mIntervalBetweenShifts == null ? context.getString(R.string.not_applicable) : DateTimeUtils.getPeriodString(mIntervalBetweenShifts), mIntervalBetweenShiftsListener);
            hasError = AppConstants.hasInsufficientIntervalBetweenShifts(mIntervalBetweenShifts);
        } else if (position == mCallbacks.getRowNumberDurationWorkedOverDay()) {
            holder.rootBind(context, 0, R.string.duration_worked_over_day, DateTimeUtils.getPeriodString(mDurationOverDay), mDurationOverDayListener);
            hasError = AppConstants.exceedsDurationOverDay(mDurationOverDay);
        } else if (position == mCallbacks.getRowNumberDurationWorkedOverWeek()) {
            holder.rootBind(context, 0, R.string.duration_worked_over_week, DateTimeUtils.getPeriodString(mDurationOverWeek), mDurationOverWeekListener);
            hasError = AppConstants.exceedsDurationOverWeek(mDurationOverWeek);
        } else if (position == mCallbacks.getRowNumberDurationWorkedOverFortnight()) {
            holder.rootBind(context, 0, R.string.duration_worked_over_fortnight, DateTimeUtils.getPeriodString(mDurationOverFortnight), mDurationOverFortnightListener);
            hasError = AppConstants.exceedsDurationOverFortnight(mDurationOverFortnight);
        } else if (position == mCallbacks.getRowNumberLastWeekendWorked()) {
            holder.rootBind(context, 0, R.string.last_weekend_worked, mPreviousWeekend == null ? context.getString(R.string.not_applicable) : DateTimeUtils.getDateSpanString(mPreviousWeekend), mPreviousWeekendListener);
            hasError = mConsecutiveWeekendsWorked;
        } else return false;
        holder.secondaryIcon.setImageResource(hasError ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
        return true;
    }

    interface Callbacks extends DetailFragmentBehaviour, WithCompliance {
    }
}
