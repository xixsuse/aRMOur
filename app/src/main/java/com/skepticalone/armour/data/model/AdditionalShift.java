package com.skepticalone.armour.data.model;

import android.support.annotation.NonNull;

import com.skepticalone.armour.data.util.MoneyConverter;

import org.threeten.bp.ZoneId;

import java.math.BigDecimal;

public final class AdditionalShift extends Shift implements Payable {

    @NonNull
    private final PaymentData paymentData;
    @NonNull
    private final BigDecimal totalPayment;

    public AdditionalShift(@NonNull RawAdditionalShiftEntity rawShift, @NonNull ZoneId zoneId, @NonNull ShiftType.Configuration configuration) {
        super(rawShift, zoneId, configuration);
        paymentData = new PaymentData(rawShift.getPaymentData(), zoneId);
        totalPayment = MoneyConverter.hourlyRateToTotal(getPaymentData().getPayment(), getShiftData().getDuration());
    }

    @NonNull
    @Override
    public PaymentData getPaymentData() {
        return paymentData;
    }

    @NonNull
    @Override
    public BigDecimal getTotalPayment() {
        return totalPayment;
    }
}
