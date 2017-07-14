package com.skepticalone.mecachecker.data;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.Comparators;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public final class PaymentData {

    @NonNull
    @ColumnInfo(name = Contract.COLUMN_NAME_PAYMENT)
    private final BigDecimal payment;

    @Nullable
    @ColumnInfo(name = Contract.COLUMN_NAME_CLAIMED)
    private final DateTime claimed;

    @Nullable
    @ColumnInfo(name = Contract.COLUMN_NAME_PAID)
    private final DateTime paid;
//
//    @Nullable
//    @ColumnInfo(name = Contract.COLUMN_NAME_COMMENT)
//    private final String comment;

    PaymentData(
            @NonNull BigDecimal payment,
            @Nullable DateTime claimed,
            @Nullable DateTime paid
//            @Nullable String comment
    ) {
        this.payment = payment;
        this.claimed = claimed;
        this.paid = claimed == null ? null : paid;
//        this.comment = comment;
    }

    @NonNull
    public BigDecimal getPayment() {
        return payment;
    }

    @Nullable
    public DateTime getClaimed() {
        return claimed;
    }

    @Nullable
    public DateTime getPaid() {
        return paid;
    }
//
//    @Nullable
//    public String getComment() {
//        return comment;
//    }

    @DrawableRes
    public int getIcon() {
        return paid == null ? claimed == null ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof PaymentData) {
            PaymentData other = (PaymentData) object;
            return
                    Comparators.equalBigDecimals(payment, other.payment) &&
                            Comparators.equalDateTimes(claimed, other.claimed) &&
                            Comparators.equalDateTimes(paid, other.paid);
//            &&
//                            Comparators.equalStrings(comment, other.comment);
        }
        return false;
    }
}
