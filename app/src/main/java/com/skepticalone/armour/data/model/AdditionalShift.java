package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.armour.util.MoneyConverter;

import org.threeten.bp.ZoneId;

import java.math.BigDecimal;

public final class AdditionalShift extends Shift implements Payment {

    @NonNull
    private final Payment.Data paymentData;
    @NonNull
    private final BigDecimal totalPayment;

    public AdditionalShift(@NonNull AdditionalShiftEntity rawShift, @NonNull ZoneId zoneId, @NonNull ShiftType.Configuration configuration) {
        super(rawShift.getId(), rawShift.getComment(), rawShift.getShiftData(), zoneId, configuration);
        paymentData = new Payment.Data(rawShift.getPaymentData(), zoneId);
        totalPayment = MoneyConverter.hourlyRateToTotal(getPaymentData().getPayment(), getShiftData().getDuration());
    }

    @NonNull
    @Override
    public Payment.Data getPaymentData() {
        return paymentData;
    }

    @NonNull
    @Override
    public BigDecimal getTotalPayment() {
        return totalPayment;
    }
}
