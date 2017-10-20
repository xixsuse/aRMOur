package com.skepticalone.armour.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.Compliance;

final class ItemViewHolder extends RecyclerView.ViewHolder {

    private final ImageView primaryIcon, secondaryIcon;
    private final SwitchCompat switchControl;
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

    void setupPlain() {
        switchControl.setVisibility(View.GONE);
    }

    void setupSwitch() {
        switchControl.setVisibility(View.VISIBLE);
    }

    void setPrimaryIcon(@DrawableRes int icon) {
        primaryIcon.setImageResource(icon);
    }

    void setSecondaryIcon(@DrawableRes int icon) {
        secondaryIcon.setImageResource(icon);
    }

    void hideSecondaryIcon() {
        secondaryIcon.setVisibility(View.GONE);
    }

    void setCompliant(@Nullable Boolean compliant) {
        if (compliant == null) {
            secondaryIcon.setVisibility(View.GONE);
        } else {
            secondaryIcon.setImageResource(Compliance.getComplianceIcon(compliant));
            secondaryIcon.setVisibility(View.VISIBLE);
        }
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

    void bindSwitch(boolean checked, @Nullable CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        boolean enabled = onCheckedChangeListener != null;
        itemView.setOnClickListener(enabled ? new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchControl.toggle();
            }
        } : null);
        switchControl.setEnabled(enabled);
        if (switchControl.isChecked() != checked) {
            switchControl.setOnCheckedChangeListener(null);
            switchControl.setChecked(checked);
        }
        switchControl.setOnCheckedChangeListener(onCheckedChangeListener);
    }

}
