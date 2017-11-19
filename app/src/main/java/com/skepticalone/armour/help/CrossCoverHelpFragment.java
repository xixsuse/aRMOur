package com.skepticalone.armour.help;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.armour.R;

public final class CrossCoverHelpFragment extends HelpFragment {

    @Override
    int getTitle() {
        return R.string.cross_cover_shifts;
    }

    @Override
    void onAddToView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        inflater.inflate(R.layout.help_item_payment, container, true);
        View section;
        section = inflater.inflate(R.layout.help_item_add_single, container, false);
        ((TextView) section.findViewById(R.id.title)).setText("Add shift");
        ((TextView) section.findViewById(R.id.add_single_label)).setText("Add a cross cover shift");
        container.addView(section);
        inflater.inflate(R.layout.help_item_delete, container, true);
        inflater.inflate(R.layout.help_item_totals, container, true);
        inflater.inflate(R.layout.help_item_subtotals, container, true);
    }
}
