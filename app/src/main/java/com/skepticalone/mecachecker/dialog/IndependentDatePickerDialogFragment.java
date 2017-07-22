package com.skepticalone.mecachecker.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;

import org.joda.time.LocalDate;

public final class IndependentDatePickerDialogFragment extends LifecycleDialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog datePickerDialog;
    private ViewModelCallbacks viewModel;

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        LocalDate today = LocalDate.now();
        datePickerDialog = new DatePickerDialog(
                context, this,
                today.getYear(),
                today.getMonthOfYear() - 1,
                today.getDayOfMonth()
        );
    }

    @Override
    @NonNull
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        return datePickerDialog;
    }

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ((TargetFragmentCallbacks) getTargetFragment()).getViewModel(getActivity());
        if (savedInstanceState == null) {
            viewModel.getCurrentDate().observe(this, new Observer<LocalDate>() {
                @Override
                public void onChanged(@Nullable LocalDate currentDate) {
                    if (currentDate != null) {
                        datePickerDialog.updateDate(currentDate.getYear(), currentDate.getMonthOfYear() - 1, currentDate.getDayOfMonth());
                    }
                }
            });
        }
    }

    @Override
    public final void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        viewModel.saveNewDate(new LocalDate(year, month + 1, dayOfMonth));
    }

    public interface ViewModelCallbacks {
        @NonNull LiveData<LocalDate> getCurrentDate();
        void saveNewDate(@NonNull LocalDate newDate);
    }

    public interface TargetFragmentCallbacks {
        @NonNull ViewModelCallbacks getViewModel(@NonNull FragmentActivity activity);
    }

}
