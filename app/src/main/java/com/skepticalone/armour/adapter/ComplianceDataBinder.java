package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.armour.data.model.Compliance;
import com.skepticalone.armour.data.model.ComplianceData;

abstract class ComplianceDataBinder<DataType extends ComplianceData> extends ItemViewHolder.PlainBinder {

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final DataType data;

    ComplianceDataBinder(@NonNull Callbacks callbacks, @NonNull DataType dataType) {
        this.callbacks = callbacks;
        this.data = dataType;
    }

    @NonNull
    final DataType getData() {
        return data;
    }

    @NonNull
    abstract String getMessage(@NonNull Context context);

    @Override
    final boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
        //noinspection unchecked
        ComplianceDataBinder<DataType> newBinder = (ComplianceDataBinder<DataType>) other;
        return getData().isChecked() == newBinder.getData().isChecked() && areContentsTheSame(getData(), newBinder.getData());
    }

    abstract boolean areContentsTheSame(@NonNull DataType A, @NonNull DataType B);

    @Override
    public final void onClick(View view) {
        callbacks.showMessage(getMessage(view.getContext()));
    }

    @Override
    final boolean showSecondaryIcon() {
        return data.isChecked();
    }

    @Override
    final int getSecondaryIcon() {
        return Compliance.getComplianceIcon(data.isCompliantIfChecked());
    }

    interface Callbacks {
        void showMessage(@NonNull String message);
    }

}
