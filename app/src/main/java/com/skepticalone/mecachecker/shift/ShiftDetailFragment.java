package com.skepticalone.mecachecker.shift;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftProvider;


public class ShiftDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String SHIFT_ID = "SHIFT_ID";
    private long mShiftId;
    private TextView mDateView, mStartTimeView, mEndTimeView;

    static ShiftDetailFragment create(long id) {
        ShiftDetailFragment fragment = new ShiftDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(SHIFT_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mShiftId = getArguments().getLong(SHIFT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_detail_fragment, container, false);
        mDateView = (TextView) layout.findViewById(R.id.date);
        mStartTimeView = (TextView) layout.findViewById(R.id.startTime);
        mEndTimeView = (TextView) layout.findViewById(R.id.endTime);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(OverviewActivity.LOADER_DETAIL_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftWithComplianceUri(mShiftId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            long start = data.getLong(ComplianceCursor.COLUMN_INDEX_START);
            long end = data.getLong(ComplianceCursor.COLUMN_INDEX_END);
            mDateView.setText(getString(R.string.date_format, start));
            mStartTimeView.setText(getString(R.string.time_format, start));
            mEndTimeView.setText(getString(R.string.time_format_with_day, end));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
//
//    interface Listener {
//        //        void onAddShiftClicked();
////        void onShiftClicked(PeriodWithStableId shift);
////        void onShiftSwiped(long shiftId);
////        void onShiftLongClicked(PeriodWithStableId shift);
////        int getShiftCount();
//        PeriodWithStableId getShift(long id);
//    }
}
