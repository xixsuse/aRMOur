package com.skepticalone.armour.util;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.skepticalone.armour.R;

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

}
