package com.skepticalone.mecachecker.shift;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.Compliance;
import com.skepticalone.mecachecker.NonCompliantTimeSpan;
import com.skepticalone.mecachecker.PeriodWithStableId;
import com.skepticalone.mecachecker.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {

    private static final String TAG = "ShiftAdapter";
    private final OnShiftClickListener mListener;
    private final List<PeriodWithStableId> mShifts = new ArrayList<>();

    ShiftAdapter(OnShiftClickListener listener) {
        super();
        setHasStableIds(true);
        mListener = listener;
    }

    void swapCursor(@Nullable Cursor cursor) {
        mShifts.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                mShifts.add(new PeriodWithStableId(
                        cursor.getLong(ShiftListActivity.COLUMN_INDEX_START),
                        cursor.getLong(ShiftListActivity.COLUMN_INDEX_END),
                        cursor.getLong(ShiftListActivity.COLUMN_INDEX_ID)
                ));
            } while (cursor.moveToNext());
            Compliance.checkMinimumRestHoursBetweenShifts(mShifts);
            Compliance.checkMaximumHoursPerDay(mShifts);
            Compliance.checkMaximumHoursPerWeek(mShifts);
            Compliance.checkMaximumHoursPerFortnight(mShifts);
            Compliance.checkMaximumConsecutiveWeekends(mShifts);
            for (PeriodWithStableId shift : mShifts) {
                Log.i(TAG, shift.toString());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mShifts.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shift_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PeriodWithStableId shift = mShifts.get(position);
//        holder.dateView.setText(sDateFormat.format(shift.getStart()));
        holder.dateView.setText(holder.dateView.getContext().getString(R.string.date_format, shift.getStart()));
//        holder.startView.setText(sTimeFormat.format(shift.getStart()));
        holder.startView.setText(holder.startView.getContext().getString(R.string.time_format, shift.getStart()));
//        holder.endView.setText(sTimeFormat.format(shift.getEnd()));
        holder.endView.setText(holder.endView.getContext().getString(shift.isSameDay() ? R.string.time_format : R.string.time_format_with_day, shift.getEnd()));
        holder.minimumRestHoursBetweenShiftsView.setVisibility(
                shift.isCompliantWithMinimumRestHoursBetweenShifts() ?
                        View.GONE :
                        View.VISIBLE
        );
        holder.minimumRestHoursBetweenShiftsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showDialogNonCompliantWithMinimumRestHoursBetweenShifts(shift.nonCompliantPeriodWithMinimumRestHoursBetweenShifts());
            }
        });
        holder.maximumHoursPerDayView.setVisibility(
                shift.isCompliantWithMaximumHoursPerDay() ?
                        View.GONE :
                        View.VISIBLE
        );
        holder.maximumHoursPerDayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showDialogNonCompliantWithMaximumHoursPerDay(shift.nonCompliantPeriodWithMaximumHoursPerDay());
            }
        });
        holder.maximumHoursPerWeekView.setVisibility(
                shift.isCompliantWithMaximumHoursPerWeek() ?
                        View.GONE :
                        View.VISIBLE
        );
        holder.maximumHoursPerWeekView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showDialogNonCompliantWithMaximumHoursPerWeek(shift.nonCompliantPeriodWithMaximumHoursPerWeek());
            }
        });
        holder.maximumHoursPerFortnightView.setVisibility(
                shift.isCompliantWithMaximumHoursPerFortnight() ?
                        View.GONE :
                        View.VISIBLE
        );
        holder.maximumHoursPerFortnightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showDialogNonCompliantWithMaximumHoursPerFortnight(shift.nonCompliantPeriodWithMaximumHoursPerFortnight());
            }
        });
        holder.maximumConsecutiveWeekendsView.setVisibility(
                shift.isCompliantWithMaximumConsecutiveWeekends() ?
                        View.GONE :
                        View.VISIBLE
        );
        holder.maximumConsecutiveWeekendsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.showDialogNonCompliantWithMaximumConsecutiveWeekends();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShiftClick(shift.getId());
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onShiftLongClick(shift.getId());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mShifts.size();
    }

    @Nullable
    Date getLastShiftEnd() {
        int size = getItemCount();
        if (size == 0) return null;
        return mShifts.get(size - 1).getEnd();
    }

    interface OnShiftClickListener {
        void onShiftClick(long id);
        void onShiftLongClick(long id);

        void showDialogNonCompliantWithMinimumRestHoursBetweenShifts(NonCompliantTimeSpan nonCompliantPeriod);

        void showDialogNonCompliantWithMaximumHoursPerDay(NonCompliantTimeSpan nonCompliantPeriod);

        void showDialogNonCompliantWithMaximumHoursPerWeek(NonCompliantTimeSpan nonCompliantPeriod);

        void showDialogNonCompliantWithMaximumHoursPerFortnight(NonCompliantTimeSpan nonCompliantPeriod);

        void showDialogNonCompliantWithMaximumConsecutiveWeekends();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView
                dateView,
                startView,
                endView,
                minimumRestHoursBetweenShiftsView,
                maximumHoursPerDayView,
                maximumHoursPerWeekView,
                maximumHoursPerFortnightView,
                maximumConsecutiveWeekendsView;

        ViewHolder(View view) {
            super(view);
            dateView = (TextView) view.findViewById(R.id.date);
            startView = (TextView) view.findViewById(R.id.start);
            endView = (TextView) view.findViewById(R.id.end);
            minimumRestHoursBetweenShiftsView = (TextView) view.findViewById(R.id.minimum_rest_hours_between_shifts);
            maximumHoursPerDayView = (TextView) view.findViewById(R.id.maximum_hours_per_day);
            maximumHoursPerWeekView = (TextView) view.findViewById(R.id.maximum_hours_per_week);
            maximumHoursPerFortnightView = (TextView) view.findViewById(R.id.maximum_hours_per_fortnight);
            maximumConsecutiveWeekendsView = (TextView) view.findViewById(R.id.maximum_consecutive_weekends);
        }
    }
}
