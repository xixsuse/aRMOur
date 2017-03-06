package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;


public abstract class ShiftTypeVariableFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final int
            LOADER_ID_ROSTERED_LIST = 1,
            LOADER_ID_ROSTERED_DETAIL = 2,
            LOADER_ID_ADDITIONAL_LIST = 3,
            LOADER_ID_ADDITIONAL_DETAIL = 4;
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
    RecyclerView.LayoutManager mLayoutManager;

    abstract RecyclerView.Adapter getAdapter();

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

    abstract int getLoaderId();

    @StringRes
    abstract int getTitle();

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle());
        getLoaderManager().initLoader(getLoaderId(), null, this);
    }

    @LayoutRes
    abstract int getLayout();

    abstract boolean shouldAddDivider();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(getLayout(), container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recycler);
        mLayoutManager = recyclerView.getLayoutManager();
        recyclerView.setAdapter(getAdapter());
        if (shouldAddDivider()) {
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
            );
        }
        return layout;
    }
}
