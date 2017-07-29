package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.AdditionalShiftDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.math.BigDecimal;


public final class AdditionalShiftViewModel extends ItemViewModel<AdditionalShiftEntity, AdditionalShiftDao>
        implements ShiftViewModelContract<AdditionalShiftEntity>, PayableViewModelContract<AdditionalShiftEntity> {

    private final PayableHelper payableHelper;
    private final String hourlyRateKey;
    private final int defaultHourlyRate;
    private final ShiftUtil.Calculator calculator;

    public AdditionalShiftViewModel(Application application) {
        super(application);
        payableHelper = new PayableHelper(getDao());
        hourlyRateKey = application.getString(R.string.key_hourly_rate);
        defaultHourlyRate = application.getResources().getInteger(R.integer.default_hourly_rate);
        calculator = new ShiftUtil.Calculator(application);
    }

    @NonNull
    @Override
    AdditionalShiftDao onCreateDao(@NonNull AppDatabase database) {
        return database.additionalShiftDao();
    }

    @Override
    public void saveNewPayment(@NonNull BigDecimal payment) {
        payableHelper.saveNewPayment(getCurrentItem(), payment);
    }

    @Override
    public void setClaimed(boolean claimed) {
        payableHelper.setClaimed(getCurrentItem(), claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        payableHelper.setPaid(getCurrentItem(), paid);
    }

    private void saveNewShiftTimes(long id, @NonNull ShiftData shiftData) {
        getDao().setShiftTimesSync(id, shiftData.getStart(), shiftData.getEnd());
    }

    public void saveNewDate(@NonNull LocalDate newDate) {
        AdditionalShiftEntity shift = getCurrentItem().getValue();
        if (shift != null) {
            saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(newDate));
        }
    }

    @Override
    public void saveNewTime(@NonNull LocalTime time, boolean start) {
        AdditionalShiftEntity shift = getCurrentItem().getValue();
        if (shift != null) {
            saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewTime(time, start));
        }
    }

    @Override
    public void addNewShift(@NonNull ShiftUtil.ShiftType shiftType) {
        final int hourlyRate = PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(hourlyRateKey, defaultHourlyRate);
        final LocalTime startTime = calculator.getStartTime(shiftType),
                endTime = calculator.getEndTime(shiftType);
        DateTime newStart = DateTime.now().withTime(startTime);
        final DateTime lastShiftEnd = getDao().getLastShiftEndSync(), newEnd;
        if (lastShiftEnd != null && newStart.isBefore(lastShiftEnd)) {
            newStart = lastShiftEnd.withTime(newStart.toLocalTime());
            while (newStart.isBefore(lastShiftEnd)) {
                newStart = newStart.plusDays(1);
            }
        }
        newEnd = ShiftData.getNewEnd(newStart, endTime);
        insertItem(new AdditionalShiftEntity(new PaymentData(hourlyRate), new ShiftData(newStart, newEnd), null));
    }
}
