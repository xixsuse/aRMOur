package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.util.ShiftUtil;

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

}