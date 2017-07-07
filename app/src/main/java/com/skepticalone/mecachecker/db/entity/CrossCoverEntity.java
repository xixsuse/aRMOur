package com.skepticalone.mecachecker.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.CrossCover;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

@Entity(tableName = "cross_cover")
public class CrossCoverEntity implements CrossCover {
//    @Ignore
//    public static final String
//            SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `cross_cover` (" +
//            "`id` INTEGER PRIMARY KEY, " +
//            "`date` INTEGER NOT NULL, " +
//            "`payment` INTEGER NOT NULL, " +
//            "`comment` TEXT DEFAULT NULL, " +
//            "`claimed` INTEGER DEFAULT NULL, " +
//            "`paid` INTEGER DEFAULT NULL, " +
//            "CHECK (`paid` IS NULL OR `claimed` IS NOT NULL), " +
//            "CHECK (`claimed` <= `paid`), " +
//            "CHECK (length(`comment`) > 0)" +
//            ")",
//            SQL_DROP_TABLE = "DROP TABLE IF EXISTS `cross_cover`";

    @NonNull
    private final LocalDate date;
    @NonNull
    private final BigDecimal payment;
    @Nullable
    private final String comment;
    @PrimaryKey(autoGenerate = true)
    private long id = 0L;
    @Nullable
    private DateTime claimed = null;
    @Nullable
    private DateTime paid = null;

    public CrossCoverEntity(
            @NonNull LocalDate date,
            @NonNull BigDecimal payment,
            @Nullable String comment) {
        this.date = date;
        this.payment = payment;
        this.comment = comment;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    @Override
    public LocalDate getDate() {
        return date;
    }

    @NonNull
    @Override
    public BigDecimal getPayment() {
        return payment;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Nullable
    @Override
    public DateTime getClaimed() {
        return claimed;
    }

    public void setClaimed(@Nullable DateTime claimed) {
        this.claimed = claimed;
    }

    @Nullable
    @Override
    public DateTime getPaid() {
        return paid;
    }

    public void setPaid(@Nullable DateTime paid) {
        this.paid = paid;
    }
}
