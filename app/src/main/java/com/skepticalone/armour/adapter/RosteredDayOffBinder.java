package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.ComplianceDataRosteredDayOff;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

final class RosteredDayOffBinder extends ComplianceDataBinder<ComplianceDataRosteredDayOff> {

    RosteredDayOffBinder(@NonNull Callbacks callbacks, @NonNull ComplianceDataRosteredDayOff complianceDataRosteredDayOff) {
        super(callbacks, complianceDataRosteredDayOff);
    }

    @Override
    int getPrimaryIcon() {
        return R.drawable.ic_safer_rosters_black_24dp;
    }

    @NonNull
    @Override
    String getFirstLine(@NonNull Context context) {
        return context.getString(R.string.rostered_day_off);
    }

    @NonNull
    @Override
    String getSecondLine(@NonNull Context context) {
        return getData().getDate() == null ? context.getString(R.string.no_rostered_day_off_found) : DateTimeUtils.getFullDateString(getData().getDate());
    }

    @NonNull
    @Override
    String getMessage(@NonNull Context context) {
        return context.getString(getData().allowMidweekRDOs() ? R.string.meca_safer_rosters_rostered_day_off_lenient : R.string.meca_safer_rosters_rostered_day_off_strict);
    }

    @Override
    boolean areContentsTheSame(@NonNull ComplianceDataRosteredDayOff A, @NonNull ComplianceDataRosteredDayOff B) {
        return
                A.allowMidweekRDOs() == B.allowMidweekRDOs() &&
                        Comparators.equalDates(A.getDate(), B.getDate());
    }

}
