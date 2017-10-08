package com.skepticalone.armour.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.R;

import org.threeten.bp.ZoneId;

public final class LiveTimeZone extends LiveConfig<ZoneId> {

    @Nullable
    private static LiveTimeZone INSTANCE;

    @NonNull
    private final String keyTimeZoneId;

    @NonNull
    private final String[] watchKeys;

    private LiveTimeZone(@NonNull Context context) {
        keyTimeZoneId = context.getString(R.string.key_time_zone_id);
        watchKeys = new String[]{
                keyTimeZoneId
        };
    }

    @NonNull
    public static LiveTimeZone getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            synchronized (LiveTimeZone.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LiveTimeZone(context);
                    INSTANCE.init(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    @NonNull
    public String[] getWatchKeys() {
        return watchKeys;
    }

    @NonNull
    @Override
    public ZoneId getNewValue(@NonNull SharedPreferences sharedPreferences) {
        String zoneId = sharedPreferences.getString(keyTimeZoneId, null);
        return zoneId == null ? ZoneId.systemDefault() : ZoneId.of(zoneId);
    }

}
