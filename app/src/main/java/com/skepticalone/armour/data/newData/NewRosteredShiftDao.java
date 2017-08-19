package com.skepticalone.armour.data.newData;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.skepticalone.armour.data.db.Contract;

import java.util.List;

@Dao
public interface NewRosteredShiftDao {
    @Query("SELECT " +
            Contract.COLUMN_NAME_SHIFT_START +
            ", " +
            Contract.COLUMN_NAME_SHIFT_END +
            " FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_START
    )
    LiveData<List<RawShift>> getShifts();
}