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

@Dao
public interface CrossCoverDao {
    @Insert
    void insertCrossCoverShift(CrossCoverEntity crossCover);

    @Query("SELECT * FROM cross_cover ORDER BY date")
    LiveData<List<CrossCoverEntity>> getCrossCoverShifts();

    @Query("SELECT * FROM cross_cover WHERE id = :id")
    LiveData<CrossCoverEntity> getCrossCoverShift(long id);

    @Query("UPDATE cross_cover SET date = :date WHERE id = :id")
    void setDate(long id, @NonNull LocalDate date);

    @Query("UPDATE cross_cover SET claimed = :claimed WHERE id = :id")
    void setClaimed(long id, @Nullable DateTime claimed);

    @Query("UPDATE cross_cover SET paid = :paid WHERE id = :id")
    void setPaid(long id, @Nullable DateTime paid);

    @Query("DELETE FROM cross_cover WHERE id = :id")
    void deleteCrossCoverShift(long id);

}
