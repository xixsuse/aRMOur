package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.util.ShiftType;

import org.joda.time.LocalTime;

import java.math.BigDecimal;

public final class AdditionalShiftViewModel extends ItemViewModel<AdditionalShiftEntity>
        implements ShiftViewModel<AdditionalShiftEntity>, PayableItemViewModel<AdditionalShiftEntity> {

    private final AdditionalShiftDao additionalShiftDao;

    AdditionalShiftViewModel(Application application) {
        super(application);
        additionalShiftDao = AppDatabase.getInstance(application).additionalShiftDao();
    }

    @Override
    public void setPayment(long id, @NonNull BigDecimal payment) {

    }

    @Override
    public void addShift(@NonNull ShiftType shiftType) {

    }

    @Override
    public void setClaimed(long id, boolean claimed) {

    }

    @Override
    public void setStart(long id, @NonNull LocalTime start) {

    }

    @Override
    public void setPaid(long id, boolean paid) {

    }

    @Override
    public void setEnd(long id, @NonNull LocalTime end) {

    }

    @Override
    BaseItemDao<AdditionalShiftEntity> getDao() {
        return null;
    }
}
