package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.armour.adapter.ItemViewHolder;

public abstract class Row {

    private final boolean isChecked;

    Row(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public abstract boolean isCompliantIfChecked();

    public final boolean isCompliant() {
        return !isChecked || isCompliantIfChecked();
    }

    public final boolean isChecked() {
        return isChecked;
    }

    final boolean equalCompliance(@NonNull Row other) {
        return isChecked ? (other.isChecked && isCompliantIfChecked() == other.isCompliantIfChecked()) : !other.isChecked;
    }

    public abstract static class Binder<RowType extends Row> extends ItemViewHolder.PlainBinder {

        @NonNull
        private final Callbacks callbacks;
        @NonNull
        private final RowType row;

        Binder(@NonNull Callbacks callbacks, @NonNull RowType row) {
            this.callbacks = callbacks;
            this.row = row;
        }

        @NonNull
        final RowType getRow() {
            return row;
        }

        @Override
        @CallSuper
        public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
            Binder newBinder = (Binder) other;
            return row.isChecked() ? (newBinder.row.isChecked() && row.isCompliantIfChecked() == newBinder.row.isCompliantIfChecked()) : !newBinder.row.isChecked();
        }

        @NonNull
        abstract String getMessage(@NonNull Context context);

        @Override
        public final void onClick(View view) {
            callbacks.showMessage(getMessage(view.getContext()));
        }

        @Override
        public final boolean showSecondaryIcon() {
            return row.isChecked();
        }

        @Override
        public final int getSecondaryIcon() {
            return Compliance.getComplianceIcon(row.isCompliantIfChecked());
        }

        public interface Callbacks {
            void showMessage(@NonNull String message);
        }

    }
}
