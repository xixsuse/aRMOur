package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.util.ShiftType;

import org.joda.time.LocalTime;

import java.util.List;

public final class AdditionalShiftViewModel extends ItemViewModel<AdditionalShiftEntity>
        implements ShiftViewModel<AdditionalShiftEntity>, PayableItemViewModel<AdditionalShiftEntity> {

    private final AdditionalShiftDao additionalShiftDao;

    AdditionalShiftViewModel(Application application) {
        super(application);
        additionalShiftDao = AppDatabase.getInstance(application).additionalShiftDao();
    }

    @Override
    public LiveData<List<AdditionalShiftEntity>> getItems() {
        return null;
    }

    @Override
    public LiveData<AdditionalShiftEntity> getItem(long id) {
        return null;
    }

    @Override
    public void addShift(@NonNull ShiftType shiftType) {

    }

    @Override
    public void deleteItem(long id) {

    }

    @Override
    public void setStart(long id, @NonNull LocalTime start) {

    }

    @Override
    public void setEnd(long id, @NonNull LocalTime end) {

    }

    @Override
    public void setClaimed(long id, boolean claimed) {

    }

    @Override
    public void setPaid(long id, boolean paid) {

    }

    @Override
    public void setComment(long id, @Nullable String comment) {

    }

}
