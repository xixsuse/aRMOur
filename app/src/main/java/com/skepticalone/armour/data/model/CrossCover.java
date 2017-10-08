package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.math.BigDecimal;

public final class CrossCover extends Item implements Payment {

    @NonNull
    private final LocalDate date;
    @NonNull
    private final Data paymentData;

    public CrossCover(@NonNull CrossCoverEntity rawCrossCover, @NonNull ZoneId zoneId) {
        super(rawCrossCover.getId(), rawCrossCover.getComment());
        date = rawCrossCover.getDate();
        paymentData = new Data(rawCrossCover.getPaymentData(), zoneId);
    }

    @NonNull
    public LocalDate getDate() {
        return date;
    }

    @NonNull
    @Override
    public Data getPaymentData() {
        return paymentData;
    }

    @NonNull
    @Override
    public BigDecimal getTotalPayment() {
        return getPaymentData().getPayment();
    }

}
