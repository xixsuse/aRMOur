package com.skepticalone.armour.help;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.AppConstants;

public final class RosteredShiftHelpFragment extends HelpFragment {
    @Override
    int getTitle() {
        return R.string.rostered_shifts;
    }

    @Override
    void onAddToView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        inflater.inflate(R.layout.help_item_shift_type, container, true);
        inflater.inflate(R.layout.help_item_compliance, container, true);
        View section;
        section = inflater.inflate(R.layout.help_item_add_shift, container, false);
        ((TextView) section.findViewById(R.id.help_description_1)).setText(getString(R.string.help_add_rostered_shift_description_1, getResources().getQuantityString(R.plurals.hours, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)));
        section.findViewById(R.id.help_description_2).setVisibility(View.VISIBLE);
        container.addView(section);
        inflater.inflate(R.layout.help_item_delete, container, true);
        section = inflater.inflate(R.layout.help_item_totals, container, false);
        ((TextView) section.findViewById(R.id.help_description_1)).setText(R.string.help_totals_rostered_shifts_description);
        container.addView(section);
        section = inflater.inflate(R.layout.help_item_subtotals, container, false);
        ((TextView) section.findViewById(R.id.help_description_1)).setText(R.string.help_subtotals_rostered_shifts_description);
        container.addView(section);
        inflater.inflate(R.layout.help_item_run_review, container, true);
    }
}
