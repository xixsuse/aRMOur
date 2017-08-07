package com.skepticalone.mecachecker.ui.totals;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemTotalsAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftTotalsAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModelContract;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftTotalsDialogFragment extends TotalsDialogFragment<RosteredShiftEntity> implements RosteredShiftTotalsAdapter.Callbacks {

    private CompoundButton compliant, nonCompliant;

    @NonNull
    @Override
    ItemTotalsAdapter<RosteredShiftEntity> createAdapter(@NonNull Context context) {
        return new RosteredShiftTotalsAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    ItemViewModelContract<RosteredShiftEntity> getViewModel() {
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
        return R.layout.compliance_summary;
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
