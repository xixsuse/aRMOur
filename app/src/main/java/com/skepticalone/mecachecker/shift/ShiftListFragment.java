package com.skepticalone.mecachecker.shift;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.PeriodWithStableId;
import com.skepticalone.mecachecker.R;

public class ShiftListFragment extends Fragment {

    private RecyclerView.LayoutManager mLayoutManager;
    private CustomAdapter mAdapter;
    private Listener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (Listener) context;
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
        mLayoutManager.scrollToPosition(mListener.getShiftCount() - 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.shift_list_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.shift_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapter();
        recyclerView.setAdapter(mAdapter);
        layout.findViewById(R.id.add_shift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddShiftClicked();
            }
        });
        return layout;
    }

    interface Listener {
        void onAddShiftClicked();

        void onShiftClicked(PeriodWithStableId shift);

        void onShiftLongClicked(PeriodWithStableId shift);

        int getShiftCount();

        PeriodWithStableId getShift(int position);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        CustomAdapter() {
            super();
            setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            return mListener.getShift(position).getId();
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shift_list_content, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            final PeriodWithStableId shift = mListener.getShift(position);
            holder.dateView.setText(holder.dateView.getContext().getString(R.string.date_format, shift.getStart()));
            holder.startView.setText(holder.startView.getContext().getString(R.string.time_format, shift.getStart()));
            holder.endView.setText(holder.endView.getContext().getString(shift.isSameDay() ? R.string.time_format : R.string.time_format_with_day, shift.getEnd()));
            holder.minimumRestHoursBetweenShiftsView.setVisibility(
                    shift.isCompliantWithMinimumRestHoursBetweenShifts() ?
                            View.GONE :
                            View.VISIBLE
            );
//            holder.minimumRestHoursBetweenShiftsView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMinimumRestHoursBetweenShifts(shift.nonCompliantPeriodWithMinimumRestHoursBetweenShifts());
//                }
//            });
            holder.maximumHoursPerDayView.setVisibility(
                    shift.isCompliantWithMaximumHoursPerDay() ?
                            View.GONE :
                            View.VISIBLE
            );
//            holder.maximumHoursPerDayView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMaximumHoursPerDay(shift.nonCompliantPeriodWithMaximumHoursPerDay());
//                }
//            });
            holder.maximumHoursPerWeekView.setVisibility(
                    shift.isCompliantWithMaximumHoursPerWeek() ?
                            View.GONE :
                            View.VISIBLE
            );
//            holder.maximumHoursPerWeekView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMaximumHoursPerWeek(shift.nonCompliantPeriodWithMaximumHoursPerWeek());
//                }
//            });
            holder.maximumHoursPerFortnightView.setVisibility(
                    shift.isCompliantWithMaximumHoursPerFortnight() ?
                            View.GONE :
                            View.VISIBLE
            );
//            holder.maximumHoursPerFortnightView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMaximumHoursPerFortnight(shift.nonCompliantPeriodWithMaximumHoursPerFortnight());
//                }
//            });
            holder.maximumConsecutiveWeekendsView.setVisibility(
                    shift.isCompliantWithMaximumConsecutiveWeekends() ?
                            View.GONE :
                            View.VISIBLE
            );
//            holder.maximumConsecutiveWeekendsView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.showDialogNonCompliantWithMaximumConsecutiveWeekends();
//                }
//            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onShiftClicked(shift);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onShiftLongClicked(shift);
                    return true;
                }
            });

        }

        @Override
        public int getItemCount() {
            return mListener.getShiftCount();
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
