package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public abstract class ShiftAddItemViewModel<Entity> extends ItemViewModel<Entity> {

    private final ShiftUtil.Calculator calculator;

    ShiftAddItemViewModel(@NonNull Application application) {
        super(application);
        calculator = new ShiftUtil.Calculator(application);
    }

    @MainThread
    public final void addNewShift(@NonNull ShiftUtil.ShiftType shiftType) {
        addNewShift(calculator.getStartTime(shiftType), calculator.getEndTime(shiftType));
    }

    @MainThread
    abstract void addNewShift(@NonNull LocalTime start, @NonNull LocalTime end);

    @NonNull
    private static DateTime getStart(@NonNull final DateTime lastShiftEndTime, @NonNull final LocalTime startTime) {
        DateTime newStart = lastShiftEndTime.withTime(startTime);
        while (newStart.isBefore(lastShiftEndTime)) {
            newStart = newStart.plusDays(1);
        }
        return newStart;
    }

    @NonNull
    static DateTime getEnd(@NonNull final DateTime start, @NonNull final LocalTime endTime) {
        DateTime end = start.withTime(endTime);
        while (!end.isAfter(start)) end = end.plusDays(1);
        return end;
    }

    @NonNull
    static ShiftData createNewShiftData(@NonNull final LocalTime startTime, @NonNull final LocalTime endTime, @Nullable final DateTime lastShiftEndTime) {
        final DateTime
                newStart = lastShiftEndTime == null ? startTime.toDateTimeToday() : getStart(lastShiftEndTime, startTime),
                newEnd = getEnd(newStart, endTime);
        return new ShiftData(newStart, newEnd);
    }
}