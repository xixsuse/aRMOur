package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.compliance.ComplianceSaferRosters;
import com.skepticalone.armour.data.compliance.RowRecoveryFollowingNightsSaferRosters;
import com.skepticalone.armour.data.compliance.RowRosteredDayOff;
import com.skepticalone.armour.data.compliance.RowWeekendLegacy;
import com.skepticalone.armour.data.model.RosteredShift;
import com.skepticalone.armour.util.AppConstants;
import com.skepticalone.armour.util.DateTimeUtils;

import org.threeten.bp.LocalDate;

public final class RosteredShiftDetailAdapter extends ItemDetailAdapter<RosteredShift> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_SHIFT_TYPE = 3,
            ROW_NUMBER_TOGGLE_LOGGED = 4,
            NUMBER_OF_ROWS_FOR_LOGGED = 2;
    @NonNull
    private final ShiftDetailAdapterHelper<RosteredShift> shiftDetailAdapterHelper;
    @NonNull
    private final Callbacks callbacks;
    private int
            rowNumberLoggedStart,
            rowNumberLoggedEnd,
            rowNumberComment,
            rowNumberDurationBetweenShifts,
            rowNumberDurationWorkedOverDay,
            rowNumberDurationWorkedOverWeek,
            rowNumberDurationWorkedOverFortnight,
            rowNumberConsecutiveDays,
            rowNumberLongDay,
            rowNumberNight,
            rowNumberRecoveryFollowingNights,
            rowNumberWeekend,
            rowNumberRosteredDayOff,
            rowCount;

    public RosteredShiftDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context, callbacks);
        this.callbacks = callbacks;
        shiftDetailAdapterHelper = new ShiftDetailAdapterHelper<RosteredShift>(callbacks) {

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
            void changeTime(boolean start) {
                RosteredShiftDetailAdapter.this.callbacks.changeTime(start, false);
            }

        };
//        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                if (rowNumberLoggedStart >= positionStart) {
//                    rowNumberLoggedStart += itemCount;
//                }
//                if (rowNumberLoggedEnd >= positionStart) {
//                    rowNumberLoggedEnd += itemCount;
//                }
//                if (rowNumberComment >= positionStart) {
//                    rowNumberComment += itemCount;
//                }
//                if (rowNumberDurationBetweenShifts >= positionStart) {
//                    rowNumberDurationBetweenShifts += itemCount;
//                }
//                if (rowNumberDurationWorkedOverDay >= positionStart) {
//                    rowNumberDurationWorkedOverDay += itemCount;
//                }
//                if (rowNumberDurationWorkedOverWeek >= positionStart) {
//                    rowNumberDurationWorkedOverWeek += itemCount;
//                }
//                if (rowNumberDurationWorkedOverFortnight >= positionStart) {
//                    rowNumberDurationWorkedOverFortnight += itemCount;
//                }
//                if (rowNumberConsecutiveDays >= positionStart) {
//                    rowNumberConsecutiveDays += itemCount;
//                }
//                if (rowNumberLongDay >= positionStart) {
//                    rowNumberLongDay += itemCount;
//                }
//                if (rowNumberNight >= positionStart) {
//                    rowNumberNight += itemCount;
//                }
//                if (rowNumberRecoveryFollowingNights >= positionStart) {
//                    rowNumberRecoveryFollowingNights += itemCount;
//                }
//                if (rowNumberWeekend >= positionStart) {
//                    rowNumberWeekend += itemCount;
//                }
//                if (rowNumberRosteredDayOff >= positionStart) {
//                    rowNumberRosteredDayOff += itemCount;
//                }
//                rowCount += itemCount;
//            }
//
//            @Override
//            public void onItemRangeRemoved(int positionStart, int itemCount) {
//                onItemRangeInserted(positionStart, -itemCount);
//            }
//        });
    }

