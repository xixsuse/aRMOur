package com.skepticalone.armour.help;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.armour.R;

public final class AdditionalShiftHelpFragment extends HelpFragment {

    @Override
    int getTitle() {
        return R.string.additional_shifts;
    }

    @Override
    void onAddToView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        inflater.inflate(R.layout.help_item_shift_type, container, true);
        inflater.inflate(R.layout.help_item_payment, container, true);
        View section;
        section = inflater.inflate(R.layout.help_item_add_shift, container, false);
        ((TextView) section.findViewById(R.id.help_description_1)).setText("New additional shifts begin as soon as possible following the previous shift");
        container.addView(section);
        inflater.inflate(R.layout.help_item_delete, container, true);
        inflater.inflate(R.layout.help_item_totals, container, true);
        inflater.inflate(R.layout.help_item_subtotals, container, true);
    }
}
