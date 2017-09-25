package com.skepticalone.armour.ui.totals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.PayableTotalsAdapter;

abstract class PayableTotalsDialogFragment<Entity> extends TotalsDialogFragment<Entity> implements PayableTotalsAdapter.Callbacks {

    private CompoundButton unclaimed, claimed, paid;

    @Override
    final int getLayout() {
        return R.layout.payable_summary;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        //noinspection ConstantConditions
        unclaimed = layout.findViewById(R.id.unclaimed);
        unclaimed.setOnCheckedChangeListener(getAdapter());
        claimed = layout.findViewById(R.id.claimed);
        claimed.setOnCheckedChangeListener(getAdapter());
        paid = layout.findViewById(R.id.paid);
        paid.setOnCheckedChangeListener(getAdapter());
        return layout;
    }

    @Override
    public final boolean includeUnclaimed() {
        return unclaimed.isChecked();
    }

    @Override
    public final boolean includeClaimed() {
        return claimed.isChecked();
    }

    @Override
    public final boolean includePaid() {
        return paid.isChecked();
    }

}