//    private void addRows(int startPositionInclusive, int count) {
//        if (rowNumberLoggedStart >= startPositionInclusive) {
//            rowNumberLoggedStart += count;
//        }
//        if (rowNumberLoggedEnd >= startPositionInclusive) {
//            rowNumberLoggedEnd += count;
//        }
//        if (rowNumberComment >= startPositionInclusive) {
//            rowNumberComment += count;
//        }
//        if (rowNumberDurationBetweenShifts >= startPositionInclusive) {
//            rowNumberDurationBetweenShifts += count;
//        }
//        if (rowNumberDurationWorkedOverDay >= startPositionInclusive) {
//            rowNumberDurationWorkedOverDay += count;
//        }
//        if (rowNumberDurationWorkedOverWeek >= startPositionInclusive) {
//            rowNumberDurationWorkedOverWeek += count;
//        }
//        if (rowNumberDurationWorkedOverFortnight >= startPositionInclusive) {
//            rowNumberDurationWorkedOverFortnight += count;
//        }
//        if (rowNumberConsecutiveDays >= startPositionInclusive) {
//            rowNumberConsecutiveDays += count;
//        }
//        if (rowNumberLongDay >= startPositionInclusive) {
//            rowNumberLongDay += count;
//        }
//        if (rowNumberNight >= startPositionInclusive) {
//            rowNumberNight += count;
//        }
//        if (rowNumberRecoveryFollowingNights >= startPositionInclusive) {
//            rowNumberRecoveryFollowingNights += count;
//        }
//        if (rowNumberWeekend >= startPositionInclusive) {
//            rowNumberWeekend += count;
//        }
//        if (rowNumberRosteredDayOff >= startPositionInclusive) {
//            rowNumberRosteredDayOff += count;
//        }
//        rowCount += count;
//    }

    @Override
    void onChanged(@Nullable RosteredShift oldRosteredShift, @Nullable RosteredShift rosteredShift) {
        if (oldRosteredShift == null && rosteredShift != null) {
            int lastPosition = ROW_NUMBER_TOGGLE_LOGGED;
            if (rosteredShift.getLoggedShiftData() == null) {
                rowNumberLoggedStart = rowNumberLoggedEnd = RecyclerView.NO_POSITION;
            } else {
                rowNumberLoggedStart = ++lastPosition;
                rowNumberLoggedEnd = ++lastPosition;
            }
            rowNumberComment = ++lastPosition;
            rowNumberDurationBetweenShifts = rosteredShift.getCompliance().getDurationBetweenShifts() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberDurationWorkedOverDay = ++lastPosition;
            rowNumberDurationWorkedOverWeek = ++lastPosition;
            rowNumberDurationWorkedOverFortnight = ++lastPosition;
            rowNumberConsecutiveDays = rosteredShift.getCompliance().getConsecutiveDays() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberLongDay = rosteredShift.getCompliance().getLongDay() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberNight = rosteredShift.getCompliance().getNight() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberRecoveryFollowingNights = rosteredShift.getCompliance().getRecoveryFollowingNights() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberWeekend = rosteredShift.getCompliance().getWeekend() == null ? RecyclerView.NO_POSITION : ++lastPosition;
            rowNumberRosteredDayOff = rosteredShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) rosteredShift.getCompliance()).getRosteredDayOff() != null ? ++lastPosition : RecyclerView.NO_POSITION;
            rowCount = ++lastPosition;
        }
        super.onChanged(oldRosteredShift, rosteredShift);
    }

    @Override
    int getRowNumberComment() {
        return rowNumberComment;
    }

    @Override
    int getFixedRowCount() {
        return rowCount;
    }

    @Override
    void notifyUpdated(@NonNull RosteredShift oldShift, @NonNull RosteredShift newShift) {
        super.notifyUpdated(oldShift, newShift);
        shiftDetailAdapterHelper.onItemUpdated(oldShift, newShift, this);
        if (oldShift.getLoggedShiftData() == null && newShift.getLoggedShiftData() != null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            int lastPosition = ROW_NUMBER_TOGGLE_LOGGED;
            rowNumberLoggedStart = ++lastPosition;
            rowNumberLoggedEnd = ++lastPosition;
            rowNumberComment = ++lastPosition;
            if (rowNumberDurationBetweenShifts != RecyclerView.NO_POSITION)
                rowNumberDurationBetweenShifts = ++lastPosition;
            rowNumberDurationWorkedOverDay = ++lastPosition;
            rowNumberDurationWorkedOverWeek = ++lastPosition;
            rowNumberDurationWorkedOverFortnight = ++lastPosition;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberLongDay != RecyclerView.NO_POSITION)
                rowNumberLongDay = ++lastPosition;
            if (rowNumberNight != RecyclerView.NO_POSITION)
                rowNumberNight = ++lastPosition;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemRangeInserted(rowNumberLoggedStart, NUMBER_OF_ROWS_FOR_LOGGED);
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() == null) {
            notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            notifyItemRangeRemoved(rowNumberLoggedStart, NUMBER_OF_ROWS_FOR_LOGGED);
            rowNumberLoggedStart = RecyclerView.NO_POSITION;
            rowNumberLoggedEnd = RecyclerView.NO_POSITION;
            int lastPosition = ROW_NUMBER_TOGGLE_LOGGED;
            rowNumberComment = ++lastPosition;
            if (rowNumberDurationBetweenShifts != RecyclerView.NO_POSITION)
                rowNumberDurationBetweenShifts = ++lastPosition;
            rowNumberDurationWorkedOverDay = ++lastPosition;
            rowNumberDurationWorkedOverWeek = ++lastPosition;
            rowNumberDurationWorkedOverFortnight = ++lastPosition;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberLongDay != RecyclerView.NO_POSITION)
                rowNumberLongDay = ++lastPosition;
            if (rowNumberNight != RecyclerView.NO_POSITION)
                rowNumberNight = ++lastPosition;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getLoggedShiftData() != null && newShift.getLoggedShiftData() != null) {
            if (!oldShift.getLoggedShiftData().getDuration().equals(newShift.getLoggedShiftData().getDuration())) {
                notifyItemChanged(ROW_NUMBER_TOGGLE_LOGGED);
            }
            if (!oldShift.getLoggedShiftData().getStart().toLocalTime().equals(newShift.getLoggedShiftData().getStart().toLocalTime())) {
                notifyItemChanged(rowNumberLoggedStart);
            }
            if (!oldShift.getLoggedShiftData().getEnd().toLocalTime().equals(newShift.getLoggedShiftData().getEnd().toLocalTime()) || (oldShift.getShiftData().getStart().toLocalDate().isEqual(oldShift.getLoggedShiftData().getEnd().toLocalDate()) ? !newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getLoggedShiftData().getEnd().toLocalDate()) : (newShift.getShiftData().getStart().toLocalDate().isEqual(newShift.getLoggedShiftData().getEnd().toLocalDate()) || !oldShift.getLoggedShiftData().getEnd().getDayOfWeek().equals(newShift.getLoggedShiftData().getEnd().getDayOfWeek())))) {
                notifyItemChanged(rowNumberLoggedEnd);
            }
        }
        if (oldShift.getCompliance().getDurationBetweenShifts() == null && newShift.getCompliance().getDurationBetweenShifts() != null) {
            int lastPosition = rowNumberComment;
            rowNumberDurationBetweenShifts = ++lastPosition;
            rowNumberDurationWorkedOverDay = ++lastPosition;
            rowNumberDurationWorkedOverWeek = ++lastPosition;
            rowNumberDurationWorkedOverFortnight = ++lastPosition;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberLongDay != RecyclerView.NO_POSITION)
                rowNumberLongDay = ++lastPosition;
            if (rowNumberNight != RecyclerView.NO_POSITION)
                rowNumberNight = ++lastPosition;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberDurationBetweenShifts);
        } else if (oldShift.getCompliance().getDurationBetweenShifts() != null && newShift.getCompliance().getDurationBetweenShifts() == null) {
            notifyItemRemoved(rowNumberDurationBetweenShifts);
            rowNumberDurationBetweenShifts = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberComment;
            rowNumberDurationWorkedOverDay = ++lastPosition;
            rowNumberDurationWorkedOverWeek = ++lastPosition;
            rowNumberDurationWorkedOverFortnight = ++lastPosition;
            if (rowNumberConsecutiveDays != RecyclerView.NO_POSITION)
                rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberLongDay != RecyclerView.NO_POSITION)
                rowNumberLongDay = ++lastPosition;
            if (rowNumberNight != RecyclerView.NO_POSITION)
                rowNumberNight = ++lastPosition;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getDurationBetweenShifts() != null && newShift.getCompliance().getDurationBetweenShifts() != null && !oldShift.getCompliance().getDurationBetweenShifts().isEqual(newShift.getCompliance().getDurationBetweenShifts())) {
            notifyItemChanged(rowNumberDurationBetweenShifts);
        }
        if (!oldShift.getCompliance().getDurationOverDay().isEqual(newShift.getCompliance().getDurationOverDay())) {
            notifyItemChanged(rowNumberDurationWorkedOverDay);
        }
        if (!oldShift.getCompliance().getDurationOverWeek().isEqual(newShift.getCompliance().getDurationOverWeek())) {
            notifyItemChanged(rowNumberDurationWorkedOverWeek);
        }
        if (!oldShift.getCompliance().getDurationOverFortnight().isEqual(newShift.getCompliance().getDurationOverFortnight())) {
            notifyItemChanged(rowNumberDurationWorkedOverFortnight);
        }
        if (oldShift.getCompliance().getConsecutiveDays() == null && newShift.getCompliance().getConsecutiveDays() != null) {
            int lastPosition = rowNumberDurationWorkedOverFortnight;
            rowNumberConsecutiveDays = ++lastPosition;
            if (rowNumberLongDay != RecyclerView.NO_POSITION)
                rowNumberLongDay = ++lastPosition;
            if (rowNumberNight != RecyclerView.NO_POSITION)
                rowNumberNight = ++lastPosition;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberConsecutiveDays);
        } else if (oldShift.getCompliance().getConsecutiveDays() != null && newShift.getCompliance().getConsecutiveDays() == null) {
            notifyItemRemoved(rowNumberConsecutiveDays);
            rowNumberConsecutiveDays = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberDurationWorkedOverFortnight;
            if (rowNumberLongDay != RecyclerView.NO_POSITION)
                rowNumberLongDay = ++lastPosition;
            if (rowNumberNight != RecyclerView.NO_POSITION)
                rowNumberNight = ++lastPosition;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getConsecutiveDays() != null && newShift.getCompliance().getConsecutiveDays() != null && !oldShift.getCompliance().getConsecutiveDays().isEqual(newShift.getCompliance().getConsecutiveDays())) {
            notifyItemChanged(rowNumberConsecutiveDays);
        }
        if (oldShift.getCompliance().getLongDay() == null && newShift.getCompliance().getLongDay() != null) {
            int lastPosition = rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            rowNumberLongDay = ++lastPosition;
            if (rowNumberNight != RecyclerView.NO_POSITION)
                rowNumberNight = ++lastPosition;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberLongDay);
        } else if (oldShift.getCompliance().getLongDay() != null && newShift.getCompliance().getLongDay() == null) {
            notifyItemRemoved(rowNumberLongDay);
            rowNumberLongDay = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            if (rowNumberNight != RecyclerView.NO_POSITION)
                rowNumberNight = ++lastPosition;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getLongDay() != null && newShift.getCompliance().getLongDay() != null && !oldShift.getCompliance().getLongDay().isEqual(newShift.getCompliance().getLongDay())) {
            notifyItemChanged(rowNumberLongDay);
        }
        if (oldShift.getCompliance().getNight() == null && newShift.getCompliance().getNight() != null) {
            int lastPosition = rowNumberLongDay != RecyclerView.NO_POSITION ? rowNumberLongDay : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            rowNumberNight = ++lastPosition;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberNight);
        } else if (oldShift.getCompliance().getNight() != null && newShift.getCompliance().getNight() == null) {
            notifyItemRemoved(rowNumberNight);
            rowNumberNight = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberLongDay != RecyclerView.NO_POSITION ? rowNumberLongDay : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            if (rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION)
                rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getNight() != null && newShift.getCompliance().getNight() != null && !oldShift.getCompliance().getNight().isEqual(newShift.getCompliance().getNight())) {
            notifyItemChanged(rowNumberNight);
        }
        if (oldShift.getCompliance().getRecoveryFollowingNights() == null && newShift.getCompliance().getRecoveryFollowingNights() != null) {
            int lastPosition = rowNumberNight != RecyclerView.NO_POSITION ? rowNumberNight : rowNumberLongDay != RecyclerView.NO_POSITION ? rowNumberLongDay : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            rowNumberRecoveryFollowingNights = ++lastPosition;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberRecoveryFollowingNights);
        } else if (oldShift.getCompliance().getRecoveryFollowingNights() != null && newShift.getCompliance().getRecoveryFollowingNights() == null) {
            notifyItemRemoved(rowNumberRecoveryFollowingNights);
            rowNumberRecoveryFollowingNights = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberNight != RecyclerView.NO_POSITION ? rowNumberNight : rowNumberLongDay != RecyclerView.NO_POSITION ? rowNumberLongDay : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            if (rowNumberWeekend != RecyclerView.NO_POSITION)
                rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getRecoveryFollowingNights() != null && newShift.getCompliance().getRecoveryFollowingNights() != null && !oldShift.getCompliance().getRecoveryFollowingNights().isEqual(newShift.getCompliance().getRecoveryFollowingNights())) {
            notifyItemChanged(rowNumberRecoveryFollowingNights);
        }
        if (oldShift.getCompliance().getWeekend() == null && newShift.getCompliance().getWeekend() != null) {
            int lastPosition = rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION ? rowNumberRecoveryFollowingNights : rowNumberNight != RecyclerView.NO_POSITION ? rowNumberNight : rowNumberLongDay != RecyclerView.NO_POSITION ? rowNumberLongDay : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            rowNumberWeekend = ++lastPosition;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberWeekend);
        } else if (oldShift.getCompliance().getWeekend() != null && newShift.getCompliance().getWeekend() == null) {
            notifyItemRemoved(rowNumberWeekend);
            rowNumberWeekend = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION ? rowNumberRecoveryFollowingNights : rowNumberNight != RecyclerView.NO_POSITION ? rowNumberNight : rowNumberLongDay != RecyclerView.NO_POSITION ? rowNumberLongDay : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            if (rowNumberRosteredDayOff != RecyclerView.NO_POSITION)
                rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance().getWeekend() != null && newShift.getCompliance().getWeekend() != null && !oldShift.getCompliance().getWeekend().isEqual(newShift.getCompliance().getWeekend())) {
            notifyItemChanged(rowNumberWeekend);
        }
        if (!(oldShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) oldShift.getCompliance()).getRosteredDayOff() != null) && newShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) newShift.getCompliance()).getRosteredDayOff() != null) {
            int lastPosition = rowNumberWeekend != RecyclerView.NO_POSITION ? rowNumberWeekend : rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION ? rowNumberRecoveryFollowingNights : rowNumberNight != RecyclerView.NO_POSITION ? rowNumberNight : rowNumberLongDay != RecyclerView.NO_POSITION ? rowNumberLongDay : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            rowNumberRosteredDayOff = ++lastPosition;
            rowCount = ++lastPosition;
            notifyItemInserted(rowNumberRosteredDayOff);
        } else if (oldShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) oldShift.getCompliance()).getRosteredDayOff() != null && !(newShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) newShift.getCompliance()).getRosteredDayOff() != null)) {
            notifyItemRemoved(rowNumberRosteredDayOff);
            rowNumberRosteredDayOff = RecyclerView.NO_POSITION;
            int lastPosition = rowNumberWeekend != RecyclerView.NO_POSITION ? rowNumberWeekend : rowNumberRecoveryFollowingNights != RecyclerView.NO_POSITION ? rowNumberRecoveryFollowingNights : rowNumberNight != RecyclerView.NO_POSITION ? rowNumberNight : rowNumberLongDay != RecyclerView.NO_POSITION ? rowNumberLongDay : rowNumberConsecutiveDays != RecyclerView.NO_POSITION ? rowNumberConsecutiveDays : rowNumberDurationWorkedOverFortnight;
            rowCount = ++lastPosition;
        } else if (oldShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) oldShift.getCompliance()).getRosteredDayOff() != null && newShift.getCompliance() instanceof ComplianceSaferRosters && ((ComplianceSaferRosters) newShift.getCompliance()).getRosteredDayOff() != null && !((ComplianceSaferRosters) oldShift.getCompliance()).getRosteredDayOff().isEqual(((ComplianceSaferRosters) newShift.getCompliance()).getRosteredDayOff())) {
            notifyItemChanged(rowNumberRosteredDayOff);
        }
    }

    @Override
    void onBindViewHolder(@NonNull RosteredShift shift, int position, @NonNull ItemViewHolder holder) {
        if (position == ROW_NUMBER_TOGGLE_LOGGED) {
            holder.setupSwitch();
            holder.bindSwitch(shift.getLoggedShiftData() != null, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean logged) {
                    callbacks.setLogged(logged);
                }
            });
            holder.setPrimaryIcon(R.drawable.ic_clipboard_black_24dp);
            holder.hideSecondaryIcon();
            holder.setText(getContext().getString(R.string.logged), shift.getLoggedShiftData() == null ? null : DateTimeUtils.getDurationString(getContext(), shift.getLoggedShiftData().getDuration()));
        } else if (position == rowNumberLoggedStart) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_clipboard_play_black_24dp);
            holder.hideSecondaryIcon();
            //noinspection ConstantConditions
            holder.setText(getContext().getString(R.string.logged_start), DateTimeUtils.getTimeString(shift.getLoggedShiftData().getStart().toLocalTime()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(true, true);
                }
            });
        } else if (position == rowNumberLoggedEnd) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_clipboard_stop_black_24dp);
            holder.hideSecondaryIcon();
            //noinspection ConstantConditions
            holder.setText(getContext().getString(R.string.logged_end), DateTimeUtils.getEndTimeString(shift.getLoggedShiftData().getEnd().toLocalDateTime(), shift.getShiftData().getStart().toLocalDate()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeTime(false, true);
                }
            });
        } else if (position == rowNumberDurationBetweenShifts) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_sleep_black_24dp);
            //noinspection ConstantConditions
            holder.setCompliant(shift.getCompliance().getDurationBetweenShifts());
            holder.setText(getContext().getString(R.string.time_between_shifts), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationBetweenShifts().getDuration()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_minimum_hours_between_shifts, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS));
                }
            });
        } else if (position == rowNumberDurationWorkedOverDay) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_duration_black_24dp);
            holder.setCompliant(shift.getCompliance().getDurationOverDay());
            holder.setText(getContext().getString(R.string.duration_worked_over_day), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverDay().getDuration()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_day, AppConstants.MAXIMUM_HOURS_OVER_DAY));
                }
            });
        } else if (position == rowNumberDurationWorkedOverWeek) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_week_black_24dp);
            holder.setCompliant(shift.getCompliance().getDurationOverWeek());
            holder.setText(getContext().getString(R.string.duration_worked_over_week), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverWeek().getDuration()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_week, AppConstants.MAXIMUM_HOURS_OVER_WEEK));
                }
            });
        } else if (position == rowNumberDurationWorkedOverFortnight) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_weeks_black_24dp);
            holder.setCompliant(shift.getCompliance().getDurationOverFortnight());
            holder.setText(getContext().getString(R.string.duration_worked_over_fortnight), DateTimeUtils.getDurationString(getContext(), shift.getCompliance().getDurationOverFortnight().getDuration()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_hours_over_fortnight, AppConstants.MAXIMUM_HOURS_OVER_FORTNIGHT));
                }
            });
        } else if (position == rowNumberConsecutiveDays) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_consecutive_shifts_black_24dp);
            //noinspection ConstantConditions
            holder.setCompliant(shift.getCompliance().getConsecutiveDays());
            int days = shift.getCompliance().getConsecutiveDays().getIndexOfDayShift() + 1;
            holder.setText(getContext().getString(R.string.number_of_consecutive_days_worked), getContext().getResources().getQuantityString(R.plurals.days, days, days));
            final int message = shift.getCompliance().getConsecutiveDays().getMessage(), maximumConsecutiveDays = shift.getCompliance().getConsecutiveDays().getMaximumConsecutiveDays();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.showMessage(getContext().getString(message, maximumConsecutiveDays));
                }
            });
        } else if (position == rowNumberLongDay) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_long_day_black_24dp);
            //noinspection ConstantConditions
            holder.setCompliant(shift.getCompliance().getLongDay());
            int longDays = shift.getCompliance().getLongDay().getIndexOfLongDay() + 1;
            holder.setText(getContext().getString(R.string.number_of_long_days_in_week), getContext().getResources().getQuantityString(R.plurals.long_days, longDays, longDays));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_long_days_over_week, AppConstants.MAXIMUM_LONG_DAYS_PER_WEEK, AppConstants.MAXIMUM_HOURS_IN_NORMAL_DAY));
                }
            });
        } else if (position == rowNumberNight) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_consecutive_shifts_black_24dp);
            //noinspection ConstantConditions
            holder.setCompliant(shift.getCompliance().getNight());
            int nights = shift.getCompliance().getNight().getIndexOfNightShift() + 1;
            holder.setText(getContext().getString(R.string.number_of_consecutive_nights_worked), getContext().getResources().getQuantityString(R.plurals.nights, nights, nights));

            final int maximumConsecutiveNights = shift.getCompliance().getNight().getMaximumConsecutiveNights();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.showMessage(getContext().getString(R.string.meca_maximum_consecutive_nights, maximumConsecutiveNights));
                }
            });
        } else if (position == rowNumberRecoveryFollowingNights) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_recovery_days_following_nights);
            //noinspection ConstantConditions
            holder.setCompliant(shift.getCompliance().getRecoveryFollowingNights());
            int recoveryDays = shift.getCompliance().getRecoveryFollowingNights().getRecoveryDays(), consecutiveNights = shift.getCompliance().getRecoveryFollowingNights().getConsecutiveNights();
            holder.setText(getContext().getString(R.string.recovery_days_following_nights), getContext().getString(R.string.n_days_following_n_nights, getContext().getResources().getQuantityString(R.plurals.days, recoveryDays, recoveryDays), getContext().getResources().getQuantityString(R.plurals.nights, consecutiveNights, consecutiveNights)));
            final String message;
            if (shift.getCompliance().getRecoveryFollowingNights() instanceof RowRecoveryFollowingNightsSaferRosters) {
                if (consecutiveNights < 2) {
                    message = getContext().getString(R.string.recovery_days_only_required_following_n_nights, getContext().getResources().getQuantityString(R.plurals.nights, 2, 2));
                } else if (consecutiveNights == 2) {
                    message = getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 2, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_2_NIGHTS);
                } else if (consecutiveNights == 3) {
                    if (shift.getCompliance().getRecoveryFollowingNights().isLenient()) {
                        message = getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_fewer_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_LENIENT);
                    } else {
                        message = getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 3, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_3_NIGHTS_STRICT);
                    }
                } else {
                    message = getContext().getString(R.string.meca_safer_rosters_minimum_recovery_after_more_consecutive_nights, 4, AppConstants.SAFER_ROSTERS_MINIMUM_RECOVERY_DAYS_FOLLOWING_4_OR_MORE_NIGHTS);
                }
            } else {
                message = getContext().getString(R.string.meca_minimum_recovery_after_consecutive_nights, AppConstants.MINIMUM_RECOVERY_DAYS_FOLLOWING_NIGHTS, AppConstants.MINIMUM_NIGHTS_BEFORE_RECOVERY);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callbacks.showMessage(message);
                }
            });
        } else if (position == rowNumberWeekend) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_weekend_black_24dp);
            //noinspection ConstantConditions
            holder.setCompliant(shift.getCompliance().getWeekend());
            LocalDate currentWeekend = shift.getCompliance().getWeekend().getCurrentWeekend();
            @StringRes final int title;
            final String secondLine, thirdLine, message;
            if (shift.getCompliance().getWeekend() instanceof RowWeekendLegacy) {
                LocalDate previousWeekend = ((RowWeekendLegacy) shift.getCompliance().getWeekend()).getPreviousWeekend();
                if (previousWeekend == null) {
                    secondLine = getContext().getString(R.string.not_applicable);
                    thirdLine = null;
                } else {
                    secondLine = DateTimeUtils.getWeekendDateSpanString(previousWeekend);
                    //noinspection ConstantConditions
                    thirdLine = DateTimeUtils.getWeeksAgo(getContext(), currentWeekend, previousWeekend);
                }
                message = getContext().getString(R.string.meca_consecutive_weekends);
            } else {
                secondLine = shift.getCompliance().getWeekend().getNumerator() + " out of " + shift.getCompliance().getWeekend().getDenominator();
                thirdLine = null;
                message = getContext().getString(R.string.meca_safer_rosters_maximum_consecutive_weekends,
                        2,
                        shift.getCompliance().getWeekend().getDenominator(),
                        shift.getCompliance().getWeekend().getDenominator() - 2
                );
            }
            holder.setText(getContext().getString(R.string.last_weekend_worked), secondLine, thirdLine);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(message);
                }
            });
        } else if (position == rowNumberRosteredDayOff) {
            holder.setupPlain();
            holder.setPrimaryIcon(R.drawable.ic_safer_rosters_black_24dp);
            RowRosteredDayOff rosteredDayOff = ((ComplianceSaferRosters) shift.getCompliance()).getRosteredDayOff();
            //noinspection ConstantConditions
            holder.setCompliant(rosteredDayOff);
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
            holder.setText(getContext().getString(R.string.rostered_day_off), rosteredDayOff.getDate() == null ? "No RDO found" : DateTimeUtils.getFullDateString(rosteredDayOff.getDate()));
            final boolean allowMidweekRDOs = rosteredDayOff.allowMidweekRDOs();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.showMessage(getContext().getString(allowMidweekRDOs ? R.string.meca_safer_rosters_rostered_day_off_lenient : R.string.meca_safer_rosters_rostered_day_off_strict));
                }
            });
        } else {
            holder.hideSecondaryIcon();
            if (!shiftDetailAdapterHelper.bindViewHolder(shift, position, holder, this)) {
                super.onBindViewHolder(shift, position, holder);
            }
        }
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, DateDetailAdapterHelper.Callbacks {
        void setLogged(boolean logged);
        void changeTime(boolean start, boolean logged);
        void showMessage(@NonNull String message);
    }

}
