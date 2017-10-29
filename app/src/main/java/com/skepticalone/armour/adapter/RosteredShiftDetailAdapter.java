package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.skepticalone.armour.data.compliance.Row;
import com.skepticalone.armour.data.compliance.RowDurationBetweenShifts;
import com.skepticalone.armour.data.compliance.RowDurationOverDay;
import com.skepticalone.armour.data.compliance.RowDurationOverFortnight;
import com.skepticalone.armour.data.compliance.RowDurationOverWeek;
import com.skepticalone.armour.data.model.CommentBinder;
import com.skepticalone.armour.data.model.DateBinder;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.data.model.ShiftDataBinder;
import com.skepticalone.armour.data.model.ShiftTypeBinder;
import com.skepticalone.armour.data.model.ToggleLoggedBinder;

import java.util.ArrayList;
import java.util.List;

public final class RosteredShiftDetailAdapter extends ObservableAdapter<RosteredShift> {

    //    private static final int
//            ROW_NUMBER_DATE = 0,
//            ROW_NUMBER_START = 1,
//            ROW_NUMBER_END = 2,
//            ROW_NUMBER_SHIFT_TYPE = 3,
//            ROW_NUMBER_COMMENT = 4,
//            FIXED_ROWS_COUNT = 5,
//            NUMBER_OF_ROWS_FOR_LOGGED = 2;
//
//    enum RowType {
//            LOGGED_START,
//            LOGGED_END,
//            COMMENT,
//            DURATION_BETWEEN_SHIFTS,
//            DURATION_WORKED_OVER_DAY,
//            DURATION_WORKED_OVER_WEEK,
//            DURATION_WORKED_OVER_FORTNIGHT,
//            CONSECUTIVE_DAYS,
//            LONG_DAY,
//            NIGHT,
//            RECOVERY_FOLLOWING_NIGHTS,
//            WEEKEND,
//            ROSTERED_DAY_OFF,
//    }
//
//    private final List<RowType> rowTypes = new ArrayList<>(RowType.values().length);
//    @NonNull
//    private final ShiftDetailAdapterHelper shiftDetailAdapterHelper;
    @NonNull
    private final Callbacks callbacks;
    @Nullable
    private List<ItemViewHolder.Binder> mList;

    public RosteredShiftDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            private static final String TAG = "AdapterDataObserver";
            @Override
            public void onChanged() {
                super.onChanged();
                Log.d(TAG, "onChanged() called");
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                Log.d(TAG, "onItemRangeChanged() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                Log.d(TAG, "onItemRangeChanged() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "], payload = [" + payload + "]");
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                Log.d(TAG, "onItemRangeInserted() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Log.d(TAG, "onItemRangeRemoved() called with: positionStart = [" + positionStart + "], itemCount = [" + itemCount + "]");
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                Log.d(TAG, "onItemRangeMoved() called with: fromPosition = [" + fromPosition + "], toPosition = [" + toPosition + "], itemCount = [" + itemCount + "]");
            }
        });
    }
