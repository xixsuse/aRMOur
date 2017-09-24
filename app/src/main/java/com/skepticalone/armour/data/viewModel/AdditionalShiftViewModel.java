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
import com.skepticalone.armour.data.dao.RawAdditionalShiftDao;
import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.model.RawAdditionalShiftEntity;
import com.skepticalone.armour.data.model.LiveAdditionalShifts;
import com.skepticalone.armour.data.model.LiveShiftConfig;
import com.skepticalone.armour.data.model.RawShift;
import com.skepticalone.armour.data.model.Shift;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.math.BigDecimal;
import java.util.List;


public final class AdditionalShiftViewModel extends ItemViewModel<RawAdditionalShiftEntity> implements ShiftViewModelContract<RawAdditionalShiftEntity>, PayableViewModelContract<RawAdditionalShiftEntity> {

    @NonNull
    private final PayableViewModelHelper payableViewModelHelper;

    @NonNull
    private final LiveData<List<RawAdditionalShiftEntity>> items;

    public AdditionalShiftViewModel(@NonNull Application application) {
        super(application);
        items = new LiveAdditionalShifts(application, getDao().getItems());
        this.payableViewModelHelper = new PayableViewModelHelper(getDao().getPayableDaoHelper());
    }

    @NonNull
    @Override
    RawAdditionalShiftDao getDao() {
        return AppDatabase.getInstance(getApplication()).additionalShiftDao();
    }

    @NonNull
    @Override
    public LiveData<List<RawAdditionalShiftEntity>> fetchItems() {
        return items;
    }

    @NonNull
    @Override
    LiveData<RawAdditionalShiftEntity> fetchItem(final long id) {
        return Transformations.map(items, new Function<List<RawAdditionalShiftEntity>, RawAdditionalShiftEntity>() {
            @Override
            public RawAdditionalShiftEntity apply(List<RawAdditionalShiftEntity> shifts) {
                if (shifts != null) {
                    for (RawAdditionalShiftEntity shift : shifts) {
                        if (shift.getId() == id) return shift;
                    }
                }
                return null;
            }
        });
    }

    private int getPaymentInCents(@NonNull Shift.ShiftType shiftType) {
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
    public void addNewShift(@NonNull final Shift.ShiftType shiftType) {
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
    private void saveNewShiftTimes(final long id, @NonNull final RawShift.RawShiftData rawShiftData) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    getDao().setTimesSync(id, rawShiftData.getStart(), rawShiftData.getEnd());
                } catch (SQLiteConstraintException e) {
                    postOverlappingShifts();
                }
            }
        });
    }

    @Override
    public void saveNewDate(@NonNull LocalDate date) {
        RawAdditionalShiftEntity shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), shift.getRawShiftData().withNewDate(date, getZoneId()));
    }

    @MainThread
    public void saveNewTime(@NonNull LocalTime time, boolean isStart) {
        RawAdditionalShiftEntity shift = getCurrentItem().getValue();
        if (shift == null) throw new IllegalStateException();
        saveNewShiftTimes(shift.getId(), shift.getRawShiftData().withNewTime(time, getZoneId(), isStart));
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
