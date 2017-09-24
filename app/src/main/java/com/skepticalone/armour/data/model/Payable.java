package com.skepticalone.armour.data.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.math.BigDecimal;

public interface Payable {

    @NonNull
    PaymentData getPaymentData();

    @NonNull
    BigDecimal getTotalPayment();

    final class PaymentData {

        @NonNull
        private final BigDecimal payment;

        @Nullable
        private final ZonedDateTime claimed, paid;

        @DrawableRes
        private final int icon;

        PaymentData(@NonNull RawPaymentData rawData, @NonNull ZoneId zoneId) {
            payment = rawData.getPayment();
            claimed = rawData.getClaimed() == null ? null : rawData.getClaimed().atZone(zoneId);
            paid = rawData.getPaid() == null ? null : rawData.getPaid().atZone(zoneId);
            icon = paid == null ? claimed == null ? R.drawable.unclaimed_black_24dp : R.drawable.claimed_black_24dp : R.drawable.paid_black_24dp;
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
            return icon;
        }
    }

}
