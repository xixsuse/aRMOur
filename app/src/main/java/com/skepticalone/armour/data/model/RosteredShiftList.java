package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.compliance.Configuration;
import com.skepticalone.armour.data.compliance.ConfigurationSaferRosters;
import com.skepticalone.armour.data.compliance.LiveComplianceConfig;
import com.skepticalone.armour.data.compliance.RosteredDayOff;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class RosteredShiftList extends ShiftList<RosteredShiftEntity, RosteredShift> {

    @NonNull
    private final LiveData<Configuration> liveComplianceConfig;

    public RosteredShiftList(@NonNull Context context, @NonNull LiveData<List<RosteredShiftEntity>> liveRawRosteredShifts) {
        super(context, liveRawRosteredShifts);
        liveComplianceConfig = LiveComplianceConfig.getInstance(context);
        addSource(liveComplianceConfig, new Observer<Configuration>() {
            @Override
            public void onChanged(@Nullable Configuration complianceConfig) {
                onUpdated(liveRawItems.getValue(), liveTimeZone.getValue(), liveShiftConfig.getValue(), complianceConfig);
            }
        });
    }

    @Override
    void onUpdated(@Nullable List<RosteredShiftEntity> rawRosteredShifts, @Nullable ZoneId timeZone, @Nullable Shift.ShiftType.Configuration shiftConfig) {
        onUpdated(rawRosteredShifts, timeZone, shiftConfig, liveComplianceConfig.getValue());
    }

    private void onUpdated(@Nullable List<RosteredShiftEntity> rawRosteredShifts, @Nullable ZoneId timeZone, @Nullable Shift.ShiftType.Configuration shiftConfig, @Nullable Configuration complianceConfig) {
        if (rawRosteredShifts != null && timeZone != null && shiftConfig != null && complianceConfig != null) {
            List<RosteredShift> rosteredShifts = new ArrayList<>(rawRosteredShifts.size());
            for (RosteredShiftEntity rawRosteredShift : rawRosteredShifts) {
                rosteredShifts.add(new RosteredShift(rawRosteredShift, timeZone, shiftConfig, rosteredShifts, complianceConfig));
            }
            if (complianceConfig instanceof ConfigurationSaferRosters) {
                RosteredDayOff.process(rosteredShifts, (ConfigurationSaferRosters) complianceConfig);
            }
            setValue(rosteredShifts);
        }
    }

}
