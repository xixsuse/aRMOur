package com.skepticalone.mecachecker.components.shifts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;


class ListItemViewHolder extends RecyclerView.ViewHolder {

    final ImageView primaryIcon, secondaryIcon;
    private final TextView text;
    private final TextAppearanceSpan firstLineStyle, secondLineStyle, thirdLineStyle;

    ListItemViewHolder(View itemView) {
        super(itemView);
        text = (TextView) itemView.findViewById(R.id.text);
        primaryIcon = (ImageView) itemView.findViewById(R.id.primary_icon);
        secondaryIcon = (ImageView) itemView.findViewById(R.id.secondary_icon);
        firstLineStyle = new TextAppearanceSpan(itemView.getContext(), R.style.TextAppearance_AppCompat_Subhead);
        secondLineStyle = new TextAppearanceSpan(itemView.getContext(), R.style.TextAppearance_AppCompat_Body1);
        thirdLineStyle = new TextAppearanceSpan(itemView.getContext(), R.style.TextAppearance_AppCompat_Small);
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

    void setText(@NonNull String firstLine, @Nullable String secondLine) {
        setText(firstLine, secondLine, null);
    }

    void setText(@NonNull String firstLine) {
        setText(firstLine, null);
    }

}
