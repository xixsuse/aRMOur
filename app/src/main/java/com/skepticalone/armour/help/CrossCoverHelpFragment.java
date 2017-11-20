package com.skepticalone.armour.help;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.skepticalone.armour.R;

public final class CrossCoverHelpFragment extends HelpFragment {

    @Override
    int getTitle() {
        return R.string.cross_cover_shifts;
    }

    @Override
    void onAddToView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        inflater.inflate(R.layout.help_item_payment, container, true);
        inflater.inflate(R.layout.help_item_add_single, container, true);
        inflater.inflate(R.layout.help_item_delete, container, true);
        inflater.inflate(R.layout.help_item_totals, container, true);
        inflater.inflate(R.layout.help_item_subtotals, container, true);
    }
}
