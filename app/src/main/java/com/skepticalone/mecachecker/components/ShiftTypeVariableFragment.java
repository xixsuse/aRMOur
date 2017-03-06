package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;

import com.skepticalone.mecachecker.R;


public class ShiftTypeVariableFragment extends Fragment {

    String normalDayStartKey,
            normalDayEndKey,
            longDayStartKey,
            longDayEndKey,
            nightShiftStartKey,
            nightShiftEndKey;
    int
            normalDayStartDefault,
            normalDayEndDefault,
            longDayStartDefault,
            longDayEndDefault,
            nightShiftStartDefault,
            nightShiftEndDefault;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Resources resources = context.getResources();
        normalDayStartKey = resources.getString(R.string.key_start_normal_day);
        normalDayEndKey = resources.getString(R.string.key_end_normal_day);
        longDayStartKey = resources.getString(R.string.key_start_long_day);
        longDayEndKey = resources.getString(R.string.key_end_long_day);
        nightShiftStartKey = resources.getString(R.string.key_start_night_shift);
        nightShiftEndKey = resources.getString(R.string.key_end_night_shift);
        normalDayStartDefault = resources.getInteger(R.integer.default_start_normal_day);
        normalDayEndDefault = resources.getInteger(R.integer.default_end_normal_day);
        longDayStartDefault = resources.getInteger(R.integer.default_start_long_day);
        longDayEndDefault = resources.getInteger(R.integer.default_end_long_day);
        nightShiftStartDefault = resources.getInteger(R.integer.default_start_night_shift);
        nightShiftEndDefault = resources.getInteger(R.integer.default_end_night_shift);
    }

}
