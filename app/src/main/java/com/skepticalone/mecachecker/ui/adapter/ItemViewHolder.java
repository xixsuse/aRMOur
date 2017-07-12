package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;

class ItemViewHolder extends RecyclerView.ViewHolder {

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

    void setText(@StringRes int firstLine) {
        setText(firstLine, null);
    }

    void setText(@StringRes int firstLine, @StringRes int secondLine) {
        setText(firstLine, text.getContext().getString(secondLine));
    }

    void setText(@StringRes int firstLine, @StringRes int secondLineFormat, Object... secondLineArguments) {
        setText(firstLine, text.getContext().getString(secondLineFormat, secondLineArguments));
    }

    void setText(@StringRes int firstLine, @Nullable String secondLine) {
        setText(text.getContext().getString(firstLine), secondLine);
    }

    void setText(@NonNull String firstLine) {
        setText(firstLine, null);
    }

    void setText(@NonNull String firstLine, @Nullable String secondLine) {
        setText(firstLine, secondLine, null);
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
}
