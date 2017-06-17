package com.skepticalone.mecachecker.dialog;

import android.content.Context;
import android.support.v4.app.DialogFragment;


public abstract class PickerDialogFragment extends DialogFragment {

    private Callbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    void onOverlappingShifts() {
        mCallbacks.onOverlappingShifts();
    }

    public interface Callbacks {
        void onOverlappingShifts();
    }

}
