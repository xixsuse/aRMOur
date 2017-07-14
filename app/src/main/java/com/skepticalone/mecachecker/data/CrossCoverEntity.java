package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.CrossCover;

import org.joda.time.LocalDate;

@Entity(tableName = Contract.CrossCoverShifts.TABLE_NAME, indices = {@Index(value = {Contract.CrossCoverShifts.COLUMN_NAME_DATE}, unique = true)})
public class CrossCoverEntity extends PayableItemEntity implements CrossCover {
    @NonNull
    @ColumnInfo(name = Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    private final LocalDate date;
    CrossCoverEntity(
            @Nullable String comment,
            @NonNull PaymentData paymentData,
            @NonNull LocalDate date
    ) {
        super(comment, paymentData);
        this.date = date;
    }
    @NonNull
    @Override
    public LocalDate getDate() {
        return date;
    }
}
