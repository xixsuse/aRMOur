package com.skepticalone.armour.data.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.math.BigDecimal;

public interface Payment {

    @NonNull
    Data getPaymentData();

    @NonNull
    BigDecimal getTotalPayment();

    final class Data {

        @NonNull
        private final BigDecimal payment;

        @Nullable
        private final ZonedDateTime claimed, paid;

        Data(@NonNull com.skepticalone.armour.data.model.PaymentData rawData, @NonNull ZoneId timeZone) {
            payment = rawData.getPayment();
            claimed = rawData.getClaimed() == null ? null : rawData.getClaimed().atZone(timeZone);
            paid = rawData.getPaid() == null ? null : rawData.getPaid().atZone(timeZone);
        }

        @NonNull
        public final BigDecimal getPayment() {
            return payment;
        }

        @Nullable
        public final ZonedDateTime getClaimed() {
            return claimed;
        }

        @Nullable
        public final ZonedDateTime getPaid() {
            return paid;
        }

        @DrawableRes
        public final int getIcon() {
            return paid == null ? claimed == null ? R.drawable.ic_unclaimed_black_24dp : R.drawable.ic_claimed_black_24dp : R.drawable.ic_paid_black_24dp;
        }
    }

}
