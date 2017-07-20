package com.skepticalone.mecachecker.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.model.RosteredShift;
import com.skepticalone.mecachecker.data.util.ShiftData;

@Entity(tableName = Contract.RosteredShifts.TABLE_NAME, indices = {@Index(name = Contract.RosteredShifts.INDEX, value = {Contract.COLUMN_NAME_SHIFT_START})})
public final class RosteredShiftEntity extends ItemEntity implements RosteredShift {

    @NonNull
    @Embedded
    private final ShiftData shiftData;
    @Nullable
    @Embedded(prefix = Contract.RosteredShifts.LOGGED_PREFIX)
    private final ShiftData loggedShiftData;

    public RosteredShiftEntity(
            @NonNull ShiftData shiftData,
            @Nullable ShiftData loggedShiftData,
            @Nullable String comment
    ) {
        super(comment);
        this.shiftData = shiftData;
        this.loggedShiftData = loggedShiftData;
    }

    @NonNull
    @Override
    public ShiftData getShiftData() {
        return shiftData;
    }

    @Nullable
    @Override
    public ShiftData getLoggedShiftData() {
        return loggedShiftData;
    }

}
