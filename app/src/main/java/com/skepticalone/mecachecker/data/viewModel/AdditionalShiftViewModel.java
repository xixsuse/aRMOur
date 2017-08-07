package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.AdditionalShiftDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.math.BigDecimal;


public final class AdditionalShiftViewModel extends ItemViewModel<AdditionalShiftEntity> implements ShiftViewModelContract<AdditionalShiftEntity>, PayableViewModelContract<AdditionalShiftEntity> {

    @NonNull
    private final PayableViewModelHelper payableViewModelHelper;

    public AdditionalShiftViewModel(@NonNull Application application) {
        super(application);
        this.payableViewModelHelper = new PayableViewModelHelper(getDao().getPayableDaoHelper());
    }

    @NonNull
    @Override
    AdditionalShiftDao getDao() {
        return AppDatabase.getInstance(getApplication()).additionalShiftDao();
    }

    private int getPaymentInCents(@NonNull ShiftUtil.ShiftType shiftType) {
        @StringRes final int hourlyRateKey;
        @IntegerRes final int hourlyRateDefault;
        switch (shiftType) {
            case NORMAL_DAY:
                hourlyRateKey = R.string.key_default_hourly_rate_normal_day;
                hourlyRateDefault = R.integer.default_hourly_rate_normal_hours;
                break;
            case LONG_DAY:
                hourlyRateKey = R.string.key_default_hourly_rate_long_day;
                hourlyRateDefault = R.integer.default_hourly_rate_normal_hours;
                break;
            case NIGHT_SHIFT:
                hourlyRateKey = R.string.key_default_hourly_rate_night_shift;
                hourlyRateDefault = R.integer.default_hourly_rate_after_hours;
                break;
            default:
                throw new IllegalStateException();
        }
        return PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(hourlyRateKey), getApplication().getResources().getInteger(hourlyRateDefault));
    }

    @Override
    public void addNewShift(@NonNull final ShiftUtil.ShiftType shiftType) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                ShiftUtil.Calculator calculator = ShiftUtil.Calculator.getInstance(getApplication());
                postSelectedId(getDao().insertSync(
                        calculator.getStartTime(shiftType),
                        calculator.getEndTime(shiftType),
                        getPaymentInCents(shiftType)
                ));
            }
        });
    }

    @MainThread
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
        AdditionalShiftEntity shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(date));
    }

    @MainThread
    public void saveNewTime(@NonNull LocalTime time, boolean start) {
        AdditionalShiftEntity shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewTime(time, start));
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

}
