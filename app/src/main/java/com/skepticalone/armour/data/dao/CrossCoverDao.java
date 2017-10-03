package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.RawCrossCoverEntity;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.math.BigDecimal;
import java.util.List;

@Dao
public abstract class CrossCoverDao extends ItemDao<RawCrossCoverEntity> implements PayableDao {

    CrossCoverDao(@NonNull AppDatabase database) {
        super(database);
    }

    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " = :date WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setDateSync(long id, @NonNull LocalDate date);

    @Override
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setCommentSync(long id, @Nullable String comment);

    @Override
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAYMENT +
            " = :payment WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setPaymentSync(long id, @NonNull BigDecimal payment);

    @Override
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            BaseColumns._ID +
            " = :id AND " +
            Contract.COLUMN_NAME_PAID +
            " IS NULL")
    public abstract void setClaimedSync(long id, @Nullable Instant claimed);

    @Override
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAID +
            " = :paid WHERE " +
            BaseColumns._ID +
            " = :id AND " +
            Contract.COLUMN_NAME_CLAIMED +
            " IS NOT NULL")
    public abstract void setPaidSync(long id, @Nullable Instant paid);

    @NonNull
    @Override
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    public abstract LiveData<List<RawCrossCoverEntity>> fetchItems();

    @Nullable
    @Override
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract RawCrossCoverEntity fetchItemInternalSync(long id);

    @Nullable
    @Query("SELECT " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " DESC LIMIT 1")
    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    abstract LocalDate getLastShiftDateInternalSync();

    @Transaction
    public long insertSync(@NonNull ZoneId timeZone, int paymentInCents) {
        return insertSync(RawCrossCoverEntity.from(getLastShiftDateInternalSync(), timeZone, paymentInCents));
    }

}
