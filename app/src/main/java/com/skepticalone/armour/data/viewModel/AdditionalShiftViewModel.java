package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.AdditionalShiftDao;
import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.entity.AdditionalShiftEntity;
import com.skepticalone.armour.data.entity.LiveAdditionalShifts;
import com.skepticalone.armour.data.entity.LiveShiftConfig;
import com.skepticalone.armour.data.entity.ShiftData;
import com.skepticalone.armour.util.ShiftType;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.math.BigDecimal;
import java.util.List;


public final class AdditionalShiftViewModel extends ItemViewModel<AdditionalShiftEntity> implements ShiftViewModelContract<AdditionalShiftEntity>, PayableViewModelContract<AdditionalShiftEntity> {

    @NonNull
    private final PayableViewModelHelper payableViewModelHelper;

    @NonNull
    private final LiveData<List<AdditionalShiftEntity>> items;

    public AdditionalShiftViewModel(@NonNull Application application) {
        super(application);
        items = new LiveAdditionalShifts(application, getDao().getItems());
        this.payableViewModelHelper = new PayableViewModelHelper(getDao().getPayableDaoHelper());
    }

    @NonNull
    @Override
    AdditionalShiftDao getDao() {
        return AppDatabase.getInstance(getApplication()).additionalShiftDao();
    }

    @NonNull
    @Override
    public LiveData<List<AdditionalShiftEntity>> getItems() {
        return items;
    }

    @NonNull
    @Override
    LiveData<AdditionalShiftEntity> fetchItem(final long id) {
        return Transformations.map(items, new Function<List<AdditionalShiftEntity>, AdditionalShiftEntity>() {
            @Override
            public AdditionalShiftEntity apply(List<AdditionalShiftEntity> shifts) {
                if (shifts != null) {
                    for (AdditionalShiftEntity shift : shifts) {
                        if (shift.getId() == id) return shift;
                    }
                }
                return null;
            }
        });
    }

    private int getPaymentInCents(@NonNull ShiftType shiftType) {
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
    public void addNewShift(@NonNull final ShiftType shiftType) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
                LiveShiftConfig calculator = LiveShiftConfig.getInstance(getApplication());
                postSelectedId(getDao().insertSync(
                        calculator.getPair(shiftType, sharedPreferences),
                        calculator.getFreshZoneId(sharedPreferences),
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
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(date, getZoneId()));
    }

    @MainThread
    public void saveNewTime(@NonNull LocalTime time, boolean isStart) {
        AdditionalShiftEntity shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewTime(time, getZoneId(), isStart));
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
