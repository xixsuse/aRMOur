package com.skepticalone.mecachecker.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemTotalsAdapter;
import com.skepticalone.mecachecker.adapter.PayableTotalsAdapter;
import com.skepticalone.mecachecker.data.model.Payable;

public abstract class PayableTotalsDialogFragment<Entity extends Payable> extends TotalsDialogFragment<Entity> implements PayableTotalsAdapter.Callbacks {

    private final PayableTotalsAdapter<Entity> adapter = new PayableTotalsAdapter<>(this);

    private CompoundButton unclaimed, claimed, paid;

    @Override
    final int getLayout() {
        return R.layout.expense_summary;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        //noinspection ConstantConditions
        unclaimed = layout.findViewById(R.id.unclaimed);
        unclaimed.setOnCheckedChangeListener(adapter);
        claimed = layout.findViewById(R.id.claimed);
        claimed.setOnCheckedChangeListener(adapter);
        paid = layout.findViewById(R.id.paid);
        paid.setOnCheckedChangeListener(adapter);
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

    @NonNull
    @Override
    final ItemTotalsAdapter<Entity> getAdapter() {
        return adapter;
    }
}
