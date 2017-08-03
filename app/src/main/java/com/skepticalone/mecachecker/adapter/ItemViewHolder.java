package com.skepticalone.mecachecker.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;

import java.math.BigDecimal;

final class ItemViewHolder extends RecyclerView.ViewHolder {

    final ImageView primaryIcon, secondaryIcon;
    final SwitchCompat switchControl;
    private final TextView text;
    private final TextAppearanceSpan firstLineStyle, secondLineStyle, thirdLineStyle;

    ItemViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
        primaryIcon = itemView.findViewById(R.id.primary_icon);
        text = itemView.findViewById(R.id.text);
        secondaryIcon = itemView.findViewById(R.id.secondary_icon);
        switchControl = itemView.findViewById(R.id.switch_control);
        firstLineStyle = new TextAppearanceSpan(parent.getContext(), R.style.TextAppearance_AppCompat_Subhead);
        secondLineStyle = new TextAppearanceSpan(parent.getContext(), R.style.TextAppearance_AppCompat_Body1);
        thirdLineStyle = new TextAppearanceSpan(parent.getContext(), R.style.TextAppearance_AppCompat_Small);
    }

    private void setup(
            @DrawableRes int icon,
            @Nullable View.OnClickListener onClickListener,
            boolean switchVisible,
            boolean switchChecked,
            @Nullable CompoundButton.OnCheckedChangeListener onCheckedChangeListener
    ) {
        primaryIcon.setImageResource(icon);
        if (switchVisible) {
            if (switchControl.isChecked() != switchChecked) {
                switchControl.setOnCheckedChangeListener(null);
                switchControl.setChecked(switchChecked);
            }
            switchControl.setEnabled(onCheckedChangeListener != null);
            onClickListener = onCheckedChangeListener == null ? null : new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switchControl.toggle();
                }
            };
            switchControl.setVisibility(View.VISIBLE);
        } else {
            switchControl.setVisibility(View.GONE);
        }
        switchControl.setOnCheckedChangeListener(onCheckedChangeListener);
        itemView.setOnClickListener(onClickListener);
    }

    void setupPlain(
            @DrawableRes int icon,
            @Nullable View.OnClickListener onClickListener
    ) {
        setup(icon, onClickListener, false, false, null);
    }

    void setupSwitch(
            @DrawableRes int icon,
            boolean switchChecked,
            @Nullable CompoundButton.OnCheckedChangeListener onCheckedChangeListener
    ) {
        setup(icon, null, true, switchChecked, onCheckedChangeListener);
    }

    @NonNull
    String getText(@StringRes int resId) {
        return text.getContext().getString(resId);
    }

    @NonNull
    String getCurrencyText(@NonNull BigDecimal value) {
        return text.getContext().getString(R.string.currency_format, value);
    }

    void setText(@NonNull String firstLine) {
        setText(firstLine, null);
    }

    void setText(@NonNull String firstLine, @Nullable String secondLine) {
        setText(firstLine, secondLine, null);
    }

    void setText(@NonNull String firstLine, @Nullable String secondLine, @Nullable String thirdLine) {
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

    void setCompliant(boolean compliant) {
        secondaryIcon.setImageResource(RosteredShiftEntity.getComplianceIcon(compliant));
        secondaryIcon.setVisibility(View.VISIBLE);
    }
}
