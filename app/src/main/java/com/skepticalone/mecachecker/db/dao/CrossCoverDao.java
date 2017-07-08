package com.skepticalone.mecachecker.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.db.entity.CrossCoverEntity;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.COLUMN_NAME_DATE;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.COLUMN_NAME_PAID;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts.TABLE_NAME;
import static com.skepticalone.mecachecker.db.Contract.CrossCoverShifts._ID;

@Dao
public interface CrossCoverDao {
    @Insert
    void insertCrossCoverShift(CrossCoverEntity crossCover);

    @Query("SELECT * FROM " +
            TABLE_NAME +
            " ORDER BY " +
            COLUMN_NAME_DATE)
    LiveData<List<CrossCoverEntity>> getCrossCoverShifts();

    @Query("SELECT * FROM " +
            TABLE_NAME +
            " WHERE " +
            _ID +
            " = :id")
    LiveData<CrossCoverEntity> getCrossCoverShift(long id);

    @Query("UPDATE " +
            TABLE_NAME +
            " SET " +
            COLUMN_NAME_DATE +
            " = :date WHERE " +
            _ID +
            " = :id")
    void setDate(long id, @NonNull LocalDate date);

    @Query("UPDATE " +
            TABLE_NAME +
            " SET " +
            COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            _ID +
            " = :id")
    void setClaimed(long id, @Nullable DateTime claimed);

    @Query("UPDATE " +
            TABLE_NAME +
            " SET " +
            COLUMN_NAME_PAID +
            " = :paid WHERE " +
            _ID +
            " = :id")
    void setPaid(long id, @Nullable DateTime paid);

    @Query("DELETE FROM " +
            TABLE_NAME +
            " WHERE " +
            _ID +
            " = :id")
    void deleteCrossCoverShift(long id);

}
