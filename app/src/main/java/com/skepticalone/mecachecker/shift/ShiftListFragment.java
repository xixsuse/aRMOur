package com.skepticalone.mecachecker.shift;

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

import com.skepticalone.mecachecker.AppConstants;
import com.skepticalone.mecachecker.DurationFormat;
import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ComplianceCursor;
import com.skepticalone.mecachecker.data.ShiftProvider;

import java.util.Calendar;
import java.util.Date;

public class ShiftListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CustomAdapter mAdapter;
    private Listener mListener;
    private Cursor mCursor = null;
    private final static Calendar sStart = Calendar.getInstance(), sEnd = Calendar.getInstance();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }


//    public void notifyDataSetChanged() {
//        mAdapter.notifyDataSetChanged();
//        mLayoutManager.scrollToPosition(mListener.getShiftCount() - 1);
//    }

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
//                switch (direction){
//                    case ItemTouchHelper.START:
//                        Log.i(TAG, "onSwiped: Start");
//                        break;
//                    case ItemTouchHelper.END:
//                        Log.i(TAG, "onSwiped: End");
//                        break;
//                    default:
//                        Log.i(TAG, "onSwiped: Unknown: " + direction);
//                        break;
//                }
                mListener.onShiftSwiped(viewHolder.getItemId());
            }
        }).attachToRecyclerView(recyclerView);
        mAdapter = new CustomAdapter();
        recyclerView.setAdapter(mAdapter);
        layout.findViewById(R.id.add_shift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date lastShiftEnd = new Date();
                if (mCursor != null && mCursor.moveToLast()) {
                    lastShiftEnd.setTime(mCursor.getLong(ComplianceCursor.COLUMN_INDEX_END));
                }
                mListener.onAddShiftClicked(lastShiftEnd);
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
        return new CursorLoader(getActivity(), ShiftProvider.shiftsWithComplianceUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }

    interface Listener {
        void onAddShiftClicked(Date lastShiftEnd);

        void onShiftClicked(long shiftId);

        void onShiftSwiped(long shiftId);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        CustomAdapter() {
            super();
            setHasStableIds(true);
        }
//
//        void swapCursor(Cursor cursor){
//            mCursor = cursor;
//            notifyDataSetChanged();
//        }

        @Override
        public long getItemId(int position) {
            mCursor.moveToPosition(position);
            return mCursor.getLong(ComplianceCursor.COLUMN_INDEX_ID);
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shift_list_content, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            final long shiftId = mCursor.getLong(ComplianceCursor.COLUMN_INDEX_ID);
            sStart.setTimeInMillis(mCursor.getLong(ComplianceCursor.COLUMN_INDEX_START));
            sEnd.setTimeInMillis(mCursor.getLong(ComplianceCursor.COLUMN_INDEX_END));
            holder.dateView.setText(holder.dateView.getContext().getString(R.string.date_format, sStart));
            holder.startView.setText(holder.startView.getContext().getString(R.string.time_format, sStart));
            holder.endView.setText(holder.endView.getContext().getString(sStart.get(Calendar.DAY_OF_MONTH) == sEnd.get(Calendar.DAY_OF_MONTH) ? R.string.time_format : R.string.time_format_with_day, sEnd));
//            holder.minimumRestHoursBetweenShiftsView.setVisibility(
//                    View.GONE
//                    shift.isCompliantWithMinimumRestHoursBetweenShifts() ?
//                            View.GONE :
//                            View.VISIBLE
//            );
//            holder.minimumRestHoursBetweenShiftsView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMinimumRestHoursBetweenShifts(shift.nonCompliantPeriodWithMinimumRestHoursBetweenShifts());
//                }
//            });
            long durationOverDay = mCursor.getLong(ComplianceCursor.COLUMN_INDEX_DURATION_OVER_DAY);
            if (durationOverDay > AppConstants.MAXIMUM_DURATION_OVER_DAY) {
                holder.maximumHoursPerDayView.setText(DurationFormat.getDurationString(getActivity(), durationOverDay));
                holder.maximumHoursPerDayView.setVisibility(View.VISIBLE);
            } else {
                holder.maximumHoursPerDayView.setVisibility(View.GONE);
            }
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onShiftClicked(shiftId);
                }
            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mListener.onShiftLongClicked(shift);
//                    return true;
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
            }
        }
    }
}
