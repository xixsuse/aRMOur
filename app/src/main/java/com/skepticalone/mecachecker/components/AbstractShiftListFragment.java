package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftType;

import org.joda.time.LocalTime;

abstract public class AbstractShiftListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_ID = 0;
    ShiftClickListener mListener;
    ComplianceCursor mCursor = null;
    RecyclerView.LayoutManager mLayoutManager;

    abstract RecyclerView.Adapter getAdapter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ShiftClickListener) context;
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_list_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recycler);
        mLayoutManager = recyclerView.getLayoutManager();
        recyclerView.setAdapter(getAdapter());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shift_list_menu, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle());
        getLoaderManager().initLoader(LOADER_LIST_ID, null, this);
    }

    @StringRes
    abstract int getTitle();

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
