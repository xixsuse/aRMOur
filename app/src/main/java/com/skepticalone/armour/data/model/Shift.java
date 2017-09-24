package com.skepticalone.armour.data.model;

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

    enum ShiftType {

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
