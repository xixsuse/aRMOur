package com.skepticalone.armour.data.compliance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;
import com.skepticalone.armour.adapter.ItemViewHolder;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalDate;

public final class RowRosteredDayOff extends Row {

    @Nullable
    private final LocalDate date;
    private final boolean allowMidweekRDOs;

    RowRosteredDayOff(@NonNull ConfigurationSaferRosters configuration, @Nullable LocalDate date) {
        super(configuration.checkRDOs());
        this.date = date;
        this.allowMidweekRDOs = configuration.allowMidweekRDOs();
    }

    @Nullable
    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean isCompliantIfChecked() {
        return date != null;
    }

    public static final class Binder extends Row.Binder<RowRosteredDayOff> {

        public Binder(@NonNull Callbacks callbacks, @NonNull RowRosteredDayOff row) {
            super(callbacks, row);
        }

        @Override
        public int getPrimaryIcon() {
            return R.drawable.ic_safer_rosters_black_24dp;
        }

        @NonNull
        @Override
        public String getFirstLine(@NonNull Context context) {
            return context.getString(R.string.rostered_day_off);
        }

        @Override
        public String getSecondLine(@NonNull Context context) {
            return getRow().date == null ? "No RDO found" : DateTimeUtils.getFullDateString(getRow().date);
        }

        @NonNull
        @Override
        String getMessage(@NonNull Context context) {
            return context.getString(getRow().allowMidweekRDOs ? R.string.meca_safer_rosters_rostered_day_off_lenient : R.string.meca_safer_rosters_rostered_day_off_strict);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemViewHolder.Binder other) {
            if (!super.areContentsTheSame(other)) return false;
            Binder newBinder = (Binder) other;
            return
                    getRow().allowMidweekRDOs == newBinder.getRow().allowMidweekRDOs &&
                            Comparators.equalDates(getRow().date, newBinder.getRow().date);
        }
    }

}
