package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.RosteredShift;
import com.skepticalone.mecachecker.data.util.ShiftData;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class RosteredShiftDetailAdapter extends ItemDetailAdapter<RosteredShift> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_SHIFT_TYPE = 3,
            ROW_NUMBER_LOGGED_START = 4,
            ROW_NUMBER_LOGGED_END = 5,
            ROW_NUMBER_COMMENT = 6,
            ROW_COUNT = 7;

    private final ShiftDetailAdapterHelper<RosteredShift> shiftDetailAdapterHelper;

    public RosteredShiftDetailAdapter(final Callbacks callbacks, ShiftUtil.Calculator calculator) {
        super(callbacks);
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper<RosteredShift>(calculator){
            @Override
            int getRowNumberDate() {
                return ROW_NUMBER_DATE;
            }

            @Override
            int getRowNumberStart() {
                return ROW_NUMBER_START;
            }

            @Override
            int getRowNumberEnd() {
                return ROW_NUMBER_END;
            }

            @Override
            int getRowNumberShiftType() {
                return ROW_NUMBER_SHIFT_TYPE;
            }

            @Override
            void changeDate(@NonNull RosteredShift shift) {
                callbacks.changeDate(shift.getId(), shift.getShiftData(), shift.getLoggedShiftData());
            }

            @Override
            void changeTime(@NonNull RosteredShift shift, boolean isStart) {
                callbacks.changeTime(shift.getId(), isStart, shift.getShiftData(), shift.getLoggedShiftData());
            }
        };
    }

    @Override
    int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getRowCount(@NonNull RosteredShift item) {
        return ROW_COUNT;
    }

    @Override
    void onItemUpdated(@NonNull RosteredShift oldShift, @NonNull RosteredShift newShift) {
        super.onItemUpdated(oldShift, newShift);
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
    }

    @Override
    boolean bindViewHolder(@NonNull RosteredShift shift, ItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_LOGGED_START) {
            holder.setupPlain(R.drawable.ic_clipboard_play_black_24dp, null);
            holder.setText(holder.getText(R.string.logged_start), shift.getLoggedShiftData() == null ? holder.getText(R.string.not_applicable) :  DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getStart(), shift.getShiftData().getStart().toLocalDate()));
            return true;
        } else if (position == ROW_NUMBER_LOGGED_END) {
            holder.setupPlain(R.drawable.ic_clipboard_stop_black_24dp, null);
            holder.setText(holder.getText(R.string.logged_end), shift.getLoggedShiftData() == null ? holder.getText(R.string.not_applicable) :  DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getEnd(), shift.getShiftData().getStart().toLocalDate()));
            return true;
        } else return shiftDetailAdapterHelper.bindViewHolder(shift, holder, position) ||
                super.bindViewHolder(shift, holder, position);
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks {
        void changeDate(long id, @NonNull ShiftData shiftData, @Nullable ShiftData loggedShiftData);
        void changeTime(long id, boolean isStart, @NonNull ShiftData shiftData, @Nullable ShiftData loggedShiftData);
    }

}
