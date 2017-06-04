package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftType;

abstract public class AbstractShiftListFragment extends LayoutManagerFragment {

    boolean mAddButtonJustClicked = false;

    @Override
    boolean shouldAddDivider() {
        return true;
    }

    @Override
    public int getLayout() {
        return R.layout.recycler_view;
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

    abstract void addShift(ShiftType shiftType);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_normal_day:
                addShift(ShiftType.NORMAL_DAY);
                return true;
            case R.id.add_long_day:
                addShift(ShiftType.LONG_DAY);
                return true;
            case R.id.add_night_shift:
                addShift(ShiftType.NIGHT_SHIFT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
