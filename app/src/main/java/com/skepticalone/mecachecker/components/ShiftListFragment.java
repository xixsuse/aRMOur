package com.skepticalone.mecachecker.components;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.util.AppConstants;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.Calendar;

public class ShiftListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CustomAdapter mAdapter;
    private Listener mListener;
    private ComplianceCursor mCursor = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_list_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.shift_list);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                getActivity().getContentResolver().delete(ShiftProvider.shiftUri(viewHolder.getItemId()), null, null);
            }
        }).attachToRecyclerView(recyclerView);
        mAdapter = new CustomAdapter();
        recyclerView.setAdapter(mAdapter);
        layout.findViewById(R.id.add_shift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(OverviewActivity.LOADER_LIST_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.shiftsUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = (ComplianceCursor) data;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }

    interface Listener {
        void onShiftClicked(long shiftId);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        CustomAdapter() {
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
//            long durationOverDay = mCursor.getDurationOverDay();
//            if (durationOverDay > AppConstants.MAXIMUM_DURATION_OVER_DAY) {
//                holder.maximumHoursPerDayView.setText(DurationFormat.getDurationString(getActivity(), durationOverDay));
//                holder.maximumHoursPerDayView.setVisibility(View.VISIBLE);
//            } else {
//                holder.maximumHoursPerDayView.setVisibility(View.GONE);
//            }
//            holder.maximumHoursPerDayView.setVisibility(
//                    mCursor.getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_DAY) == 1 ?
//                            View.GONE :
//                            View.VISIBLE
//            );
//            holder.maximumHoursPerDayView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMaximumHoursPerDay(shift.nonCompliantPeriodWithMaximumHoursPerDay());
//                }
//            });
//            holder.maximumHoursPerWeekView.setVisibility(
//                    View.GONE
//                    shift.isCompliantWithMaximumHoursPerWeek() ?
//                            View.GONE :
//                            View.VISIBLE
//            );
//            holder.maximumHoursPerWeekView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMaximumHoursPerWeek(shift.nonCompliantPeriodWithMaximumHoursPerWeek());
//                }
//            });
//            holder.maximumHoursPerFortnightView.setVisibility(
//                    View.GONE

//                    shift.isCompliantWithMaximumHoursPerFortnight() ?
//                            View.GONE :
//                            View.VISIBLE
//            );
//            holder.maximumHoursPerFortnightView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMaximumHoursPerFortnight(shift.nonCompliantPeriodWithMaximumHoursPerFortnight());
//                }
//            });
//            holder.maximumConsecutiveWeekendsView.setVisibility(
//                    View.GONE
//                    shift.isCompliantWithMaximumConsecutiveWeekends() ?
//                            View.GONE :
//                            View.VISIBLE
//            );
//            holder.maximumConsecutiveWeekendsView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMaximumConsecutiveWeekends();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            final TextView
                    dateView,
                    startView,
                    endView,
                    minimumRestHoursBetweenShiftsView,
                    maximumHoursPerDayView,
                    maximumHoursPerWeekView,
                    maximumHoursPerFortnightView,
                    maximumConsecutiveWeekendsView;

            CustomViewHolder(View itemView) {
                super(itemView);
                dateView = (TextView) itemView.findViewById(R.id.date);
                startView = (TextView) itemView.findViewById(R.id.start);
                endView = (TextView) itemView.findViewById(R.id.end);
                minimumRestHoursBetweenShiftsView = (TextView) itemView.findViewById(R.id.minimum_rest_hours_between_shifts);
                maximumHoursPerDayView = (TextView) itemView.findViewById(R.id.maximum_hours_per_day);
                maximumHoursPerWeekView = (TextView) itemView.findViewById(R.id.maximum_hours_per_week);
                maximumHoursPerFortnightView = (TextView) itemView.findViewById(R.id.maximum_hours_per_fortnight);
                maximumConsecutiveWeekendsView = (TextView) itemView.findViewById(R.id.maximum_consecutive_weekends);
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
