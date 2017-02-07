package com.skepticalone.mecachecker.shift;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.PeriodWithStableId;
import com.skepticalone.mecachecker.R;


public class ShiftDetailFragment extends Fragment {

    private static final String SHIFT_ID = "SHIFT_ID";
    private TextView mDateView, mStartTimeView, mEndTimeView;
    private Listener mListener;
    private long mShiftId;

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
        mListener = (Listener) context;
        mShiftId = getArguments().getLong(SHIFT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_detail_fragment, container, false);
        PeriodWithStableId shift = getShift();
        mDateView = (TextView) layout.findViewById(R.id.date);
        mDateView.setText(getString(R.string.date_format, shift.getStart()));
        mStartTimeView = (TextView) layout.findViewById(R.id.startTime);
        mStartTimeView.setText(getString(R.string.time_format, shift.getStart()));
        mEndTimeView = (TextView) layout.findViewById(R.id.endTime);
        mEndTimeView.setText(getString(shift.isSameDay() ? R.string.time_format : R.string.time_format_with_day, shift.getEnd()));
        return layout;
    }

    PeriodWithStableId getShift() {
        return mListener.getShift(mShiftId);
    }

    interface Listener {
        //        void onAddShiftClicked();
//        void onShiftClicked(PeriodWithStableId shift);
//        void onShiftSwiped(long shiftId);
//        void onShiftLongClicked(PeriodWithStableId shift);
//        int getShiftCount();
        PeriodWithStableId getShift(long id);
    }
}
