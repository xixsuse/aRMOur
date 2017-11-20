package com.skepticalone.armour.help;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.armour.R;

final class LabelViewHolder extends RecyclerView.ViewHolder {

    @NonNull
    private final ImageView iconView;
    @NonNull
    private final TextView labelView;

    LabelViewHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.help_label_view, parent, false));
        iconView = itemView.findViewById(R.id.icon);
        labelView = itemView.findViewById(R.id.label);
    }

    final void bind(boolean menuItem, @DrawableRes int icon, @StringRes int label) {
        iconView.setBackgroundResource(menuItem ? R.drawable.menu_background : 0);
        iconView.setImageResource(icon);
        iconView.setScaleType(menuItem ? ImageView.ScaleType.CENTER : ImageView.ScaleType.FIT_CENTER);
        labelView.setText(label);
    }

}