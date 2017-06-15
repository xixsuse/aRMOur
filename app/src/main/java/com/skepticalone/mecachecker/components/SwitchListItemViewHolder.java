package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
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
    private SwitchType mSwitchType;

    SwitchListItemViewHolder(ViewGroup parent, Callbacks callbacks) {
        super(parent, R.layout.list_item_switch);
        mCallbacks = callbacks;
        switchControl = itemView.findViewById(R.id.switch_control);
        switchControl.setOnCheckedChangeListener(this);
    }

//    private void bind(Context context, @DrawableRes int primaryIconRes, @StringRes int key, @NonNull String value, boolean shouldBeChecked, boolean enableToggle) {
//        rootBind(context, primaryIconRes, key, value, null);
//        if (switchControl.isChecked() != shouldBeChecked) {
//            switchControl.setOnCheckedChangeListener(null);
//            switchControl.setChecked(shouldBeChecked);
//            switchControl.setOnCheckedChangeListener(this);
//        }
//        switchControl.setEnabled(enableToggle);
//    }

    private void bind(Context context, @NonNull SwitchType switchType, @DrawableRes int primaryIconRes, @StringRes int keyRes, @Nullable String value, boolean shouldBeChecked, boolean enableToggle) {
        rootBind(context, primaryIconRes, keyRes, value, null);
        mSwitchType = switchType;
        if (switchControl.isChecked() != shouldBeChecked) {
            switchControl.setOnCheckedChangeListener(null);
            switchControl.setChecked(shouldBeChecked);
            switchControl.setOnCheckedChangeListener(this);
        }
        switchControl.setEnabled(enableToggle);
    }

    private void bindPaidOrClaimed(Context context, @NonNull SwitchType switchType, @Nullable DateTime dateTime, @DrawableRes int primaryIconRes, @StringRes int key, boolean enableToggle) {
        bind(context, switchType, dateTime == null ? 0 : primaryIconRes, key, dateTime == null ? context.getString(R.string.not_applicable) : DateTimeUtils.getDateTimeString(dateTime), dateTime != null, enableToggle);
    }

    void bindClaimed(Context context, @Nullable DateTime claimed, boolean isPaid) {
        bindPaidOrClaimed(context, SwitchType.CLAIMED, claimed, R.drawable.ic_check_box_half_black_24dp, R.string.claimed, !isPaid);
    }

    void bindPaid(Context context, @Nullable DateTime paid) {
        bindPaidOrClaimed(context, SwitchType.PAID, paid, R.drawable.ic_check_box_full_black_24dp, R.string.paid, true);
    }

    void bindLogged(Context context, boolean isLogged) {
        bind(context, SwitchType.LOGGED, R.drawable.ic_clipboard_black_24dp, R.string.log_hours, null, isLogged, true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCallbacks.onCheckedChanged(mSwitchType, isChecked);
    }

    enum SwitchType {
        CLAIMED,
        PAID,
        LOGGED
    }

    interface Callbacks {
        void onCheckedChanged(SwitchType switchType, boolean isChecked);
    }

}
