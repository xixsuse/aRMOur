package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.adapter.ItemViewHolder;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.Duration;

abstract class RowDuration extends Row {

    @NonNull
    private final Duration duration;

    RowDuration(boolean isChecked, @NonNull Duration duration) {
        super(isChecked);
        this.duration = duration;
    }

    @NonNull
    public final Duration getDuration() {
        return duration;
    }

    abstract static class Binder<RowType extends RowDuration> extends Row.Binder<RowType> {

        Binder(@NonNull Callbacks callbacks, @NonNull RowType row) {
            super(callbacks, row);
        }

        @Override
        public final boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
            return super.areContentsTheSame(other) && getRow().getDuration().equals(((RowDuration) (((Binder) other).getRow())).getDuration());
        }

        @Nullable
        @Override
        public final String getSecondLine(@NonNull Context context) {
            return DateTimeUtils.getDurationString(context, getRow().getDuration());
        }

    }
}
