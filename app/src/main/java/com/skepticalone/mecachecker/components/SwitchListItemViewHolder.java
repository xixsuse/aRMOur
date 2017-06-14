package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

class SwitchListItemViewHolder extends ListItemViewHolder implements CompoundButton.OnCheckedChangeListener {
    private final Switch switchControl;
    private final Callbacks mCallbacks;
    private boolean mIsPaidSwitch;

    SwitchListItemViewHolder(ViewGroup parent, Callbacks callbacks) {
        super(parent, R.layout.list_item_switch);
        mCallbacks = callbacks;
        switchControl = itemView.findViewById(R.id.switch_control);
        switchControl.setOnCheckedChangeListener(this);
    }

    private void bind(Context context, @Nullable DateTime dateTime, @StringRes int key, @DrawableRes int primaryIconRes, boolean enableToggle) {
        bind(context, dateTime == null ? 0 : primaryIconRes, key, dateTime == null ? context.getString(R.string.not_applicable) : DateTimeUtils.getDateTimeString(dateTime), null);
        if (switchControl.isChecked() == (dateTime == null)) {
            switchControl.setOnCheckedChangeListener(null);
            switchControl.setChecked(dateTime != null);
            switchControl.setOnCheckedChangeListener(this);
        }
        switchControl.setEnabled(enableToggle);
    }

    void bindClaimed(Context context, @Nullable DateTime claimed, @Nullable DateTime paid) {
        mIsPaidSwitch = false;
        bind(context, claimed, R.string.claimed, R.drawable.ic_check_box_half_black_24dp, paid == null);
    }

    void bindPaid(Context context, @Nullable DateTime paid) {
        mIsPaidSwitch = true;
        bind(context, paid, R.string.paid, R.drawable.ic_check_box_full_black_24dp, true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCallbacks.onCheckedChanged(mIsPaidSwitch, isChecked);
    }

    interface Callbacks {
        void onCheckedChanged(boolean isPaidSwitch, boolean isChecked);
    }

}
