package com.skepticalone.mecachecker.shift;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.Compliance;
import com.skepticalone.mecachecker.NonCompliantTimeSpan;
import com.skepticalone.mecachecker.R;

public class ComplianceFragment extends DialogFragment {

    static final String TAG = "ComplianceFragment";
    private static final String TITLE = "TITLE";
    private static final String START_OF_NON_COMPLIANT_PERIOD = "START_OF_NON_COMPLIANT_PERIOD";
    private static final String END_OF_NON_COMPLIANT_PERIOD = "END_OF_NON_COMPLIANT_PERIOD";
    private static final String HOURS = "HOURS";

    public static ComplianceFragment createDialogNonCompliantWithMinimumRestHoursBetweenShifts(NonCompliantTimeSpan nonCompliantPeriod) {
        return create(R.string.minimum_rest_hours_between_shifts, nonCompliantPeriod);
    }

    public static ComplianceFragment createDialogNonCompliantWithMaximumHoursPerDay(NonCompliantTimeSpan nonCompliantPeriod) {
        return create(R.string.maximum_hours_per_day, nonCompliantPeriod);
    }

    public static ComplianceFragment createDialogNonCompliantWithMaximumHoursPerWeek(NonCompliantTimeSpan nonCompliantPeriod) {
        return create(R.string.maximum_hours_per_week, nonCompliantPeriod);
    }

    public static ComplianceFragment createDialogNonCompliantWithMaximumHoursPerFortnight(NonCompliantTimeSpan nonCompliantPeriod) {
        return create(R.string.maximum_hours_per_fortnight, nonCompliantPeriod);
    }

    public static ComplianceFragment createDialogNonCompliantWithMaximumConsecutiveWeekends() {
        return create(R.string.maximum_consecutive_weekends, null);
    }

    private static ComplianceFragment create(@StringRes int title, @Nullable NonCompliantTimeSpan nonCompliantPeriod) {
        ComplianceFragment fragment = new ComplianceFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(TITLE, title);
        if (nonCompliantPeriod != null) {
            arguments.putLong(START_OF_NON_COMPLIANT_PERIOD, nonCompliantPeriod.getStart().getTime());
            arguments.putLong(END_OF_NON_COMPLIANT_PERIOD, nonCompliantPeriod.getEnd().getTime());
            arguments.putFloat(HOURS, nonCompliantPeriod.getHours());
        }
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt(TITLE);
        long startOfNonCompliantPeriod = getArguments().getLong(START_OF_NON_COMPLIANT_PERIOD);
        long endOfNonCompliantPeriod = getArguments().getLong(END_OF_NON_COMPLIANT_PERIOD);
        float hours = getArguments().getFloat(HOURS);
        String message;
        switch (title) {
            case R.string.minimum_rest_hours_between_shifts:
                message = getString(R.string.time_span, startOfNonCompliantPeriod, endOfNonCompliantPeriod, hours, Compliance.MINIMUM_REST_HOURS);
                break;
            case R.string.maximum_hours_per_day:
                message = getString(R.string.time_span, startOfNonCompliantPeriod, endOfNonCompliantPeriod, hours, Compliance.MAXIMUM_HOURS_PER_DAY);
                break;
            case R.string.maximum_hours_per_week:
                message = getString(R.string.time_span, startOfNonCompliantPeriod, endOfNonCompliantPeriod, hours, Compliance.MAXIMUM_HOURS_PER_WEEK);
                break;
            case R.string.maximum_hours_per_fortnight:
                message = getString(R.string.time_span, startOfNonCompliantPeriod, endOfNonCompliantPeriod, hours, Compliance.MAXIMUM_HOURS_PER_FORTNIGHT);
                break;
            case R.string.maximum_consecutive_weekends:
                message = getString(R.string.maximum_consecutive_weekends_detail);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .create();
    }
}