//        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper(callbacks) {
//
//            @Override
//            int getRowNumberDate() {
//                return ROW_NUMBER_DATE;
//            }
//
//            @Override
//            int getRowNumberStart() {
//                return ROW_NUMBER_START;
//            }
//
//            @Override
//            int getRowNumberEnd() {
//                return ROW_NUMBER_END;
//            }
//
//            @Override
//            int getRowNumberShiftType() {
//                return ROW_NUMBER_SHIFT_TYPE;
//            }
//
//            @Override
//            void changeTime(boolean start) {
//                RosteredShiftDetailAdapter.this.callbacks.changeTime(start, false);
//            }
//
//        };
//        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                if (ROW_NUMBER_LOGGED_START >= positionStart) {
//                    ROW_NUMBER_LOGGED_START += itemCount;
//                }
//                if (ROW_NUMBER_LOGGED_END >= positionStart) {
//                    ROW_NUMBER_LOGGED_END += itemCount;
//                }
//                if (ROW_NUMBER_COMMENT >= positionStart) {
//                    ROW_NUMBER_COMMENT += itemCount;
//                }
//                if (ROW_NUMBER_DURATION_BETWEEN_SHIFTS >= positionStart) {
//                    ROW_NUMBER_DURATION_BETWEEN_SHIFTS += itemCount;
//                }
//                if (ROW_NUMBER_DURATION_WORKED_OVER_DAY >= positionStart) {
//                    ROW_NUMBER_DURATION_WORKED_OVER_DAY += itemCount;
//                }
//                if (ROW_NUMBER_DURATION_WORKED_OVER_WEEK >= positionStart) {
//                    ROW_NUMBER_DURATION_WORKED_OVER_WEEK += itemCount;
//                }
//                if (ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT >= positionStart) {
//                    ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT += itemCount;
//                }
//                if (ROW_NUMBER_CONSECUTIVE_DAYS >= positionStart) {
//                    ROW_NUMBER_CONSECUTIVE_DAYS += itemCount;
//                }
//                if (ROW_NUMBER_LONG_DAY >= positionStart) {
//                    ROW_NUMBER_LONG_DAY += itemCount;
//                }
//                if (ROW_NUMBER_NIGHT >= positionStart) {
//                    ROW_NUMBER_NIGHT += itemCount;
//                }
//                if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS >= positionStart) {
//                    ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS += itemCount;
//                }
//                if (ROW_NUMBER_WEEKEND >= positionStart) {
//                    ROW_NUMBER_WEEKEND += itemCount;
//                }
//                if (ROW_NUMBER_ROSTERED_DAY_OFF >= positionStart) {
//                    ROW_NUMBER_ROSTERED_DAY_OFF += itemCount;
//                }
//                rowCount += itemCount;
//            }
//
//            @Override
//            public void onItemRangeRemoved(int positionStart, int itemCount) {
//                onItemRangeInserted(positionStart, -itemCount);
//            }
//        });
//    }
//
//    private void addRows(int startPositionInclusive, int count) {
//        if (ROW_NUMBER_LOGGED_START >= startPositionInclusive) {
//            ROW_NUMBER_LOGGED_START += count;
//        }
//        if (ROW_NUMBER_LOGGED_END >= startPositionInclusive) {
//            ROW_NUMBER_LOGGED_END += count;
//        }
//        if (ROW_NUMBER_COMMENT >= startPositionInclusive) {
//            ROW_NUMBER_COMMENT += count;
//        }
//        if (ROW_NUMBER_DURATION_BETWEEN_SHIFTS >= startPositionInclusive) {
//            ROW_NUMBER_DURATION_BETWEEN_SHIFTS += count;
//        }
//        if (ROW_NUMBER_DURATION_WORKED_OVER_DAY >= startPositionInclusive) {
//            ROW_NUMBER_DURATION_WORKED_OVER_DAY += count;
//        }
//        if (ROW_NUMBER_DURATION_WORKED_OVER_WEEK >= startPositionInclusive) {
//            ROW_NUMBER_DURATION_WORKED_OVER_WEEK += count;
//        }
//        if (ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT >= startPositionInclusive) {
//            ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT += count;
//        }
//        if (ROW_NUMBER_CONSECUTIVE_DAYS >= startPositionInclusive) {
//            ROW_NUMBER_CONSECUTIVE_DAYS += count;
//        }
//        if (ROW_NUMBER_LONG_DAY >= startPositionInclusive) {
//            ROW_NUMBER_LONG_DAY += count;
//        }
//        if (ROW_NUMBER_NIGHT >= startPositionInclusive) {
//            ROW_NUMBER_NIGHT += count;
//        }
//        if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS >= startPositionInclusive) {
//            ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS += count;
//        }
//        if (ROW_NUMBER_WEEKEND >= startPositionInclusive) {
//            ROW_NUMBER_WEEKEND += count;
//        }
//        if (ROW_NUMBER_ROSTERED_DAY_OFF >= startPositionInclusive) {
//            ROW_NUMBER_ROSTERED_DAY_OFF += count;
//        }
//        rowCount += count;
//    }

    private static int getItemCount(@Nullable List<ItemViewHolder.Binder> list) {
        return list == null ? 0 : list.size();
    }

    @Override
    public final void onChanged(@Nullable RosteredShift rosteredShift) {
        final List<ItemViewHolder.Binder> newList = rosteredShift == null ? null : getNewList(rosteredShift);
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return getItemCount(mList);
            }

            @Override
            public int getNewListSize() {
                return getItemCount(newList);
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions
                return mList.get(oldItemPosition).areItemsTheSame(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions
                return mList.get(oldItemPosition).areContentsTheSame(newList.get(newItemPosition));
            }

        }, false).dispatchUpdatesTo(this);
        mList = newList;
    }

    @Override
    public int getItemCount() {
        return getItemCount(mList);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        mList.get(position).onBindViewHolder(holder);
    }

    @NonNull
    private List<ItemViewHolder.Binder> getNewList(@NonNull RosteredShift rosteredShift) {
        List<ItemViewHolder.Binder> list = new ArrayList<>();
        list.add(new DateBinder(callbacks, rosteredShift.getShiftData().getStart().toLocalDate()));
        list.add(new ShiftDataBinder(callbacks, rosteredShift.getShiftData(), true, false));
        list.add(new ShiftDataBinder(callbacks, rosteredShift.getShiftData(), false, false));
        list.add(new ShiftTypeBinder(rosteredShift.getShiftType(), rosteredShift.getShiftData().getDuration()));
        list.add(new ToggleLoggedBinder(callbacks, rosteredShift.getLoggedShiftData()));
        if (rosteredShift.getLoggedShiftData() != null) {
            list.add(new ShiftDataBinder(callbacks, rosteredShift.getLoggedShiftData(), true, true));
            list.add(new ShiftDataBinder(callbacks, rosteredShift.getLoggedShiftData(), false, true));
        }
        list.add(new CommentBinder(callbacks, rosteredShift.getComment()));
        if (rosteredShift.getCompliance().getDurationBetweenShifts() != null) {
            list.add(new RowDurationBetweenShifts.Binder(callbacks, rosteredShift.getCompliance().getDurationBetweenShifts()));
        }
        list.add(new RowDurationOverDay.Binder(callbacks, rosteredShift.getCompliance().getDurationOverDay()));
        list.add(new RowDurationOverWeek.Binder(callbacks, rosteredShift.getCompliance().getDurationOverWeek()));
        list.add(new RowDurationOverFortnight.Binder(callbacks, rosteredShift.getCompliance().getDurationOverFortnight()));
//        if (rosteredShift.getCompliance().getConsecutiveDays() != null) {
//            list.add(rosteredShift.getCompliance().getConsecutiveDays());
//        }
//        if (rosteredShift.getCompliance().getLongDay() != null) {
//            list.add(rosteredShift.getCompliance().getLongDay());
//        }
//        if (rosteredShift.getCompliance().getNight() != null) {
//            list.add(rosteredShift.getCompliance().getNight());
//        }
//        if (rosteredShift.getCompliance().getConsecutiveDays() != null) {
//            list.add(rosteredShift.getCompliance().getConsecutiveDays());
//        }
//        if (rosteredShift.getCompliance().getLongDay() != null) {
//            list.add(rosteredShift.getCompliance().getLongDay());
//        }
//        if (rosteredShift.getCompliance().getNight() != null) {
//            list.add(rosteredShift.getCompliance().getNight());
//        }
//        if (rosteredShift.getCompliance().getRecoveryFollowingNights() != null) {
//            list.add(rosteredShift.getCompliance().getRecoveryFollowingNights());
//        }
//        if (rosteredShift.getCompliance().getWeekend() != null) {
//            list.add(rosteredShift.getCompliance().getWeekend());
//        }
//        if (rosteredShift.getCompliance() instanceof ComplianceSaferRosters) {
//            ComplianceSaferRosters compliance = (ComplianceSaferRosters) rosteredShift.getCompliance();
//            if (compliance.getRosteredDayOff() != null) {
//                list.add(compliance.getRosteredDayOff());
//            }
//        }
        return list;
    }

