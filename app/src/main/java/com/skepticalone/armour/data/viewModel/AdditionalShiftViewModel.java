package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.dao.AdditionalShiftDao;
import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.model.AdditionalShift;
import com.skepticalone.armour.data.model.AdditionalShiftEntity;
import com.skepticalone.armour.data.model.AdditionalShiftList;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.data.model.ShiftData;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.math.BigDecimal;
import java.util.List;

public final class AdditionalShiftViewModel extends ItemViewModel<AdditionalShiftEntity, AdditionalShift> implements ShiftViewModelContract<AdditionalShift>, PayableViewModelContract<AdditionalShift> {

    @NonNull
    private final LiveData<List<AdditionalShift>> additionalShifts;

    @NonNull
    private final PayableViewModelHelper payableViewModelHelper;

    public AdditionalShiftViewModel(@NonNull Application application) {
        super(application);
        payableViewModelHelper = new PayableViewModelHelper(getDao());
        additionalShifts = new AdditionalShiftList(application, getDao().fetchItems());
    }

    @NonNull
    @Override
    AdditionalShiftDao getDao() {
        return AppDatabase.getInstance(getApplication()).additionalShiftDao();
    }

    @Override
    public void addNewShift(@NonNull final Shift.ShiftType shiftType) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                postSelectedId(getDao().insertSync(
                        shiftType.getTimes(getFreshShiftConfiguration()),
                        getFreshTimezone(),
                        shiftType.getHourlyRateInCents(getApplication())
                ));
            }
        });
    }

    @Override
    public void setClaimed(boolean claimed) {
        payableViewModelHelper.setClaimed(getCurrentItemId(), claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        payableViewModelHelper.setPaid(getCurrentItemId(), paid);
    }

    @Override
    public void saveNewPayment(@NonNull BigDecimal payment) {
        payableViewModelHelper.saveNewPayment(getCurrentItemId(), payment);
    }

    private void saveNewShiftTimes(final long id, @NonNull final ShiftData shiftData) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    getDao().setTimesSync(id, shiftData.getStart(), shiftData.getEnd());
                } catch (SQLiteConstraintException e) {
                    postOverlappingShifts();
                }
            }
        });
    }

    @Override
    public void saveNewDate(@NonNull LocalDate date) {
        AdditionalShift shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(date));
    }

    public void saveNewTime(@NonNull LocalTime time, boolean isStart) {
        AdditionalShift shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewTime(time, isStart));
    }

    @NonNull
    @Override
    public LiveData<List<AdditionalShift>> getItems() {
        return additionalShifts;
    }
}
