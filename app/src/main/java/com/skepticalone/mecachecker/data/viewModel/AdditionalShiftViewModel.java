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

import java.math.BigDecimal;


public final class AdditionalShiftViewModel extends ItemViewModel<AdditionalShiftEntity, AdditionalShiftDao>
        implements ShiftViewModelContract<AdditionalShiftEntity>, PayableViewModelContract<AdditionalShiftEntity> {

    private final PayableHelper payableHelper;

    public AdditionalShiftViewModel(Application application) {
        super(application);
        payableHelper = new PayableHelper(getDao());
    }

    @NonNull
    @Override
    AdditionalShiftDao onCreateDao(@NonNull AppDatabase database) {
        return database.additionalShiftDao();
    }

    @Override
    public void saveNewPayment(@NonNull BigDecimal payment) {
        payableHelper.saveNewPayment(getCurrentItemId(), payment);
    }

    @Override
    public void setClaimed(boolean claimed) {
        payableHelper.setClaimed(getCurrentItemId(), claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        payableHelper.setPaid(getCurrentItemId(), paid);
    }


    @Override
    public void addNewShift(@NonNull final ShiftUtil.ShiftType shiftType) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                selectedId.postValue(getDao().insertItemSync(new AdditionalShiftEntity(
                        PaymentData.fromPayment(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(R.string.key_hourly_rate), getApplication().getResources().getInteger(R.integer.default_hourly_rate))),
                        ShiftData.withEarliestStart(ShiftUtil.Calculator.getInstance(getApplication()).getStartTime(shiftType), ShiftUtil.Calculator.getInstance(getApplication()).getEndTime(shiftType), getDao().getLastShiftEndSync()),
                        null
                )));
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
                    errorMessage.postValue(R.string.overlapping_shifts);
                }
            }
        });
    }

    @Override
    public void saveNewDate(@NonNull LocalDate newDate) {
        AdditionalShiftEntity shift = getCurrentItemSync();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(newDate));
    }

    @MainThread
    public void saveNewTime(@NonNull LocalTime time, boolean start) {
        AdditionalShiftEntity shift = getCurrentItemSync();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewTime(time, start));
    }

}
