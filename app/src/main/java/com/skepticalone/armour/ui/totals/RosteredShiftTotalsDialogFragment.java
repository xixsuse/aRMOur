package com.skepticalone.armour.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemTotalsAdapter;
import com.skepticalone.armour.adapter.RosteredShiftTotalsAdapter;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.viewModel.ItemViewModelContract;
import com.skepticalone.armour.data.viewModel.RosteredShiftViewModel;

public final class RosteredShiftTotalsDialogFragment extends TotalsDialogFragment<RosteredShift> implements RosteredShiftTotalsAdapter.Callbacks {

    private CompoundButton compliant, nonCompliant;

    public static RosteredShiftTotalsDialogFragment newInstance(boolean selected) {
        Bundle args = new Bundle();
        args.putBoolean(SELECTED, selected);
        RosteredShiftTotalsDialogFragment fragment = new RosteredShiftTotalsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    ItemTotalsAdapter<RosteredShift> createAdapter(@NonNull Context context) {
        return new RosteredShiftTotalsAdapter(context, isSelected() ? R.string.selected_rostered_shifts : R.string.all_rostered_shifts, this);
    }

    @NonNull
    @Override
    ItemViewModelContract<RosteredShift> getViewModel() {
        return ViewModelProviders.of(getActivity()).get(RosteredShiftViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        //noinspection ConstantConditions
        compliant = layout.findViewById(R.id.compliant);
        compliant.setOnCheckedChangeListener(getAdapter());
        nonCompliant = layout.findViewById(R.id.non_compliant);
        nonCompliant.setOnCheckedChangeListener(getAdapter());
        return layout;
    }

    @Override
    int getLayout() {
        return R.layout.compliance_totals;
    }

    @Override
    public boolean includeCompliant() {
        return compliant.isChecked();
    }

    @Override
    public boolean includeNonCompliant() {
        return nonCompliant.isChecked();
    }
}
