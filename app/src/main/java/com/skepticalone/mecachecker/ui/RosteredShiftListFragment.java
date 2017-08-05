package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.adapter.ItemTotalsAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftListAdapter;
import com.skepticalone.mecachecker.adapter.RosteredShiftTotalsAdapter;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.RosteredShiftViewModel;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftListFragment extends ShiftAddListFragment<RosteredShiftEntity, RosteredShiftViewModel> {

    @Override
    int getItemType() {
        return R.id.rostered;
    }

    @NonNull
    @Override
    ItemListAdapter<RosteredShiftEntity> createAdapter(Context context) {
        return new RosteredShiftListAdapter(this, ShiftUtil.Calculator.getInstance(context));
    }

    @NonNull
    @Override
    RosteredShiftViewModel createViewModel(@NonNull ViewModelProvider provider) {
        return provider.get(RosteredShiftViewModel.class);
    }

    @NonNull
    @Override
    TotalsDialogFragment<RosteredShiftEntity> createSummaryDialogFragment() {
        return new RosteredShiftTotalsDialogFragment();
    }

    public static final class RosteredShiftTotalsDialogFragment extends TotalsDialogFragment<RosteredShiftEntity> implements RosteredShiftTotalsAdapter.Callbacks {

        private CompoundButton compliant, nonCompliant;

        @NonNull
        @Override
        ItemTotalsAdapter<RosteredShiftEntity> createAdapter(@NonNull Context context) {
            return new RosteredShiftTotalsAdapter(this, ShiftUtil.Calculator.getInstance(context));
        }

        @NonNull
        @Override
        ViewModelContract<RosteredShiftEntity> onCreateViewModel(@NonNull ViewModelProvider provider) {
            return provider.get(RosteredShiftViewModel.class);
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
}
