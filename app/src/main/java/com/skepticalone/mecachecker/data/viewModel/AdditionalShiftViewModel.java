package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.AdditionalShiftDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;


public final class AdditionalShiftViewModel extends PayableViewModel<AdditionalShiftEntity, AdditionalShiftDao>
        implements ShiftViewModelContract<AdditionalShiftEntity> {

    public AdditionalShiftViewModel(Application application) {
        super(application);
    }

    @NonNull
    @Override
    AdditionalShiftDao onCreateDao(@NonNull AppDatabase database) {
        return database.additionalShiftDao();
    }

    @Override
    public void addNewShift(@NonNull final ShiftUtil.ShiftType shiftType) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                synchronized (AdditionalShiftViewModel.this) {
                    postSelectedId(getDao().insertItemSync(new AdditionalShiftEntity(
                            PaymentData.fromPayment(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.key_hourly_rate), getApplication().getResources().getInteger(R.integer.default_hourly_rate))),
                            ShiftData.withEarliestStart(ShiftUtil.Calculator.getInstance(getApplication()).getStartTime(shiftType), ShiftUtil.Calculator.getInstance(getApplication()).getEndTime(shiftType), getDao().getLastShiftEndSync(), false),
                            null
                    )));
                }
            }
        });
    }

    @MainThread
    private void saveNewShiftTimes(final long id, @NonNull final ShiftData shiftData) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    getDao().setShiftTimesSync(id, shiftData.getStart(), shiftData.getEnd());
                } catch (SQLiteConstraintException e) {
                    postOverlappingShifts();
                }
            }
        });
    }

    @Override
    public void saveNewDate(@NonNull AdditionalShiftEntity shift, @NonNull LocalDate date) {
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(date));
    }

    @MainThread
    public void saveNewTime(@NonNull AdditionalShiftEntity shift, @NonNull LocalTime time, boolean start) {
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewTime(time, start));
    }

}
