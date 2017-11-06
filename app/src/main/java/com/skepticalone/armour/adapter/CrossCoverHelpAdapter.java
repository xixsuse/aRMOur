package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

final class CrossCoverHelpAdapter extends HelpAdapter {

    private final static HelpViewHolder.Binder[] ROWS = {
            FIRST_HELP_ITEM,
    };

    CrossCoverHelpAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    HelpViewHolder.Binder[] getRows() {
        return ROWS;
    }
}