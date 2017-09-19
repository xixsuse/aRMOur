package com.skepticalone.armour.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.skepticalone.armour.data.viewModel.DateViewModelContract;

import org.threeten.bp.LocalDate;

abstract class DateDialogFragment<Entity> extends DialogFragment<Entity> implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog datePickerDialog;

    @NonNull
    @Override
    abstract DateViewModelContract<Entity> getViewModel();

    @Override
    @NonNull
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        LocalDate today = LocalDate.now();
        datePickerDialog = new DatePickerDialog(
                getActivity(), this,
                today.getYear(),
                today.getMonthValue() - 1,
                today.getDayOfMonth()
        );
        return datePickerDialog;
    }

    @NonNull
    abstract LocalDate getDateForDisplay(@NonNull Entity item);

    @Override
    final void onUpdateView(@NonNull Entity item) {
        LocalDate date = getDateForDisplay(item);
        datePickerDialog.updateDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
    }

    @Override
    public final void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            getViewModel().saveNewDate(LocalDate.of(year, month + 1, dayOfMonth));
        }
    }

}
