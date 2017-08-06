//package com.skepticalone.mecachecker.data.viewModel;
//
//import android.app.Application;
//import android.database.sqlite.SQLiteConstraintException;
//import android.preference.PreferenceManager;
//import android.support.annotation.IntegerRes;
//import android.support.annotation.MainThread;
//import android.support.annotation.NonNull;
//import android.support.annotation.StringRes;
//
//import com.skepticalone.mecachecker.R;
//import com.skepticalone.mecachecker.data.dao.AdditionalShiftDao;
//import com.skepticalone.mecachecker.data.db.AppDatabase;
//import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
//import com.skepticalone.mecachecker.data.util.PaymentData;
//import com.skepticalone.mecachecker.data.util.ShiftData;
//import com.skepticalone.mecachecker.util.ShiftUtil;
//
//import org.joda.time.LocalDate;
//import org.joda.time.LocalTime;
//
//
//public final class AdditionalShiftViewModel extends PayableViewModel<AdditionalShiftEntity, AdditionalShiftDao>
//        implements ShiftViewModelContract<AdditionalShiftEntity> {
//
//    public AdditionalShiftViewModel(Application application) {
//        super(application);
//    }
//
//    @NonNull
//    @Override
//    AdditionalShiftDao onCreateDao(@NonNull AppDatabase database) {
//        return database.additionalShiftDao();
//    }
//
//    @NonNull
//    private PaymentData getPaymentData(@NonNull ShiftUtil.ShiftType shiftType) {
//        @StringRes final int hourlyRateKey;
//        @IntegerRes final int hourlyRateDefault;
//        switch (shiftType) {
//            case NORMAL_DAY:
//                hourlyRateKey = R.string.key_default_hourly_rate_normal_day;
//                hourlyRateDefault = R.integer.default_hourly_rate_normal_hours;
//                break;
//            case LONG_DAY:
//                hourlyRateKey = R.string.key_default_hourly_rate_long_day;
//                hourlyRateDefault = R.integer.default_hourly_rate_normal_hours;
//                break;
//            case NIGHT_SHIFT:
//                hourlyRateKey = R.string.key_default_hourly_rate_night_shift;
//                hourlyRateDefault = R.integer.default_hourly_rate_after_hours;
//                break;
//            default:
//                throw new IllegalStateException();
//        }
//        return PaymentData.fromPayment(PreferenceManager.getDefaultSharedPreferences(getApplication()).getInt(getApplication().getString(hourlyRateKey), getApplication().getResources().getInteger(hourlyRateDefault)));
//    }
//
//    @Override
//    public void addNewShift(@NonNull final ShiftUtil.ShiftType shiftType) {
//        runAsync(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (AdditionalShiftViewModel.this) {
//                    postSelectedId(getDao().insertItemSync(new AdditionalShiftEntity(
//                            getPaymentData(shiftType),
//                            ShiftData.withEarliestStart(ShiftUtil.Calculator.getInstance(getApplication()).getStartTime(shiftType), ShiftUtil.Calculator.getInstance(getApplication()).getEndTime(shiftType), getDao().getLastShiftEndSync(), false),
//                            null
//                    )));
//                }
//            }
//        });
//    }
//
//    @MainThread
//    private void saveNewShiftTimes(final long id, @NonNull final ShiftData shiftData) {
//        runAsync(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    getDao().setShiftTimesSync(id, shiftData.getStart(), shiftData.getEnd());
//                } catch (SQLiteConstraintException e) {
//                    postOverlappingShifts();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void saveNewDate(@NonNull AdditionalShiftEntity shift, @NonNull LocalDate date) {
//        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewDate(date));
//    }
//
//    @MainThread
//    public void saveNewTime(@NonNull AdditionalShiftEntity shift, @NonNull LocalTime time, boolean start) {
//        saveNewShiftTimes(shift.getId(), shift.getShiftData().withNewTime(time, start));
//    }
//
//}
