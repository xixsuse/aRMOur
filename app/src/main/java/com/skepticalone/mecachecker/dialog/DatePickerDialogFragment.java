package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

public final class DatePickerDialogFragment extends AbstractDatePickerDialogFragment {

    private Callbacks callbacks;

    public static DatePickerDialogFragment newInstance(long id, @NonNull LocalDate date) {
        Bundle args = getArgs(id, date);
        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) getTargetFragment();
    }

    @Override
    void saveDate(@NonNull LocalDate newDate) {
        callbacks.saveDate(getItemId(), newDate);
    }

    public interface Callbacks {
        void saveDate(long id, @NonNull LocalDate date);
    }

}
