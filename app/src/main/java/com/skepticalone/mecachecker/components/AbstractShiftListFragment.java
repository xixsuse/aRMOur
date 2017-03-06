package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftType;

import org.joda.time.LocalTime;

abstract public class AbstractShiftListFragment extends ShiftTypeVariableFragment {

    boolean mAddButtonJustClicked = false;

    @Override
    boolean shouldAddDivider() {
        return true;
    }

    @Override
    int getLayout() {
        return R.layout.shift_list_fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shift_list_menu, menu);
    }

    abstract void addShift(ShiftType shiftType, @NonNull LocalTime startTime, @NonNull LocalTime endTime);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.add_normal_day:
            case R.id.add_long_day:
            case R.id.add_night_shift:
                int startKeyId, defaultStartId, endKeyId, defaultEndId;
                ShiftType shiftType;
                if (itemId == R.id.add_normal_day) {
                    shiftType = ShiftType.NORMAL_DAY;
                    startKeyId = R.string.key_start_normal_day;
                    defaultStartId = R.integer.default_start_normal_day;
                    endKeyId = R.string.key_end_normal_day;
                    defaultEndId = R.integer.default_end_normal_day;
                } else if (itemId == R.id.add_long_day) {
                    shiftType = ShiftType.LONG_DAY;
                    startKeyId = R.string.key_start_long_day;
                    defaultStartId = R.integer.default_start_long_day;
                    endKeyId = R.string.key_end_long_day;
                    defaultEndId = R.integer.default_end_long_day;
                } else {
                    shiftType = ShiftType.NIGHT_SHIFT;
                    startKeyId = R.string.key_start_night_shift;
                    defaultStartId = R.integer.default_start_night_shift;
                    endKeyId = R.string.key_end_night_shift;
                    defaultEndId = R.integer.default_end_night_shift;
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                int startTotalMinutes = preferences.getInt(getString(startKeyId), getResources().getInteger(defaultStartId));
                LocalTime startTime = new LocalTime(TimePreference.calculateHours(startTotalMinutes), TimePreference.calculateMinutes(startTotalMinutes));
                int endTotalMinutes = preferences.getInt(getString(endKeyId), getResources().getInteger(defaultEndId));
                LocalTime endTime = new LocalTime(TimePreference.calculateHours(endTotalMinutes), TimePreference.calculateMinutes(endTotalMinutes));
                addShift(shiftType, startTime, endTime);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
