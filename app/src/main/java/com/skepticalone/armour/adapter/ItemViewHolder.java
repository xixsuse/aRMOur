package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.CallSuper;
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

public final class ItemViewHolder extends RecyclerView.ViewHolder {

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

    public void setupPlain() {
        switchControl.setVisibility(View.GONE);
    }

    public void setupSwitch() {
        switchControl.setVisibility(View.VISIBLE);
    }

    public void setPrimaryIcon(@DrawableRes int icon) {
        primaryIcon.setImageResource(icon);
    }

    public void setSecondaryIcon(@DrawableRes int icon) {
        secondaryIcon.setImageResource(icon);
    }

    public void hideSecondaryIcon() {
        secondaryIcon.setVisibility(View.GONE);
    }

    public void setText(@NonNull String firstLine) {
        setText(firstLine, null);
    }

    public void setText(@NonNull String firstLine, @Nullable String secondLine) {
        setText(firstLine, secondLine, null);
    }

    public void setText(@NonNull String firstLine, @Nullable String secondLine, @Nullable String thirdLine) {
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

    public static abstract class Binder {

        @CallSuper
        void onBindViewHolder(@NonNull ItemViewHolder holder) {
            holder.primaryIcon.setImageResource(getPrimaryIcon());
            Context context = holder.itemView.getContext();
            holder.setText(getFirstLine(context), getSecondLine(context), getThirdLine(context));
        }

        @CallSuper
        boolean areItemsTheSame(@NonNull Binder other) {
            return getClass() == other.getClass();
        }

        abstract boolean areContentsTheSame(@NonNull Binder other);

        @DrawableRes
        abstract int getPrimaryIcon();

        @NonNull
        abstract String getFirstLine(@NonNull Context context);

        @Nullable
        String getSecondLine(@NonNull Context context) {
            return null;
        }

        @Nullable
        String getThirdLine(@NonNull Context context) {
            return null;
        }

    }

    static abstract class PlainBinder extends Binder implements View.OnClickListener {

        @Override
        final void onBindViewHolder(@NonNull ItemViewHolder holder) {
            super.onBindViewHolder(holder);
            holder.switchControl.setVisibility(View.GONE);
            if (showSecondaryIcon()) {
                holder.secondaryIcon.setImageResource(getSecondaryIcon());
                holder.secondaryIcon.setVisibility(View.VISIBLE);
            } else {
                holder.secondaryIcon.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }

        boolean showSecondaryIcon() {
            return false;
        }

        @DrawableRes
        int getSecondaryIcon() {
            throw new UnsupportedOperationException();
        }

    }

    static abstract class SwitchBinder extends Binder implements CompoundButton.OnCheckedChangeListener {

        @Override
        final void onBindViewHolder(@NonNull final ItemViewHolder holder) {
            super.onBindViewHolder(holder);
            holder.secondaryIcon.setVisibility(View.GONE);
            boolean enabled = isEnabled(), checked = isChecked();
            holder.itemView.setOnClickListener(enabled ? new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.switchControl.toggle();
                }
            } : null);
            holder.switchControl.setEnabled(enabled);
            if (holder.switchControl.isChecked() != checked) {
                holder.switchControl.setOnCheckedChangeListener(null);
                holder.switchControl.setChecked(checked);
            }
            holder.switchControl.setOnCheckedChangeListener(enabled ? this : null);
            holder.switchControl.setVisibility(View.VISIBLE);
        }

        abstract boolean isChecked();

        abstract boolean isEnabled();

    }
}
