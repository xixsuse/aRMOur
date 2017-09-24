package com.skepticalone.armour.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Pair;

import com.skepticalone.armour.R;
import com.skepticalone.armour.settings.TimePreference;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

public abstract class Shift extends Item {

    @NonNull
    private final ShiftData shiftData;
    @NonNull
    private final ShiftType shiftType;

//    Shift(long id, @Nullable String comment, @NonNull ShiftData shiftData, @NonNull ShiftType.Configuration configuration) {
//        super(id, comment);
//        this.shiftData = shiftData;
//        shiftType = ShiftType.from(getShiftData().getStart().toLocalTime(), getShiftData().getEnd().toLocalTime(), configuration);
//    }

    Shift(@NonNull RawShift rawShift, @NonNull ZoneId zoneId, @NonNull ShiftType.Configuration configuration) {
//        this(rawShift.getId(), rawShift.getComment(), new ShiftData(rawShift.getShiftData(), zoneId), configuration);
        super(rawShift.getId(), rawShift.getComment());
        shiftData = new ShiftData(rawShift.getShiftData(), zoneId);
        shiftType = ShiftType.from(shiftData.getStart().toLocalTime(), shiftData.getEnd().toLocalTime(), configuration);
    }

    @NonNull
    public final ShiftData getShiftData() {
        return shiftData;
    }

    @NonNull
    public final ShiftType getShiftType() {
        return shiftType;
    }

    public enum ShiftType {

        NORMAL_DAY(R.drawable.ic_normal_day_black_24dp, R.string.normal_day, R.string.normal_days),
        LONG_DAY(R.drawable.ic_long_day_black_24dp, R.string.long_day, R.string.long_days),
        NIGHT_SHIFT(R.drawable.ic_night_shift_black_24dp, R.string.night_shift, R.string.night_shifts),
        CUSTOM(R.drawable.ic_custom_shift_black_24dp, R.string.custom_shift, R.string.custom_shifts);

        @DrawableRes
        private final int icon;

        @StringRes
        private final int singularTitle, pluralTitle;

        ShiftType(@DrawableRes int icon, @StringRes int singularTitle, @StringRes int pluralTitle) {
            this.icon = icon;
            this.singularTitle = singularTitle;
            this.pluralTitle = pluralTitle;
        }

        @DrawableRes
        public final int getIcon() {
            return icon;
        }

        @StringRes
        public final int getSingularTitle() {
            return singularTitle;
        }

        @StringRes
        public final int getPluralTitle() {
            return pluralTitle;
        }

