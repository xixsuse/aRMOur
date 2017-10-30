package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemViewHolder;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;

public final class RowWeekend extends Row {

    @NonNull
    private final List<LocalDate> weekendsWorkedInPeriod = new ArrayList<>();
    private final int periodInWeeks;
    private final boolean saferRosters;
    private final boolean includesConsecutiveWeekends;

    private RowWeekend(@NonNull Configuration configuration, @NonNull LocalDate currentWeekend, @NonNull List<RosteredShift> previousShifts) {
        super(configuration.checkFrequencyOfWeekends());
        if (configuration instanceof ConfigurationSaferRosters) {
            saferRosters = true;
            ConfigurationSaferRosters configurationSaferRosters = (ConfigurationSaferRosters) configuration;
            periodInWeeks = configurationSaferRosters.allow1in2Weekends() ? (configurationSaferRosters.allowFrequentConsecutiveWeekends() ? 5 : 6) : (configurationSaferRosters.allowFrequentConsecutiveWeekends() ? 8 : 9);
        } else {
            saferRosters = false;
            periodInWeeks = configuration.allow1in2Weekends() ? AppConstants.MAXIMUM_CONSECUTIVE_WEEKEND_FREQUENCY_PERIOD_IN_WEEKS_LENIENT : AppConstants.MAXIMUM_CONSECUTIVE_WEEKEND_FREQUENCY_PERIOD_IN_WEEKS_STRICT;
        }
        weekendsWorkedInPeriod.add(currentWeekend);
        boolean consecutiveWeekends = false;
        for (int i = previousShifts.size() - 1; i >= 0; i--) {
            RowWeekend weekend = previousShifts.get(i).getCompliance().getWeekend();
            if (weekend != null) {
                LocalDate cutOff = currentWeekend.minusWeeks(periodInWeeks);
                for (LocalDate weekendWorked : weekend.weekendsWorkedInPeriod) {
                    if (!weekendWorked.isAfter(cutOff)) break;
                    else if (currentWeekend.isEqual(weekendWorked)) continue;
                    consecutiveWeekends |= currentWeekend.minusWeeks(1).isEqual(weekendWorked);
                    weekendsWorkedInPeriod.add(weekendWorked);
                }
                break;
            }
        }
        includesConsecutiveWeekends = consecutiveWeekends;
    }

    @Nullable
    static RowWeekend from(@NonNull Configuration configuration, @NonNull Shift.Data shift, @NonNull List<RosteredShift> previousShifts) {
        LocalDate currentWeekend = calculateCurrentWeekend(shift);
        return currentWeekend == null ? null : new RowWeekend(configuration, currentWeekend, previousShifts);
    }

    @Nullable
    private static LocalDate calculateCurrentWeekend(@NonNull Shift.Data shift) {
        ZonedDateTime weekendStart = shift.getStart().with(DayOfWeek.SATURDAY).with(LocalTime.MIN);
        return weekendStart.isBefore(shift.getEnd()) && shift.getStart().isBefore(weekendStart.plusDays(2)) ? weekendStart.toLocalDate() : null;
    }

    @Override
    public final boolean isCompliantIfChecked() {
        return saferRosters ? weekendsWorkedInPeriod.size() <= 2 || !includesConsecutiveWeekends : weekendsWorkedInPeriod.size() <= 1;
    }

    public static final class Binder extends Row.Binder<RowWeekend> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowWeekend row) {
            super(callbacks, row);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_weekend_black_24dp;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return context.getString(R.string.weekend_frequency);
        }

        @Override
        public String getSecondLine(@NonNull Context context) {
            return context.getString(R.string.n_weekends_worked_in_last_n_weeks, context.getResources().getQuantityString(R.plurals.weekends, getRow().weekendsWorkedInPeriod.size(), getRow().weekendsWorkedInPeriod.size()), context.getResources().getQuantityString(R.plurals.weeks, getRow().periodInWeeks, getRow().periodInWeeks));
        }

        @Override
        public String getThirdLine(@NonNull Context context) {
            StringBuilder stringBuilder = new StringBuilder();
            for (LocalDate weekend : getRow().weekendsWorkedInPeriod) {
                if (stringBuilder.length() != 0) stringBuilder.append('\n');
                stringBuilder.append(DateTimeUtils.getWeekendDateSpanString(weekend));
            }
            if (getRow().includesConsecutiveWeekends) {
                stringBuilder.append('\n').append(context.getString(R.string.includes_consecutive_weekends));
            }
            return stringBuilder.toString();
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            return getRow().saferRosters ?
                    context.getString(
                            R.string.meca_safer_rosters_maximum_consecutive_weekends,
                            2,
                            getRow().periodInWeeks,
                            getRow().periodInWeeks - 2
                    ) : context.getString(
                    getRow().periodInWeeks == AppConstants.MAXIMUM_CONSECUTIVE_WEEKEND_FREQUENCY_PERIOD_IN_WEEKS_LENIENT ?
                            R.string.meca_consecutive_weekends :
                            R.string.meca_1_in_3_weekends
            );
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
            if (!super.areContentsTheSame(other)) return false;
            Binder newBinder = (Binder) other;
            return
                    getRow().periodInWeeks == newBinder.getRow().periodInWeeks &&
                            getRow().saferRosters == newBinder.getRow().saferRosters &&
                            getRow().includesConsecutiveWeekends == newBinder.getRow().includesConsecutiveWeekends &&
                            getRow().weekendsWorkedInPeriod.equals(newBinder.getRow().weekendsWorkedInPeriod);
        }
    }

}
