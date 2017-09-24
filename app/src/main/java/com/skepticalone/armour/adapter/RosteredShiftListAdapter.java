package com.skepticalone.armour.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.skepticalone.armour.data.model.LiveShiftConfig;
import com.skepticalone.armour.data.model.RawRosteredShiftEntity;
import com.skepticalone.armour.data.model.RawShift;
import com.skepticalone.armour.util.Comparators;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

public final class RosteredShiftListAdapter extends ShiftListAdapter<RawRosteredShiftEntity> {
    public RosteredShiftListAdapter(@NonNull Callbacks callbacks, @NonNull LiveShiftConfig liveShiftConfig) {
        super(callbacks, liveShiftConfig);
    }

    @Override
    boolean areContentsTheSame(@NonNull RawRosteredShiftEntity shift1, @NonNull RawRosteredShiftEntity shift2) {
        Log.i("Compare", String.format("id %d: starts: %s, wasCompliant: %s, isCompliant: %s", shift1.getId(), shift1.getShiftData().getStart().toString(), shift1.isCompliant(), shift2.isCompliant()));
        return super.areContentsTheSame(shift1, shift2) &&
                shift1.isCompliant() == shift2.isCompliant() &&
                (shift1.getLoggedShiftData() == null ? shift2.getLoggedShiftData() == null : (shift2.getLoggedShiftData() != null && Comparators.equalShiftData(shift1.getLoggedShiftData(), shift2.getLoggedShiftData())));
    }

    @Override
    int getSecondaryIcon(@NonNull RawRosteredShiftEntity shift) {
        return RawRosteredShiftEntity.getComplianceIcon(shift.isCompliant());
    }

    @Nullable
    @Override
    String getThirdLine(@NonNull RawRosteredShiftEntity shift, @NonNull ZoneId zoneId) {
        RawShift.RawShiftData loggedRawShiftData = shift.getLoggedShiftData();
        if (loggedRawShiftData == null) return null;
        final ZonedDateTime
                loggedStart = loggedRawShiftData.getStart().atZone(zoneId),
                loggedEnd = loggedRawShiftData.getEnd().atZone(zoneId);
        return DateTimeUtils.getTimeSpanString(loggedStart.toLocalDateTime(), loggedEnd.toLocalDateTime());
    }

}