        @NonNull
        public final Pair<LocalTime, LocalTime> getTimes(@NonNull Configuration configuration) {
            final int startTotalMinutes, endTotalMinutes;
            switch (this) {
                case NORMAL_DAY:
                    startTotalMinutes = configuration.normalDayStart;
                    endTotalMinutes = configuration.normalDayEnd;
                    break;
                case LONG_DAY:
                    startTotalMinutes = configuration.longDayStart;
                    endTotalMinutes = configuration.longDayEnd;
                    break;
                case NIGHT_SHIFT:
                    startTotalMinutes = configuration.nightShiftStart;
                    endTotalMinutes = configuration.nightShiftEnd;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            return new Pair<>(TimePreference.getTime(startTotalMinutes), TimePreference.getTime(endTotalMinutes));
        }

        @NonNull
        public static ShiftType from(@NonNull LocalTime start, @NonNull LocalTime end, @NonNull Configuration configuration){
            final int startTotalMinutes = TimePreference.getTotalMinutes(start),
                    endTotalMinutes = TimePreference.getTotalMinutes(end);
            if (startTotalMinutes == configuration.normalDayStart && endTotalMinutes == configuration.normalDayEnd) {
                return NORMAL_DAY;
            } else if (startTotalMinutes == configuration.longDayStart && endTotalMinutes == configuration.longDayEnd) {
                return LONG_DAY;
            } else if (startTotalMinutes == configuration.nightShiftStart && endTotalMinutes == configuration.nightShiftEnd) {
                return NIGHT_SHIFT;
            } else {
                return CUSTOM;
            }
        }

        static final class Configuration {

            private final int
                    normalDayStart,
                    normalDayEnd,
                    longDayStart,
                    longDayEnd,
                    nightShiftStart,
                    nightShiftEnd;

            Configuration(
                    int normalDayStart,
                    int normalDayEnd,
                    int longDayStart,
                    int longDayEnd,
                    int nightShiftStart,
                    int nightShiftEnd
            ) {
                this.normalDayStart = normalDayStart;
                this.normalDayEnd = normalDayEnd;
                this.longDayStart = longDayStart;
                this.longDayEnd = longDayEnd;
                this.nightShiftStart = nightShiftStart;
                this.nightShiftEnd = nightShiftEnd;
            }

        }

        static final class LiveShiftConfig extends LiveConfig<Configuration> {

            @Nullable
            private static LiveShiftConfig INSTANCE;

            @NonNull
            private final String
                    keyNormalDayStart,
                    keyNormalDayEnd,
                    keyLongDayStart,
                    keyLongDayEnd,
                    keyNightShiftStart,
                    keyNightShiftEnd;

            private final int
                    defaultNormalDayStart,
                    defaultNormalDayEnd,
                    defaultLongDayStart,
                    defaultLongDayEnd,
                    defaultNightShiftStart,
                    defaultNightShiftEnd;

            @NonNull
            private final String[] watchKeys;

            private LiveShiftConfig(@NonNull Resources resources) {
                keyNormalDayStart = resources.getString(R.string.key_start_normal_day);
                keyNormalDayEnd = resources.getString(R.string.key_end_normal_day);
                keyLongDayStart = resources.getString(R.string.key_start_long_day);
                keyLongDayEnd = resources.getString(R.string.key_end_long_day);
                keyNightShiftStart = resources.getString(R.string.key_start_night_shift);
                keyNightShiftEnd = resources.getString(R.string.key_end_night_shift);
//                keyTimeZoneId = resources.getString(R.string.key_time_zone_id);
                defaultNormalDayStart = resources.getInteger(R.integer.default_start_normal_day);
                defaultNormalDayEnd = resources.getInteger(R.integer.default_end_normal_day);
                defaultLongDayStart = resources.getInteger(R.integer.default_start_long_day);
                defaultLongDayEnd = resources.getInteger(R.integer.default_end_long_day);
                defaultNightShiftStart = resources.getInteger(R.integer.default_start_night_shift);
                defaultNightShiftEnd = resources.getInteger(R.integer.default_end_night_shift);
                watchKeys = new String[]{
                        keyNormalDayStart,
                        keyNormalDayEnd,
                        keyLongDayStart,
                        keyLongDayEnd,
                        keyNightShiftStart,
                        keyNightShiftEnd
                };
            }

            @NonNull
            public static LiveShiftConfig getInstance(@NonNull Context context) {
                if (INSTANCE == null) {
                    synchronized (LiveShiftConfig.class) {
                        if (INSTANCE == null) {
                            INSTANCE = new LiveShiftConfig(context.getResources());
                            INSTANCE.init(context);
                        }
                    }
                }
                return INSTANCE;
            }

            @Override
            @NonNull
            String[] getWatchKeys() {
                return watchKeys;
            }

            @NonNull
            @Override
            Configuration getNewValue(@NonNull SharedPreferences sharedPreferences) {
                return new Configuration(
                        sharedPreferences.getInt(keyNormalDayStart, defaultNormalDayStart),
                        sharedPreferences.getInt(keyNormalDayEnd, defaultNormalDayEnd),
                        sharedPreferences.getInt(keyLongDayStart, defaultLongDayStart),
                        sharedPreferences.getInt(keyLongDayEnd, defaultLongDayEnd),
                        sharedPreferences.getInt(keyNightShiftStart, defaultNightShiftStart),
                        sharedPreferences.getInt(keyNightShiftEnd, defaultNightShiftEnd)
                );
            }

        }
    }

    static final class ShiftData {

        @NonNull
        private final ZonedDateTime start, end;
        @NonNull
        private final Duration duration;

        ShiftData(@NonNull RawShift.ShiftData rawShiftData, @NonNull ZoneId zoneId) {
            start = rawShiftData.getStart().atZone(zoneId);
            end = rawShiftData.getEnd().atZone(zoneId);
            duration = Duration.between(start, end);
        }

        @NonNull
        public ZonedDateTime getStart() {
            return start;
        }

        @NonNull
        public ZonedDateTime getEnd() {
            return end;
        }

        @NonNull
        public Duration getDuration() {
            return duration;
        }

    }

}
