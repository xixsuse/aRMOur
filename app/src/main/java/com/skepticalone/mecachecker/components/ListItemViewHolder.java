package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;


public class ListItemViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

    private final ImageView primaryIcon, secondaryIcon;
    private final TextView text;
    private final TextAppearanceSpan firstLineStyle, secondLineStyle, thirdLineStyle;
    private final Switch switchControl;
    private SwitchType mSwitchType;
    @Nullable
    private SwitchCallbacks mSwitchCallbacks;

    ListItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
        primaryIcon = itemView.findViewById(R.id.primary_icon);
        text = itemView.findViewById(R.id.text);
        secondaryIcon = itemView.findViewById(R.id.secondary_icon);
        switchControl = itemView.findViewById(R.id.switch_control);
        switchControl.setOnCheckedChangeListener(this);
        firstLineStyle = new TextAppearanceSpan(itemView.getContext(), R.style.TextAppearance_AppCompat_Subhead);
        secondLineStyle = new TextAppearanceSpan(itemView.getContext(), R.style.TextAppearance_AppCompat_Body1);
        thirdLineStyle = new TextAppearanceSpan(itemView.getContext(), R.style.TextAppearance_AppCompat_Small);
    }

    private void setText(@NonNull String firstLine, @Nullable String secondLine, @Nullable String thirdLine) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(firstLine);
        ssb.setSpan(firstLineStyle, 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (secondLine != null) {
            ssb.append('\n');
            int start = ssb.length();
            ssb.append(secondLine);
            ssb.setSpan(secondLineStyle, start, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (thirdLine != null) {
                ssb.append('\n');
                start = ssb.length();
                ssb.append(thirdLine);
                ssb.setSpan(thirdLineStyle, start, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        text.setText(ssb);
    }

    private void rootBind(
            @DrawableRes int primaryIconRes,
            @NonNull String firstLine,
            @Nullable String secondLine,
            @Nullable String thirdLine,
            @DrawableRes int secondaryIconRes,
            @Nullable SwitchCallbacks switchCallbacks
    ) {
        if (primaryIconRes == 0) {
            primaryIcon.setVisibility(View.GONE);
        } else {
            primaryIcon.setVisibility(View.VISIBLE);
            primaryIcon.setImageResource(primaryIconRes);
        }
        setText(firstLine, secondLine, thirdLine);
        if (secondaryIconRes != 0 && switchCallbacks != null) throw new IllegalArgumentException();
        if (secondaryIconRes == 0) {
            secondaryIcon.setVisibility(View.GONE);
        } else {
            secondaryIcon.setImageResource(secondaryIconRes);
            secondaryIcon.setVisibility(View.VISIBLE);
        }
        mSwitchCallbacks = switchCallbacks;
        switchControl.setVisibility(mSwitchCallbacks == null ? View.GONE : View.VISIBLE);
    }

    private void rootBindSwitch(
            @NonNull SwitchType switchType,
            @DrawableRes int primaryIconRes,
            @NonNull String firstLine,
            @Nullable String secondLine,
            @NonNull SwitchCallbacks switchCallbacks,
            boolean shouldBeChecked,
            boolean enableToggle
    ) {
        rootBind(primaryIconRes, firstLine, secondLine, null, 0, switchCallbacks);
        itemView.setOnClickListener(null);
        mSwitchType = switchType;
        if (switchControl.isChecked() != shouldBeChecked) {
            switchControl.setOnCheckedChangeListener(null);
            switchControl.setChecked(shouldBeChecked);
            switchControl.setOnCheckedChangeListener(this);
        }
        switchControl.setEnabled(enableToggle);
    }

    public void bindPlain(
            @DrawableRes int primaryIconRes,
            @NonNull String firstLine,
            @Nullable String secondLine,
            @Nullable String thirdLine,
            @DrawableRes int secondaryIconRes
    ) {
        rootBind(primaryIconRes, firstLine, secondLine, thirdLine, secondaryIconRes, null);
    }


//    void setText(@NonNull String firstLine, @Nullable String secondLine) {
//        setText(firstLine, secondLine, null);
//    }
//
//    void setText(@NonNull String firstLine) {
//        setText(firstLine, null);
//    }

//    private void bind(Context context, @NonNull SwitchType switchType, @DrawableRes int primaryIconRes, @StringRes int keyRes, @Nullable String value, boolean shouldBeChecked, boolean enableToggle) {
//        rootBind(context, primaryIconRes, keyRes, value, null);
//        mSwitchType = switchType;
//        if (switchControl.isChecked() != shouldBeChecked) {
//            switchControl.setOnCheckedChangeListener(null);
//            switchControl.setChecked(shouldBeChecked);
//            switchControl.setOnCheckedChangeListener(this);
//        }
//        switchControl.setEnabled(enableToggle);
//    }

    private void rootBindSwitchPaidOrClaimed(
            @NonNull Context context,
            @NonNull SwitchType switchType,
            @DrawableRes int primaryIconRes,
            @StringRes int key,
            @Nullable DateTime dateTime,
            @NonNull SwitchCallbacks switchCallbacks,
            boolean enableToggle
    ) {
        rootBindSwitch(switchType, dateTime == null ? 0 : primaryIconRes, context.getString(key), dateTime == null ? null : DateTimeUtils.getDateTimeString(dateTime), switchCallbacks, dateTime != null, enableToggle);
    }

    public void bindClaimed(
            @NonNull Context context,
            @Nullable DateTime claimed,
            @NonNull SwitchCallbacks switchCallbacks,
            boolean isPaid
    ) {
        rootBindSwitchPaidOrClaimed(context, SwitchType.CLAIMED, R.drawable.ic_check_box_half_black_24dp, R.string.claimed, claimed, switchCallbacks, !isPaid);
    }

    public void bindPaid(
            @NonNull Context context,
            @Nullable DateTime paid,
            @NonNull SwitchCallbacks switchCallbacks
    ) {
        rootBindSwitchPaidOrClaimed(context, SwitchType.PAID, R.drawable.ic_check_box_full_black_24dp, R.string.paid, paid, switchCallbacks, true);
    }

    public void bindLogged(
            @NonNull Context context,
            boolean isLogged,
            @NonNull SwitchCallbacks switchCallbacks
    ) {
        rootBindSwitch(SwitchType.LOGGED, R.drawable.ic_clipboard_black_24dp, context.getString(R.string.log_hours), null, switchCallbacks, isLogged, true);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        assert mSwitchCallbacks != null;
        mSwitchCallbacks.onCheckedChanged(mSwitchType, isChecked);
    }

    public enum SwitchType {
        CLAIMED,
        PAID,
        LOGGED
    }

    public interface SwitchCallbacks {
        void onCheckedChanged(SwitchType switchType, boolean isChecked);
    }

}
