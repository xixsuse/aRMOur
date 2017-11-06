package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;

final class RosteredShiftHelpAdapter extends HelpAdapter {

    private final static HelpViewHolder.Binder[] ROWS = {
            FIRST_HELP_ITEM,
            new HelpViewHolder.Binder(true, R.drawable.ic_normal_day_white_24dp, "Add a normal day", "Another help item, which has a floating action button as an icon and an even longer description"),
            new HelpViewHolder.Binder(true, R.drawable.ic_long_day_white_24dp, "Add a long day", null),
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
