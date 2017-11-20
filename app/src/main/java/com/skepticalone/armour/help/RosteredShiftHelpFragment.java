package com.skepticalone.armour.help;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.R;

public final class RosteredShiftHelpFragment extends HelpFragment {
    @Override
    int getTitle() {
        return R.string.rostered_shifts;
    }

    @Override
    void onAddToView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
//        inflater.inflate(R.layout.help_item_shift_type, container, true);
//        inflater.inflate(R.layout.help_item_compliance, container, true);
//        View section;
//        section = inflater.inflate(R.layout.help_item_add_shift, container, false);
//        ((TextView) section.findViewById(R.id.help_description_1)).bind(getString(R.string.help_add_rostered_shift_description_1, getResources().getQuantityString(R.plurals.hours, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)));
//        section.findViewById(R.id.help_description_2).setVisibility(View.VISIBLE);
//        container.addView(section);
//        inflater.inflate(R.layout.help_item_delete, container, true);
//        section = inflater.inflate(R.layout.help_item_totals, container, false);
//        ((TextView) section.findViewById(R.id.help_description_1)).bind(R.string.help_totals_rostered_shifts_description);
//        container.addView(section);
//        section = inflater.inflate(R.layout.help_item_subtotals, container, false);
//        ((TextView) section.findViewById(R.id.help_description_1)).bind(R.string.help_subtotals_rostered_shifts_description);
//        container.addView(section);
//        inflater.inflate(R.layout.help_item_run_review, container, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setAdapter(new RosteredShiftHelpAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return recyclerView;
    }
}
