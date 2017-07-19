package com.skepticalone.mecachecker.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.model.RosteredShift;
import com.skepticalone.mecachecker.data.util.ShiftData;

@Entity(tableName = Contract.AdditionalShifts.TABLE_NAME, indices = {@Index(name = Contract.AdditionalShifts.INDEX, value = {Contract.COLUMN_NAME_SHIFT_START})})
public final class RosteredShiftEntity extends ItemEntity implements RosteredShift {

    @NonNull
    @Embedded
    private final ShiftData shift;
    @Nullable
    @Embedded(prefix = Contract.RosteredShifts.LOGGED_PREFIX)
    private final ShiftData loggedShift;

    public RosteredShiftEntity(
            @NonNull ShiftData shift,
            @Nullable ShiftData loggedShift,
            @Nullable String comment
    ) {
        super(comment);
        this.shift = shift;
        this.loggedShift = loggedShift;
    }

    @NonNull
    @Override
    public ShiftData getShiftData() {
        return shift;
    }

    @Nullable
    @Override
    public ShiftData getLoggedShiftData() {
        return loggedShift;
    }

}