//    @Override
//    void onChanged(@Nullable RosteredShift oldRosteredShift, @Nullable RosteredShift rosteredShift) {
//
//        if (oldRosteredShift == null && rosteredShift != null) {
//            mList.clear();
//            mList.add(rosteredShift.getToggleLoggedBinder());
//            if (rosteredShift.getLoggedShiftData() != null) {
//                mList.add(rosteredShift.getLoggedShiftData().getStartBinder());
//                mList.add(rosteredShift.getLoggedShiftData().getEndBinder());
//            }
//            if (rosteredShift.getCompliance().getDurationBetweenShifts() != null) {
//                mList.add(rosteredShift.getCompliance().getDurationBetweenShifts());
//            }
//            mList.add(rosteredShift.getCompliance().getDurationOverDay());
//            mList.add(rosteredShift.getCompliance().getDurationOverWeek());
//            mList.add(rosteredShift.getCompliance().getDurationOverFortnight());
//            if (rosteredShift.getCompliance().getConsecutiveDays() != null) {
//                mList.add(rosteredShift.getCompliance().getConsecutiveDays());
//            }
//            if (rosteredShift.getCompliance().getLongDay() != null) {
//                mList.add(rosteredShift.getCompliance().getLongDay());
//            }
//            if (rosteredShift.getCompliance().getNight() != null) {
//                mList.add(rosteredShift.getCompliance().getNight());
//            }
//            if (rosteredShift.getCompliance().getConsecutiveDays() != null) {
//                mList.add(rosteredShift.getCompliance().getConsecutiveDays());
//            }
//            if (rosteredShift.getCompliance().getLongDay() != null) {
//                mList.add(rosteredShift.getCompliance().getLongDay());
//            }
//            if (rosteredShift.getCompliance().getNight() != null) {
//                mList.add(rosteredShift.getCompliance().getNight());
//            }
//            if (rosteredShift.getCompliance().getRecoveryFollowingNights() != null) {
//                mList.add(rosteredShift.getCompliance().getRecoveryFollowingNights());
//            }
//            if (rosteredShift.getCompliance().getWeekend() != null) {
//                mList.add(rosteredShift.getCompliance().getWeekend());
//            }
//            if (rosteredShift.getCompliance() instanceof ComplianceSaferRosters) {
//                ComplianceSaferRosters compliance = (ComplianceSaferRosters) rosteredShift.getCompliance();
//                if (compliance.getRosteredDayOff() != null) {
//                    mList.add(compliance.getRosteredDayOff());
//                }
//            }
//        }
//        super.onChanged(oldRosteredShift, rosteredShift);
//    }
//
////    @Override
////    int getRowNumberComment() {
////        return ROW_NUMBER_COMMENT;
////    }
////
////    @Override
////    int getFixedRowCount() {
////        return FIXED_ROWS_COUNT + mList.size();
////    }
//
//    @Override
//    void notifyUpdated(@NonNull RosteredShift oldShift, @NonNull RosteredShift newShift) {
//        super.notifyUpdated(oldShift, newShift);
//        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
//        oldShift.getToggleLoggedBinder().onUpdated(newShift, mList, this);
//        if (oldShift.getLoggedShiftData() == null && newShift.getLoggedShiftData() != null) {
//            newShift.getLoggedShiftData().getStartBinder().onInserted(mList, this);
//            newShift.getLoggedShiftData().getEndBinder().onInserted(mList, this);
//        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() == null) {
//            oldShift.getLoggedShiftData().getStartBinder().onDeleted(mList, this);
//            oldShift.getLoggedShiftData().getEndBinder().onDeleted(mList, this);
//        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() != null) {
//            oldShift.getLoggedShiftData().getStartBinder().onUpdated(newShift, mList, this);
//            oldShift.getLoggedShiftData().getEndBinder().onUpdated(newShift, mList, this);
//
//            if (!oldShift.getLoggedShiftData().getDuration().equals(newShift.getLoggedShiftData().getDuration())) {
//                notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
//            }
//            if (!oldShift.getLoggedShiftData().getStart().toLocalTime().equals(newShift.getLoggedShiftData().getStart().toLocalTime())) {
//                notifyItemChanged(ROW_NUMBER_LOGGED_START);
//            }
//            if (!oldShift.getLoggedShiftData().getEnd().toLocalTime().equals(newShift.getLoggedShiftData().getEnd().toLocalTime()) || (oldShift.getShiftData().getStart().toLocalDate().isEqual(oldShift.getLoggedShiftData().getEnd().toLocalDate()) ? !newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getLoggedShiftData().getEnd().toLocalDate()) : (newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getLoggedShiftData().getEnd().toLocalDate()) || !oldShift.getLoggedShiftData().getEnd().getDayOfWeek().equals(newShift.getLoggedShiftData().getEnd().getDayOfWeek())))) {
//                notifyItemChanged(ROW_NUMBER_LOGGED_END);
//            }
//        }
//        if (oldShift.getCompliance().getDurationBetweenShifts() == null && newShift.getCompliance().getDurationBetweenShifts() != null) {
//            int lastPosition = ROW_NUMBER_COMMENT;
//            ROW_NUMBER_DURATION_BETWEEN_SHIFTS = ++lastPosition;
//            ROW_NUMBER_DURATION_WORKED_OVER_DAY = ++lastPosition;
//            ROW_NUMBER_DURATION_WORKED_OVER_WEEK = ++lastPosition;
//            ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT = ++lastPosition;
//            if (ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_CONSECUTIVE_DAYS = ++lastPosition;
//            if (ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION)
//                ROW_NUMBER_LONG_DAY = ++lastPosition;
//            if (ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION)
//                ROW_NUMBER_NIGHT = ++lastPosition;
//            if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = ++lastPosition;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//            notifyItemInserted(ROW_NUMBER_DURATION_BETWEEN_SHIFTS);
//        } else if (oldShift.getCompliance().getDurationBetweenShifts() != null && newShift.getCompliance().getDurationBetweenShifts() == null) {
//            notifyItemRemoved(ROW_NUMBER_DURATION_BETWEEN_SHIFTS);
//            ROW_NUMBER_DURATION_BETWEEN_SHIFTS = RecyclerView.NO_POSITION;
//            int lastPosition = ROW_NUMBER_COMMENT;
//            ROW_NUMBER_DURATION_WORKED_OVER_DAY = ++lastPosition;
//            ROW_NUMBER_DURATION_WORKED_OVER_WEEK = ++lastPosition;
//            ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT = ++lastPosition;
//            if (ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_CONSECUTIVE_DAYS = ++lastPosition;
//            if (ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION)
//                ROW_NUMBER_LONG_DAY = ++lastPosition;
//            if (ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION)
//                ROW_NUMBER_NIGHT = ++lastPosition;
//            if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = ++lastPosition;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//        } else if (oldShift.getCompliance().getDurationBetweenShifts() != null && newShift.getCompliance().getDurationBetweenShifts() != null && !oldShift.getCompliance().getDurationBetweenShifts().isEqual(newShift.getCompliance().getDurationBetweenShifts())) {
//            notifyItemChanged(ROW_NUMBER_DURATION_BETWEEN_SHIFTS);
//        }
//        if (!oldShift.getCompliance().getDurationOverDay().isEqual(newShift.getCompliance().getDurationOverDay())) {
//            notifyItemChanged(ROW_NUMBER_DURATION_WORKED_OVER_DAY);
//        }
//        if (!oldShift.getCompliance().getDurationOverWeek().isEqual(newShift.getCompliance().getDurationOverWeek())) {
//            notifyItemChanged(ROW_NUMBER_DURATION_WORKED_OVER_WEEK);
//        }
//        if (!oldShift.getCompliance().getDurationOverFortnight().isEqual(newShift.getCompliance().getDurationOverFortnight())) {
//            notifyItemChanged(ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT);
//        }
//        if (oldShift.getCompliance().getConsecutiveDays() == null && newShift.getCompliance().getConsecutiveDays() != null) {
//            int lastPosition = ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            ROW_NUMBER_CONSECUTIVE_DAYS = ++lastPosition;
//            if (ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION)
//                ROW_NUMBER_LONG_DAY = ++lastPosition;
//            if (ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION)
//                ROW_NUMBER_NIGHT = ++lastPosition;
//            if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = ++lastPosition;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//            notifyItemInserted(ROW_NUMBER_CONSECUTIVE_DAYS);
//        } else if (oldShift.getCompliance().getConsecutiveDays() != null && newShift.getCompliance().getConsecutiveDays() == null) {
//            notifyItemRemoved(ROW_NUMBER_CONSECUTIVE_DAYS);
//            ROW_NUMBER_CONSECUTIVE_DAYS = RecyclerView.NO_POSITION;
//            int lastPosition = ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            if (ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION)
//                ROW_NUMBER_LONG_DAY = ++lastPosition;
//            if (ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION)
//                ROW_NUMBER_NIGHT = ++lastPosition;
//            if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = ++lastPosition;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//        } else if (oldShift.getCompliance().getConsecutiveDays() != null && newShift.getCompliance().getConsecutiveDays() != null && !oldShift.getCompliance().getConsecutiveDays().isEqual(newShift.getCompliance().getConsecutiveDays())) {
//            notifyItemChanged(ROW_NUMBER_CONSECUTIVE_DAYS);
//        }
//        if (oldShift.getCompliance().getLongDay() == null && newShift.getCompliance().getLongDay() != null) {
//            int lastPosition = ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            ROW_NUMBER_LONG_DAY = ++lastPosition;
//            if (ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION)
//                ROW_NUMBER_NIGHT = ++lastPosition;
//            if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = ++lastPosition;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//            notifyItemInserted(ROW_NUMBER_LONG_DAY);
//        } else if (oldShift.getCompliance().getLongDay() != null && newShift.getCompliance().getLongDay() == null) {
//            notifyItemRemoved(ROW_NUMBER_LONG_DAY);
//            ROW_NUMBER_LONG_DAY = RecyclerView.NO_POSITION;
//            int lastPosition = ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            if (ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION)
//                ROW_NUMBER_NIGHT = ++lastPosition;
//            if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = ++lastPosition;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//        } else if (oldShift.getCompliance().getLongDay() != null && newShift.getCompliance().getLongDay() != null && !oldShift.getCompliance().getLongDay().isEqual(newShift.getCompliance().getLongDay())) {
//            notifyItemChanged(ROW_NUMBER_LONG_DAY);
//        }
//        if (oldShift.getCompliance().getNight() == null && newShift.getCompliance().getNight() != null) {
//            int lastPosition = ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION ? ROW_NUMBER_LONG_DAY : ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            ROW_NUMBER_NIGHT = ++lastPosition;
//            if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = ++lastPosition;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//            notifyItemInserted(ROW_NUMBER_NIGHT);
//        } else if (oldShift.getCompliance().getNight() != null && newShift.getCompliance().getNight() == null) {
//            notifyItemRemoved(ROW_NUMBER_NIGHT);
//            ROW_NUMBER_NIGHT = RecyclerView.NO_POSITION;
//            int lastPosition = ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION ? ROW_NUMBER_LONG_DAY : ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            if (ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION)
//                ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = ++lastPosition;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//        } else if (oldShift.getCompliance().getNight() != null && newShift.getCompliance().getNight() != null && !oldShift.getCompliance().getNight().isEqual(newShift.getCompliance().getNight())) {
//            notifyItemChanged(ROW_NUMBER_NIGHT);
//        }
//        if (oldShift.getCompliance().getRecoveryFollowingNights() == null && newShift.getCompliance().getRecoveryFollowingNights() != null) {
//            int lastPosition = ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION ? ROW_NUMBER_NIGHT : ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION ? ROW_NUMBER_LONG_DAY : ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = ++lastPosition;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//            notifyItemInserted(ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS);
//        } else if (oldShift.getCompliance().getRecoveryFollowingNights() != null && newShift.getCompliance().getRecoveryFollowingNights() == null) {
//            notifyItemRemoved(ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS);
//            ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS = RecyclerView.NO_POSITION;
//            int lastPosition = ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION ? ROW_NUMBER_NIGHT : ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION ? ROW_NUMBER_LONG_DAY : ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            if (ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION)
//                ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//        } else if (oldShift.getCompliance().getRecoveryFollowingNights() != null && newShift.getCompliance().getRecoveryFollowingNights() != null && !oldShift.getCompliance().getRecoveryFollowingNights().isEqual(newShift.getCompliance().getRecoveryFollowingNights())) {
//            notifyItemChanged(ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS);
//        }
//        if (oldShift.getCompliance().getWeekend() == null && newShift.getCompliance().getWeekend() != null) {
//            int lastPosition = ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION ? ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS : ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION ? ROW_NUMBER_NIGHT : ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION ? ROW_NUMBER_LONG_DAY : ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            ROW_NUMBER_WEEKEND = ++lastPosition;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//            notifyItemInserted(ROW_NUMBER_WEEKEND);
//        } else if (oldShift.getCompliance().getWeekend() != null && newShift.getCompliance().getWeekend() == null) {
//            notifyItemRemoved(ROW_NUMBER_WEEKEND);
//            ROW_NUMBER_WEEKEND = RecyclerView.NO_POSITION;
//            int lastPosition = ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION ? ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS : ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION ? ROW_NUMBER_NIGHT : ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION ? ROW_NUMBER_LONG_DAY : ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            if (ROW_NUMBER_ROSTERED_DAY_OFF != RecyclerView.NO_POSITION)
//                ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//        } else if (oldShift.getCompliance().getWeekend() != null && newShift.getCompliance().getWeekend() != null && !oldShift.getCompliance().getWeekend().isEqual(newShift.getCompliance().getWeekend())) {
//            notifyItemChanged(ROW_NUMBER_WEEKEND);
//        }
//        if (!(oldShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) oldShift.getCompliance()).getRosteredDayOff() != null) && newShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) newShift.getCompliance()).getRosteredDayOff() != null) {
//            int lastPosition = ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION ? ROW_NUMBER_WEEKEND : ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION ? ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS : ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION ? ROW_NUMBER_NIGHT : ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION ? ROW_NUMBER_LONG_DAY : ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            ROW_NUMBER_ROSTERED_DAY_OFF = ++lastPosition;
//            rowCount = ++lastPosition;
//            notifyItemInserted(ROW_NUMBER_ROSTERED_DAY_OFF);
//        } else if (oldShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) oldShift.getCompliance()).getRosteredDayOff() != null && !(newShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) newShift.getCompliance()).getRosteredDayOff() != null)) {
//            notifyItemRemoved(ROW_NUMBER_ROSTERED_DAY_OFF);
//            ROW_NUMBER_ROSTERED_DAY_OFF = RecyclerView.NO_POSITION;
//            int lastPosition = ROW_NUMBER_WEEKEND != RecyclerView.NO_POSITION ? ROW_NUMBER_WEEKEND : ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS != RecyclerView.NO_POSITION ? ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS : ROW_NUMBER_NIGHT != RecyclerView.NO_POSITION ? ROW_NUMBER_NIGHT : ROW_NUMBER_LONG_DAY != RecyclerView.NO_POSITION ? ROW_NUMBER_LONG_DAY : ROW_NUMBER_CONSECUTIVE_DAYS != RecyclerView.NO_POSITION ? ROW_NUMBER_CONSECUTIVE_DAYS : ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT;
//            rowCount = ++lastPosition;
//        } else if (oldShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) oldShift.getCompliance()).getRosteredDayOff() != null && newShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) newShift.getCompliance()).getRosteredDayOff() != null && !((ComplianceSaferRosters) oldShift.getCompliance()).getRosteredDayOff().isEqual(((ComplianceSaferRosters) newShift.getCompliance()).getRosteredDayOff())) {
//            notifyItemChanged(ROW_NUMBER_ROSTERED_DAY_OFF);
//        }
//    }
//
//    @Override
//    void onBindViewHolder(@NonNull RosteredShift shift, int position, @NonNull ItemViewHolder holder) {
//        if (position == ROW_NUMBER_TOGGLE_LOGGED) {
//        } else if (position == ROW_NUMBER_LOGGED_START) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_clipboard_play_black_24dp);
//            holder.hideSecondaryIcon();
//            //noinspection ConstantConditions
//            holder.setText(getContext().getString(R.string.logged_start), DateTimeUtils.getTimeString(shift.getLoggedShiftData().getStart().toLocalTime()));
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callbacks.changeTime(true, true);
//                }
//            });
//        } else if (position == ROW_NUMBER_LOGGED_END) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_clipboard_stop_black_24dp);
//            holder.hideSecondaryIcon();
//            //noinspection ConstantConditions
//            holder.setText(getContext().getString(R.string.logged_end), DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getEnd().toLocalDateTime(), shift.getShiftData().getStart().toLocalDate()));
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callbacks.changeTime(false, true);
//                }
//            });
//        } else if (position == ROW_NUMBER_DURATION_BETWEEN_SHIFTS) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_sleep_black_24dp);
//            //noinspection ConstantConditions
//            holder.setCompliant(shift.getCompliance().getDurationBetweenShifts());
//            holder.setText(getContext().getString(R.string.time_between_shifts), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationBetweenShifts().getDuration()));
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callbacks.showMessage(getContext().getString(R.string.meca_minimum_hours_between_shifts, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS));
//                }
//            });
//        } else if (position == ROW_NUMBER_DURATION_WORKED_OVER_DAY) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_duration_black_24dp);
//            holder.setCompliant(shift.getCompliance().getDurationOverDay());
//            holder.setText(getContext().getString(R.string.duration_worked_over_day), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverDay().getDuration()));
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_day, AppConstants.MAXIMUM_HOURS_OVER_DAY));
//                }
//            });
//        } else if (position == ROW_NUMBER_DURATION_WORKED_OVER_WEEK) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_week_black_24dp);
//            holder.setCompliant(shift.getCompliance().getDurationOverWeek());
//            holder.setText(getContext().getString(R.string.duration_worked_over_week), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverWeek().getDuration()));
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_week, AppConstants.MAXIMUM_HOURS_OVER_WEEK));
//                }
//            });
//        } else if (position == ROW_NUMBER_DURATION_WORKED_OVER_FORTNIGHT) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_weeks_black_24dp);
//            holder.setCompliant(shift.getCompliance().getDurationOverFortnight());
//            holder.setText(getContext().getString(R.string.duration_worked_over_fortnight), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverFortnight().getDuration()));
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_fortnight, AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT));
//                }
//            });
//        } else if (position == ROW_NUMBER_CONSECUTIVE_DAYS) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_consecutive_shifts_black_24dp);
//            //noinspection ConstantConditions
//            holder.setCompliant(shift.getCompliance().getConsecutiveDays());
//            int days = shift.getCompliance().getConsecutiveDays().getIndexOfDayShift() + 1;
//            holder.setText(getContext().getString(R.string.number_of_consecutive_days_worked), getContext().getResources().getQuantityString(R.plurals.days, days, days));
//            final int message = shift.getCompliance().getConsecutiveDays().getMessage(), maximumConsecutiveDays = shift.getCompliance().getConsecutiveDays().getMaximumConsecutiveDays();
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    callbacks.showMessage(getContext().getString(message, maximumConsecutiveDays));
//                }
//            });
//        } else if (position == ROW_NUMBER_LONG_DAY) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_long_day_black_24dp);
//            //noinspection ConstantConditions
//            holder.setCompliant(shift.getCompliance().getLongDay());
//            int longDays = shift.getCompliance().getLongDay().getIndexOfLongDay() + 1;
//            holder.setText(getContext().getString(R.string.number_of_long_days_in_week), getContext().getResources().getQuantityString(R.plurals.long_days, longDays, longDays));
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_long_days_over_week, AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK, AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY));
//                }
//            });
//        } else if (position == ROW_NUMBER_NIGHT) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_consecutive_shifts_black_24dp);
//            //noinspection ConstantConditions
//            holder.setCompliant(shift.getCompliance().getNight());
//            int nights = shift.getCompliance().getNight().getIndexOfNightShift() + 1;
//            holder.setText(getContext().getString(R.string.number_of_consecutive_nights_worked), getContext().getResources().getQuantityString(R.plurals.nights, nights, nights));
//
//            final int maximumConsecutiveNights = shift.getCompliance().getNight().getMaximumConsecutiveNights();
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_consecutive_nights, maximumConsecutiveNights));
//                }
//            });
//        } else if (position == ROW_NUMBER_RECOVERY_FOLLOWING_NIGHTS) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_recovery_days_following_nights);
//            //noinspection ConstantConditions
//            holder.setCompliant(shift.getCompliance().getRecoveryFollowingNights());
//            int recoveryDays = shift.getCompliance().getRecoveryFollowingNights().getRecoveryDays(), consecutiveNights = shift.getCompliance().getRecoveryFollowingNights().getConsecutiveNights();
//            holder.setText(getContext().getString(R.string.recovery_days_following_nights), getContext().getString(R.string.n_days_following_n_nights, getContext().getResources().getQuantityString(R.plurals.days, recoveryDays, recoveryDays), getContext().getResources().getQuantityString(R.plurals.nights, consecutiveNights, consecutiveNights)));
//            final String message;
//            if (shift.getCompliance().getRecoveryFollowingNights() instanceof RowRecoveryFollowingNightsSaferRosters) {
//                if (consecutiveNights < 2) {
//                    message = getContext().getString(R.string.recovery_days_only_required_following_n_nights, getContext().getResources().getQuantityString(R.plurals.nights, 2, 2));
//                } else if (consecutiveNights == 2) {
//                    message = getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 2, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS);
//                } else if (consecutiveNights == 3) {
//                    if (shift.getCompliance().getRecoveryFollowingNights().isLenient()) {
//                        message = getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT);
//                    } else {
//                        message = getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT);
//                    }
//                } else {
//                    message = getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 4, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS);
//                }
//            } else {
//                message = getContext().getString(R.string.meca_minimum_recovery_after_consecutive_nights, AppConstants.MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS, AppConstants.MINIMUM_NIGHTS_BEFORE_RECOVERY);
//            }
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    callbacks.showMessage(message);
//                }
//            });
//        } else if (position == ROW_NUMBER_WEEKEND) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_weekend_black_24dp);
//            //noinspection ConstantConditions
//            holder.setCompliant(shift.getCompliance().getWeekend());
//            LocalDate currentWeekend = shift.getCompliance().getWeekend().getCurrentWeekend();
//            @StringRes final int title;
//            final String secondLine, thirdLine, message;
//            if (shift.getCompliance().getWeekend() instanceof RowWeekendLegacy) {
//                LocalDate previousWeekend = ((RowWeekendLegacy) shift.getCompliance().getWeekend()).getPreviousWeekend();
//                if (previousWeekend == null) {
//                    secondLine = getContext().getString(R.string.not_applicable);
//                    thirdLine = null;
//                } else {
//                    secondLine = DateTimeUtils.getWeekendDateSpanString(previousWeekend);
//                    //noinspection ConstantConditions
//                    thirdLine = DateTimeUtils.getWeeksAgo(getContext(), currentWeekend, previousWeekend);
//                }
//                message = getContext().getString(R.string.meca_consecutive_weekends);
//            } else {
//                secondLine = shift.getCompliance().getWeekend().getNumerator() + " out of " + shift.getCompliance().getWeekend().getDenominator();
//                thirdLine = null;
//                message = getContext().getString(R.string.meca_safer_rosters_maximum_consecutive_weekends,
//                        2,
//                        shift.getCompliance().getWeekend().getDenominator(),
//                        shift.getCompliance().getWeekend().getDenominator() - 2
//                );
//            }
//            holder.setText(getContext().getString(R.string.last_weekend_worked), secondLine, thirdLine);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callbacks.showMessage(message);
//                }
//            });
//        } else if (position == ROW_NUMBER_ROSTERED_DAY_OFF) {
//            holder.setupPlain();
//            holder.setPrimaryIcon(R.drawable.ic_safer_rosters_black_24dp);
//            RowRosteredDayOff rosteredDayOff = ((ComplianceSaferRosters) shift.getCompliance()).getRosteredDayOff();
//            //noinspection ConstantConditions
//            holder.setCompliant(rosteredDayOff);
////            LocalDate currentWeekend = shift.getCompliance().getWeekend().getCurrentWeekend();
////            @StringRes final int title;
////            final String secondLine, thirdLine, message;
////            if (shift.getCompliance().getWeekend() instanceof RowWeekendLegacy) {
////                LocalDate previousWeekend = ((RowWeekendLegacy) shift.getCompliance().getWeekend()).getPreviousWeekend();
////                if (previousWeekend == null) {
////                    secondLine = getContext().getString(R.string.not_applicable);
////                    thirdLine = null;
////                } else {
////                    secondLine = DateTimeUtils.getWeekendDateSpanString(previousWeekend);
////                    //noinspection ConstantConditions
////                    thirdLine = DateTimeUtils.getWeeksAgo(getContext(), currentWeekend, previousWeekend);
////                }
////                message = getContext().getString(R.string.meca_consecutive_weekends);
////            } else {
////                secondLine = shift.getCompliance().getWeekend().getNumerator() + " out of " + shift.getCompliance().getWeekend().getDenominator();
////                thirdLine = null;
////                message = getContext().getString(R.string.meca_safer_rosters_maximum_consecutive_weekends,
////                        2,
////                        shift.getCompliance().getWeekend().getDenominator(),
////                        shift.getCompliance().getWeekend().getDenominator() - 2
////                );
////            }
//            holder.setText(getContext().getString(R.string.rostered_day_off), rosteredDayOff.getDate() == null ? "No RDO found" : DateTimeUtils.getFullDateString(rosteredDayOff.getDate()));
//            final boolean allowMidweekRDOs = rosteredDayOff.allowMidweekRDOs();
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    callbacks.showMessage(getContext().getString(allowMidweekRDOs ? R.string.meca_safer_rosters_rostered_day_off_lenient : R.string.meca_safer_rosters_rostered_day_off_strict));
//                }
//            });
//        } else {
//            holder.hideSecondaryIcon();
//            if (!shiftDetailAdapterHelper.bindViewHolder(shift, position, holder, this)) {
//                super.onBindViewHolder(shift, position, holder);
//            }
//        }
//    }

    public interface Callbacks extends DateBinder.Callbacks, ToggleLoggedBinder.Callbacks, ShiftDataBinder.Callbacks, CommentBinder.Callbacks, Row.Binder.Callbacks {
//        void showMessage(@NonNull String message);
    }

}
