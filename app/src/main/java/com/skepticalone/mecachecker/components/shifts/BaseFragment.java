package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;


abstract class BaseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @StringRes
    abstract int getTitle();

    abstract int getLoaderId();

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle());
        getLoaderManager().initLoader(getLoaderId(), null, this);
    }

}
