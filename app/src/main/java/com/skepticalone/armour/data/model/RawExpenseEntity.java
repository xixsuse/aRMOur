package com.skepticalone.armour.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.db.Contract;

@Entity(tableName = Contract.Expenses.TABLE_NAME)
public final class RawExpenseEntity extends Item {
    @NonNull
    @ColumnInfo(name = Contract.Expenses.COLUMN_NAME_TITLE)
    private final String title;
    @NonNull
    @Embedded
    private final RawPaymentData paymentData;
    public RawExpenseEntity(
            long id,
            @Nullable String comment,
            @NonNull String title,
            @NonNull RawPaymentData paymentData
    ) {
        super(id, comment);
        this.title = title;
        this.paymentData = paymentData;
    }

    public static RawExpenseEntity from(@NonNull String title) {
        return new RawExpenseEntity(NO_ID, null, title, RawPaymentData.from(0));
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public RawPaymentData getPaymentData() {
        return paymentData;
    }
//
//    //
////    @NonNull
////    @Override
////    public RawPaymentData getPaymentData() {
////        return paymentData;
////    }
//
//    @NonNull
//    @Override
//    public BigDecimal getTotalPayment() {
//        return paymentData.getPayment();
//    }
}
