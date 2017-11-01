package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataWeekend;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalDate;

final class WeekendBinder extends ComplianceDataBinder<ComplianceDataWeekend> {

    WeekendBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataWeekend complianceDataWeekend) {
        super(callbacks, complianceDataWeekend);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_weekend_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.weekend_frequency);
    }

    @Override
    String getSecondLine(@NonNull Context context) {
        return context.getString(R.string.n_weekends_worked_in_last_n_weeks, context.getResources().getQuantityString(R.plurals.weekends, getData().getWeekendsWorkedInPeriod().size(), getData().getWeekendsWorkedInPeriod().size()), context.getResources().getQuantityString(R.plurals.weeks, getData().getPeriodInWeeks(), getData().getPeriodInWeeks()));
    }

    @Override
    String getThirdLine(@NonNull Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        for (LocalDate weekend : getData().getWeekendsWorkedInPeriod()) {
            if (stringBuilder.length() != 0) stringBuilder.append('\n');
            stringBuilder.append(DateTimeUtils.getWeekendDateSpanString(weekend));
        }
        if (getData().includesConsecutiveWeekends()) {
            stringBuilder.append('\n').append(context.getString(R.string.includes_consecutive_weekends));
        }
        return stringBuilder.toString();
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        return getData().saferRosters() ?
                context.getString(
                        R.string.meca_safer_rosters_maximum_consecutive_weekends,
                        2,
                        getData().getPeriodInWeeks(),
                        getData().getPeriodInWeeks() - 2
                ) : context.getString(
                getData().getPeriodInWeeks() == AppConstants.MAXIMUM_CONSECUTIVE_WEEKEND_FREQUENCY_PERIOD_IN_WEEKS_LENIENT ?
                        R.string.meca_consecutive_weekends :
                        R.string.meca_1_in_3_weekends
        );
    }

    @Override
    boolean areContentsTheSame(@NonNull ComplianceDataWeekend A, @NonNull ComplianceDataWeekend B) {
        return
                A.getPeriodInWeeks() == B.getPeriodInWeeks() &&
                        A.includesConsecutiveWeekends() == B.includesConsecutiveWeekends() &&
                        A.saferRosters() == B.saferRosters() &&
                        A.getWeekendsWorkedInPeriod().equals(B.getWeekendsWorkedInPeriod());
    }

}
