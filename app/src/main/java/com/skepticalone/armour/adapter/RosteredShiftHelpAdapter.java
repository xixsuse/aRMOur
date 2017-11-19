package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;

final class RosteredShiftHelpAdapter extends HelpAdapter {

    private final static HelpViewHolder.Binder[] ROWS = {
            new HelpViewHolder.Binder(true, R.drawable.ic_normal_day_white_24dp, "Add a normal day", null),
            new HelpViewHolder.Binder(true, R.drawable.ic_long_day_white_24dp, "Add a long day", null),
            new HelpViewHolder.Binder(true, R.drawable.ic_night_shift_white_24dp, "Add a night shift", null),
            new HelpViewHolder.Binder(false, R.drawable.compliant_black_24dp, "Compliant shift", "Passes all compliance checks"),
            new HelpViewHolder.Binder(false, R.drawable.non_compliant_red_24dp, "Non-compliant shift", "Fails at least one compliance check"),
            new HelpViewHolder.Binder(false, R.drawable.ic_check_circle_24dp, "Selected shift", "Long click to select shifts"),
            new HelpViewHolder.Binder(false, R.drawable.run_review_white_24dp, "Non-compliant shift", "Fails at least one compliance check"),
    };

    RosteredShiftHelpAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    HelpViewHolder.Binder[] getRows() {
        return ROWS;
    }
}
