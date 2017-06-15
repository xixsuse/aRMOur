package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.support.v4.app.DialogFragment;


abstract class PickerDialogFragment extends DialogFragment {

    private Callbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    void onOverlappingShifts() {
        mCallbacks.onOverlappingShifts();
    }

    interface Callbacks {
        void onOverlappingShifts();
    }

}
