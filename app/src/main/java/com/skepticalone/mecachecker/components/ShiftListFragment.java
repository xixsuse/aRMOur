package com.skepticalone.mecachecker.components;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.AppConstants;

import java.util.Calendar;

public class ShiftListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_LIST_ID = 0;
    private ShiftAdapter mAdapter;
    private Listener mListener;
    private ComplianceCursor mCursor = null;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean mAddButtonJustClicked = false;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_list_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.shift_list);
        mLayoutManager = recyclerView.getLayoutManager();
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                getActivity().getContentResolver().delete(ShiftProvider.shiftUri(viewHolder.getItemId()), null, null);
            }
        }).attachToRecyclerView(recyclerView);
        mAdapter = new ShiftAdapter();
        recyclerView.setAdapter(mAdapter);
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shift_list_menu, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_LIST_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftsUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = new ComplianceCursor(data);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.add_normal_day:
            case R.id.add_long_day:
            case R.id.add_night_shift:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Log.d("PREFERENCES LIST", "size: " + preferences.getAll().size());
                int startKey, defaultStartId, endKey, defaultEndId;
                if (itemId == R.id.add_normal_day) {
                    startKey = R.string.key_start_normal_day;
                    defaultStartId = R.integer.default_start_normal_day;
                    endKey = R.string.key_end_normal_day;
                    defaultEndId = R.integer.default_end_normal_day;
                } else if (itemId == R.id.add_long_day) {
                    startKey = R.string.key_start_long_day;
                    defaultStartId = R.integer.default_start_long_day;
                    endKey = R.string.key_end_long_day;
                    defaultEndId = R.integer.default_end_long_day;
                } else {
                    startKey = R.string.key_start_night_shift;
                    defaultStartId = R.integer.default_start_night_shift;
                    endKey = R.string.key_end_night_shift;
                    defaultEndId = R.integer.default_end_night_shift;
                }
                int startTotalMinutes = preferences.getInt(getString(startKey), getResources().getInteger(defaultStartId));
                int endTotalMinutes = preferences.getInt(getString(endKey), getResources().getInteger(defaultEndId));
                Calendar calendar = Calendar.getInstance();
                if (mCursor != null && mCursor.moveToLast()) {
                    calendar.setTimeInMillis(mCursor.getEnd());
                }
                long start = calendar.getTimeInMillis();
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, TimePreference.calculateMinutes(startTotalMinutes));
                calendar.set(Calendar.HOUR_OF_DAY, TimePreference.calculateHours(startTotalMinutes));
                if (calendar.getTimeInMillis() < start) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                start = calendar.getTimeInMillis();
                calendar.set(Calendar.MINUTE, TimePreference.calculateMinutes(endTotalMinutes));
                calendar.set(Calendar.HOUR_OF_DAY, TimePreference.calculateHours(endTotalMinutes));
                if (calendar.getTimeInMillis() < start) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                getActivity().getContentResolver().insert(ShiftProvider.shiftsUri, ShiftProvider.getContentValues(start, calendar.getTimeInMillis()));
                mAddButtonJustClicked = true;
                return true;
            case R.id.settings:
                getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    interface Listener {
        void onShiftClicked(long shiftId);
    }

    class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.CustomViewHolder> {

        ShiftAdapter() {
            super();
            setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getId();
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shift_list_content, parent, false));
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            int shiftTypeDrawableId;
            switch (mCursor.getShiftType()) {
                case ComplianceCursor.SHIFT_TYPE_NORMAL_DAY:
                    shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
                    break;
                case ComplianceCursor.SHIFT_TYPE_LONG_DAY:
                    shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
                    break;
                case ComplianceCursor.SHIFT_TYPE_NIGHT_SHIFT:
                    shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
                    break;
                default:
                    shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
                    break;
            }
            holder.shiftIconView.setImageResource(shiftTypeDrawableId);
            long start = mCursor.getStart(), end = mCursor.getEnd();
            holder.dayView.setText(getString(R.string.day_format, start));
            holder.dateView.setText(getString(R.string.date_format, start));
            holder.startView.setText(getString(R.string.time_format, start));
            holder.endView.setText(getString(R.string.time_format, end));
            boolean error = (position > 0 && mCursor.getDurationOfRest() < AppConstants.MINIMUM_DURATION_REST) ||
                    mCursor.getDurationOverDay() > AppConstants.MAXIMUM_DURATION_OVER_DAY ||
                    mCursor.getDurationOverWeek() > AppConstants.MAXIMUM_DURATION_OVER_WEEK ||
                    mCursor.getDurationOverFortnight() > AppConstants.MAXIMUM_DURATION_OVER_FORTNIGHT ||
                    mCursor.consecutiveWeekendsWorked();
            holder.itemView.setBackgroundResource(error ? R.color.colorBackgroundError : android.R.color.background_light);
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            final TextView
                    dayView,
                    dateView,
                    startView,
                    endView;
            final ImageView shiftIconView;

            CustomViewHolder(View itemView) {
                super(itemView);
                dayView = (TextView) itemView.findViewById(R.id.day);
                dateView = (TextView) itemView.findViewById(R.id.date);
                startView = (TextView) itemView.findViewById(R.id.start);
                endView = (TextView) itemView.findViewById(R.id.end);
                shiftIconView = (ImageView) itemView.findViewById(R.id.shift_icon);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onShiftClicked(getItemId());
                    }
                });
            }
        }
    }
}
