package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;


abstract class ListItemViewHolder extends RecyclerView.ViewHolder {

    final ImageView primaryIcon;
    private final TextView text;
    private final TextAppearanceSpan firstLineStyle, secondLineStyle, thirdLineStyle;

    private ListItemViewHolder(View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.text);
        primaryIcon = itemView.findViewById(R.id.primary_icon);
        firstLineStyle = new TextAppearanceSpan(itemView.getContext(), R.style.TextAppearance_AppCompat_Subhead);
        secondLineStyle = new TextAppearanceSpan(itemView.getContext(), R.style.TextAppearance_AppCompat_Body1);
        thirdLineStyle = new TextAppearanceSpan(itemView.getContext(), R.style.TextAppearance_AppCompat_Small);
    }

    ListItemViewHolder(ViewGroup parent, @LayoutRes int layout) {
        this(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    void bind(Context context, @DrawableRes int primaryIconRes, @StringRes int keyRes, @Nullable String value, @Nullable View.OnClickListener listener) {
        primaryIcon.setImageResource(primaryIconRes);
        setText(context.getString(keyRes), value);
        itemView.setOnClickListener(listener);
    }

    final void setText(@NonNull String firstLine, @Nullable String secondLine, @Nullable String thirdLine) {
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

    final void setText(@NonNull String firstLine, @Nullable String secondLine) {
        setText(firstLine, secondLine, null);
    }

    final void setText(@NonNull String firstLine) {
        setText(firstLine, null);
    }

}
