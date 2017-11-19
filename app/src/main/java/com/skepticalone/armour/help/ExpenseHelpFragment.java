package com.skepticalone.armour.help;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.armour.R;

public final class ExpenseHelpFragment extends HelpFragment {

    @Override
    int getTitle() {
        return R.string.expenses;
    }

    @Override
    void onAddToView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View section;
        section = inflater.inflate(R.layout.help_item_payment, container, false);
        section.findViewById(R.id.help_description_2).setVisibility(View.VISIBLE);
        container.addView(section);
        section = inflater.inflate(R.layout.help_item_add_single, container, false);
        ((TextView) section.findViewById(R.id.title)).setText("Add expense");
        ((TextView) section.findViewById(R.id.add_single_label)).setText("Add an expense");
        section.findViewById(R.id.help_description_1).setVisibility(View.GONE);
        container.addView(section);
        section = inflater.inflate(R.layout.help_item_delete, container, false);
        ((TextView) section.findViewById(R.id.title)).setText("Delete expenses");
        ((TextView) section.findViewById(R.id.select_label)).setText("Press and hold to select expenses");
        container.addView(section);
        section = inflater.inflate(R.layout.help_item_totals, container, false);
        ((TextView) section.findViewById(R.id.help_description_1)).setText("Opens a summary view containing information for all expenses, which can be filtered by payment status");
        container.addView(section);
        section = inflater.inflate(R.layout.help_item_subtotals, container, false);
        ((TextView) section.findViewById(R.id.title)).setText("Show subtotals for selected expenses");
        ((TextView) section.findViewById(R.id.select_label)).setText("Press and hold to select expenses");
        ((TextView) section.findViewById(R.id.help_description_1)).setText("Opens a summary view containing information for selected expenses, which can be filtered by payment status");
        container.addView(section);
    }
}
