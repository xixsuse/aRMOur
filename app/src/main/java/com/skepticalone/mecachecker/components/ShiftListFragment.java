package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_shift:
                Calendar calendar = Calendar.getInstance();
                if (mCursor != null && mCursor.moveToLast()) {
                    calendar.setTimeInMillis(mCursor.getEnd());
                }
                long start = calendar.getTimeInMillis();
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, AppConstants.DEFAULT_START_MINUTE);
                calendar.set(Calendar.HOUR_OF_DAY, AppConstants.DEFAULT_START_HOUR_OF_DAY);
                if (calendar.getTimeInMillis() < start) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                start = calendar.getTimeInMillis();
                calendar.set(Calendar.MINUTE, AppConstants.DEFAULT_END_MINUTE);
                calendar.set(Calendar.HOUR_OF_DAY, AppConstants.DEFAULT_END_HOUR_OF_DAY);
                if (calendar.getTimeInMillis() < start) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                getActivity().getContentResolver().insert(ShiftProvider.shiftsUri, ShiftProvider.getContentValues(start, calendar.getTimeInMillis()));
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
            long start = mCursor.getStart();
            holder.dateView.setText(getString(R.string.date_format, start));
            holder.startView.setText(getString(R.string.time_format, start));
            holder.endView.setText(getString(mCursor.startsAndEndsOnSameDay() ? R.string.time_format : R.string.time_format_with_day, mCursor.getEnd()));
            boolean error = (position > 0 && mCursor.getDurationOfRest() < AppConstants.MINIMUM_DURATION_REST) ||
                    mCursor.getDurationOverDay() > AppConstants.MAXIMUM_DURATION_OVER_DAY ||
                    mCursor.getDurationOverWeek() > AppConstants.MAXIMUM_DURATION_OVER_WEEK ||
                    mCursor.getDurationOverFortnight() > AppConstants.MAXIMUM_DURATION_OVER_FORTNIGHT ||
                    mCursor.consecutiveWeekendsWorked();
            holder.mComplianceErrorView.setVisibility(error ? View.VISIBLE : View.GONE);
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            final TextView
                    dateView,
                    startView,
                    endView;
            final View mComplianceErrorView;

            CustomViewHolder(View itemView) {
                super(itemView);
                dateView = (TextView) itemView.findViewById(R.id.date);
                startView = (TextView) itemView.findViewById(R.id.start);
                endView = (TextView) itemView.findViewById(R.id.end);
                mComplianceErrorView = itemView.findViewById(R.id.compliance_error);
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
