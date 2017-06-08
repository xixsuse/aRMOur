package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

class SwitchListItemViewHolder extends ListItemViewHolder implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "SwitchListItemVH";
    private final Switch switchControl;
    private final Uri mUri;
    private final String mClaimedColumnName, mPaidColumnName;
    private boolean mPaidNotClaimed;

    SwitchListItemViewHolder(ViewGroup parent, Uri uri, String claimedColumnName, String paidColumnName) {
        super(parent, R.layout.list_item_switch);
        switchControl = (Switch) itemView.findViewById(R.id.switch_control);
        switchControl.setOnCheckedChangeListener(this);
        mUri = uri;
        mClaimedColumnName = claimedColumnName;
        mPaidColumnName = paidColumnName;
    }

    private void bind(Context context, @Nullable DateTime dateTime, @StringRes int key, @DrawableRes int primaryIconRes, boolean enableToggle) {
        primaryIcon.setImageResource(dateTime == null ? 0 : primaryIconRes);
        setText(context.getString(key), dateTime == null ? context.getString(R.string.not_applicable) : DateTimeUtils.getDateTimeString(dateTime));
        if (switchControl.isChecked() == (dateTime == null)) {
            Log.i(TAG, "bind: need to manually set switch to " + String.valueOf(dateTime != null));
            switchControl.setOnCheckedChangeListener(null);
            switchControl.setChecked(dateTime != null);
            switchControl.setOnCheckedChangeListener(this);
        }
        switchControl.setEnabled(enableToggle);
    }

    void bindClaimed(Context context, @Nullable DateTime claimed, boolean enableToggle) {
        mPaidNotClaimed = false;
        bind(context, claimed, R.string.claimed, R.drawable.ic_check_box_half_black_24dp, enableToggle);
    }

    void bindPaid(Context context, @Nullable DateTime paid) {
        mPaidNotClaimed = true;
        bind(context, paid, R.string.paid, R.drawable.ic_check_box_full_black_24dp, true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ContentValues values = new ContentValues();
        values.put(mPaidNotClaimed ? mPaidColumnName : mClaimedColumnName, isChecked ? System.currentTimeMillis() : null);
        buttonView.getContext().getContentResolver().update(mUri, values, null, null);
    }

}
