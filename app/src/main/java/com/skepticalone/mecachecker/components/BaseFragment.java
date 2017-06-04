package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.util.ShiftTypeCalculator;

public abstract class BaseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ShiftTypeCalculator shiftTypeCalculator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        shiftTypeCalculator = new ShiftTypeCalculator(context.getResources());
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getTitle());
        getLoaderManager().initLoader(getLoaderId(), null, this);
    }

    @LayoutRes
    protected abstract int getLayout();

    protected abstract int getLoaderId();

    @StringRes
    protected abstract int getTitle();

}
