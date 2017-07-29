package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.util.ShiftUtil;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public interface ShiftViewModelContract<Entity> extends ViewModelContract<Entity> {

    void addNewShift(@NonNull ShiftUtil.ShiftType shiftType);
    void saveNewDate(@NonNull LocalDate date);
    void saveNewTime(@NonNull LocalTime time, boolean isStart);

}
