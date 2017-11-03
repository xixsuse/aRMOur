package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataConsecutiveWeekends;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalDate;

final class ConsecutiveWeekendsBinder extends ComplianceDataBinder<ComplianceDataConsecutiveWeekends> {

    ConsecutiveWeekendsBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataConsecutiveWeekends complianceDataConsecutiveWeekends) {
        super(callbacks, complianceDataConsecutiveWeekends);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_weekend_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.consecutive_weekends);
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Context context) {
        return context.getString(R.string.x_worked_in_last_y, context.getResources().getQuantityString(R.plurals.weekends, getData().getWeekendsWorkedInPeriod().size(), getData().getWeekendsWorkedInPeriod().size()), context.getResources().getQuantityString(R.plurals.weeks, getData().getPeriodInWeeks(), getData().getPeriodInWeeks()));
    }

    @Override
    String getThirdLine(@NonNull Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        LocalDate previousWeekendWorked = null;
        for (LocalDate weekend : getData().getWeekendsWorkedInPeriod()) {
            if (previousWeekendWorked != null) {
                stringBuilder.append('\n');
            }
            String weekendDateSpan = DateTimeUtils.getWeekendDateSpanString(weekend);
            if (previousWeekendWorked != null && previousWeekendWorked.plusWeeks(1).isEqual(weekend)) {
                stringBuilder.append(context.getString(R.string.consecutive, weekendDateSpan));
            } else {
                stringBuilder.append(weekendDateSpan);
            }
            previousWeekendWorked = weekend;
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
                getData().getPeriodInWeeks() == AppConstants.CONSECUTIVE_WEEKEND_LENIENT_PERIOD_IN_WEEKS ?
                        R.string.meca_consecutive_weekends :
                        R.string.meca_1_in_3_weekends
        );
    }

    @Override
    boolean areContentsTheSame(@NonNull ComplianceDataConsecutiveWeekends A, @NonNull ComplianceDataConsecutiveWeekends B) {
        return
                A.getPeriodInWeeks() == B.getPeriodInWeeks() &&
                        A.saferRosters() == B.saferRosters() &&
                        A.getWeekendsWorkedInPeriod().equals(B.getWeekendsWorkedInPeriod());
    }

}
