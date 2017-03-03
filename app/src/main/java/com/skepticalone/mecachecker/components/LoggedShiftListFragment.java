package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;

import org.joda.time.Interval;


public class LoggedShiftListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_ID = 5;
    private static final String[] PROJECTION = new String[]{
            ShiftContract.RosteredShift._ID,
            ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_START,
            ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_END,
            ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_START,
            ShiftContract.RosteredShift.COLUMN_NAME_LOGGED_END
    };
    private static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_SCHEDULED_START = 1,
            COLUMN_INDEX_SCHEDULED_END = 2,
            COLUMN_INDEX_LOGGED_START = 3,
            COLUMN_INDEX_LOGGED_END = 4;
    private ShiftAdapter mAdapter;
    private ShiftClickListener mListener;
    private Cursor mCursor = null;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean mAddButtonJustClicked = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ShiftClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_list_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recycler);
        mLayoutManager = recyclerView.getLayoutManager();
        mAdapter = new ShiftAdapter();
        recyclerView.setAdapter(mAdapter);
        return layout;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.shift_list_menu, menu);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.raw);
        getLoaderManager().initLoader(LOADER_LIST_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftsUri, PROJECTION, null, null, ShiftContract.RosteredShift.COLUMN_NAME_ROSTERED_START);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mAdapter.notifyDataSetChanged();
        if (mAddButtonJustClicked) {
            mLayoutManager.scrollToPosition(mAdapter.getItemCount() - 1);
            mAddButtonJustClicked = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        switch (itemId) {
//            case R.id.add_normal_day:
//            case R.id.add_long_day:
//            case R.id.add_night_shift:
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                int startKeyId, defaultStartId, endKeyId, defaultEndId, skipWeekendsKeyId, defaultSkipWeekendsId;
//                if (itemId == R.id.add_normal_day) {
//                    startKeyId = R.string.key_start_normal_day;
//                    defaultStartId = R.integer.default_start_normal_day;
//                    endKeyId = R.string.key_end_normal_day;
//                    defaultEndId = R.integer.default_end_normal_day;
//                    skipWeekendsKeyId = R.string.key_skip_weekend_normal_day;
//                    defaultSkipWeekendsId = R.bool.default_skip_weekend_normal_day;
//                } else if (itemId == R.id.add_long_day) {
//                    startKeyId = R.string.key_start_long_day;
//                    defaultStartId = R.integer.default_start_long_day;
//                    endKeyId = R.string.key_end_long_day;
//                    defaultEndId = R.integer.default_end_long_day;
//                    skipWeekendsKeyId = R.string.key_skip_weekend_long_day;
//                    defaultSkipWeekendsId = R.bool.default_skip_weekend_long_day;
//                } else {
//                    startKeyId = R.string.key_start_night_shift;
//                    defaultStartId = R.integer.default_start_night_shift;
//                    endKeyId = R.string.key_end_night_shift;
//                    defaultEndId = R.integer.default_end_night_shift;
//                    skipWeekendsKeyId = R.string.key_skip_weekend_night_shift;
//                    defaultSkipWeekendsId = R.bool.default_skip_weekend_night_shift;
//                }
//                DateTime minStart;
//                if (mCursor != null && mCursor.moveToLast()) {
//                    minStart = new DateTime(mCursor.getLong(COLUMN_INDEX_SCHEDULED_END)).plus(AppConstants.MINIMUM_TIME_BETWEEN_SHIFTS);
//                } else {
//                    minStart = new DateTime().withTimeAtStartOfDay();
//                }
//                int startTotalMinutes = preferences.getInt(getString(startKeyId), getResources().getInteger(defaultStartId));
//                DateTime newStart = minStart.withTime(TimePreference.calculateHours(startTotalMinutes), TimePreference.calculateMinutes(startTotalMinutes), 0, 0);
//                boolean skipWeekends = preferences.getBoolean(getString(skipWeekendsKeyId), getResources().getBoolean(defaultSkipWeekendsId));
//                while (newStart.isBefore(minStart) || (skipWeekends && newStart.getDayOfWeek() >= DateTimeConstants.SATURDAY)) {
//                    newStart = newStart.plusDays(1);
//                }
//                int endTotalMinutes = preferences.getInt(getString(endKeyId), getResources().getInteger(defaultEndId));
//                DateTime newEnd = newStart.withTime(TimePreference.calculateHours(endTotalMinutes), TimePreference.calculateMinutes(endTotalMinutes), 0, 0);
//                if (!newEnd.isAfter(newStart)) {
//                    newEnd = newEnd.plusDays(1);
//                }
//                getActivity().getContentResolver().insert(ShiftProvider.shiftsUri, ShiftProvider.getContentValues(newStart.getMillis(), newEnd.getMillis()));
//                mAddButtonJustClicked = true;
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    class ShiftAdapter extends AbstractTwoLineAdapter {

        ShiftAdapter() {
            super();
            setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(COLUMN_INDEX_ID);
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final CustomViewHolder holder = super.onCreateViewHolder(parent, viewType);
            holder.primaryIconView.setVisibility(View.GONE);
            holder.secondaryIconView.setImageResource(R.drawable.ic_check_black_24dp);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRawShiftClicked(holder.getItemId());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return getActivity().getContentResolver().delete(ShiftProvider.shiftUri(holder.getItemId()), null, null) == 1;
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, int position) {
            mCursor.moveToPosition(position);
//            int shiftTypeDrawableId;
//            switch (mCursor.getShiftType()) {
//                case NORMAL_DAY:
//                    shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
//                    break;
//                case LONG_DAY:
//                    shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
//                    break;
//                case NIGHT_SHIFT:
//                    shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
//                    break;
//                case OTHER:
//                    shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
//                    break;
//                default:
//                    throw new IllegalArgumentException();
//            }
//            holder.primaryIconView.setImageResource(shiftTypeDrawableId);
            Interval shift = new Interval(mCursor.getLong(COLUMN_INDEX_SCHEDULED_START), mCursor.getLong(COLUMN_INDEX_SCHEDULED_END));
            holder.primaryTextView.setText(getString(R.string.day_date_format, shift.getStartMillis()));
            holder.secondaryTextView.setText(getString(R.string.time_span_format, shift.getStartMillis(), shift.getEndMillis()));
//            boolean error = AppConstants.hasInsufficientTimeBetweenShifts(mCursor.getTimeBetweenShifts()) ||
//                    AppConstants.exceedsDurationOverDay(mCursor.getDurationOverDay()) ||
//                    AppConstants.exceedsDurationOverWeek(mCursor.getDurationOverWeek()) ||
//                    AppConstants.exceedsDurationOverFortnight(mCursor.getDurationOverFortnight()) ||
//                    mCursor.consecutiveWeekendsWorked();
            holder.secondaryIconView.setVisibility((mCursor.isNull(COLUMN_INDEX_LOGGED_START) || mCursor.isNull(COLUMN_INDEX_LOGGED_END)) ? View.GONE : View.VISIBLE);
//            holder.secondaryIconView.setImageResource(error ? R.drawable.ic_warning_red_24dp : R.drawable.ic_check_black_24dp);
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

    }

}
