package com.skepticalone.mecachecker;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A fragment representing a single Shift detail screen.
 * This fragment is either contained in a {@link ShiftListActivity}
 * in two-pane mode (on tablets) or a {@link ShiftDetailActivity}
 * on handsets.
 */
public class ShiftDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_START = "START";
    public static final String ARG_END = "END";

    /**
     * The dummy content this fragment is presenting.
     */
    private Shift.DummyItem mItem;
    private TextView mStartTime, mEndTime, mDuration;
    private Calendar mStart = Calendar.getInstance(), mEnd = Calendar.getInstance();
    private DateFormat mDateFormat = new SimpleDateFormat("d/MM/yyyy HH:mm", Locale.US);
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShiftDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Shift.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.shift_detail_2, container, false);
        mStartTime = (TextView) rootView.findViewById(R.id.startTime);
        assert mStartTime != null;
        mEndTime = (TextView) rootView.findViewById(R.id.endTime);
        assert mEndTime != null;
        mDuration = (TextView) rootView.findViewById(R.id.duration);
        assert mDuration != null;
        return rootView;
//        View rootView = inflater.inflate(R.layout.shift_detail, container, false);
//
//        // Show the dummy content as text in a TextView.
//        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.shift_detail)).setText(mItem.details);
//        }
//
//        return rootView;
    }

    public void setTime(boolean start, int hour, int minute){
        if (start) {
            mStart.set(Calendar.HOUR_OF_DAY, hour);
            mStart.set(Calendar.MINUTE, minute);
        } else {
            mEnd.set(Calendar.HOUR_OF_DAY, hour);
            mEnd.set(Calendar.MINUTE, minute);
        }
        while (mEnd.before(mStart)){
            mEnd.add(Calendar.DATE, 1);
        }
        mStartTime.setText("Start: " + mDateFormat.format(mStart.getTime()));
        mEndTime.setText("End: " + mDateFormat.format(mEnd.getTime()));
        mDuration.setText((
                (double)(mEnd.getTimeInMillis() - mStart.getTimeInMillis())
        )/(1000 * 60 * 60) + " hours");
    }
}
