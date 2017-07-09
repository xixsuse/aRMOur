package com.skepticalone.mecachecker.ui.components;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatDialogFragment;


abstract class AbstractDialogFragment extends AppCompatDialogFragment {
    private static final String ITEM_ID = "ITEM_ID";
    private long mItemId;

    static Bundle getArgs(long itemId) {
        Bundle args = new Bundle();
        args.putLong(ITEM_ID, itemId);
        return args;
    }

    @Override
    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        mItemId = getArguments().getLong(ITEM_ID);
    }

    long getItemId() {
        return mItemId;
    }

}
