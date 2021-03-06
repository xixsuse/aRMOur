package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.Pair;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.AdditionalShiftEntity;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@SuppressWarnings("NullableProblems")
@Dao
public abstract class AdditionalShiftDao extends ItemDao<AdditionalShiftEntity> implements PaymentDao {

    @Query("UPDATE " +
            Contract.AdditionalShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_SHIFT_START +
            " = :start, " +
            Contract.COLUMN_NAME_SHIFT_END +
            " = :end WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setTimesSync(long id, @NonNull Instant start, @NonNull Instant end);

    @Override
    @Query("UPDATE " +
            Contract.AdditionalShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setCommentSync(long id, @Nullable String comment);

    @Override
    @Query("UPDATE " +
            Contract.AdditionalShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAYMENT +
            " = :payment WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setPaymentSync(long id, @NonNull BigDecimal payment);

    @Override
    @Query("UPDATE " +
            Contract.AdditionalShifts.TABLE_NAME +
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
            Contract.AdditionalShifts.TABLE_NAME +
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
            Contract.AdditionalShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_START)
    public abstract LiveData<List<AdditionalShiftEntity>> fetchItems();

    @NonNull
    @Override
    @Query("SELECT * FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " IN(:ids)")
    abstract List<AdditionalShiftEntity> fetchItemsInternalSync(@NonNull Set<Long> ids);

    @Nullable
    @Query("SELECT " +
            Contract.COLUMN_NAME_SHIFT_END +
            " FROM " +
            Contract.AdditionalShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_END +
            " DESC LIMIT 1")
    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    abstract Instant getLastShiftEndInternalSync();

    @Transaction
    public long insertSync(@NonNull Pair<LocalTime, LocalTime> times, @NonNull ZoneId zoneId, int hourlyRateInCents) {
        return insertSync(
                AdditionalShiftEntity.from(getLastShiftEndInternalSync(), times, zoneId, hourlyRateInCents)
        );
    }

}
