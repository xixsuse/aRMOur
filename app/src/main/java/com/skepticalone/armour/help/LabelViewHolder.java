package com.skepticalone.armour.help;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.armour.R;

abstract class LabelViewHolder extends RecyclerView.ViewHolder {

    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @NonNull
    final ImageView iconView;
    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    @NonNull
    final TextView labelView;

    private LabelViewHolder(@LayoutRes int layout, @NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
        iconView = itemView.findViewById(R.id.icon);
        labelView = itemView.findViewById(R.id.label);
    }

    static final class Plain extends LabelViewHolder {

        Plain(@NonNull ViewGroup parent) {
            super(R.layout.help_label_view_plain, parent);
        }

        final void bind(boolean menuItem, @DrawableRes int icon, @StringRes int label) {
            iconView.setBackgroundResource(menuItem ? R.drawable.menu_background : 0);
            iconView.setImageResource(icon);
            iconView.setScaleType(menuItem ? ImageView.ScaleType.CENTER : ImageView.ScaleType.FIT_CENTER);
            labelView.setText(label);
        }

    }

    static final class Fab extends LabelViewHolder {

        Fab(@NonNull ViewGroup parent) {
            super(R.layout.help_label_view_fab, parent);
        }

        final void bind(@DrawableRes int icon, @StringRes int label) {
            iconView.setImageResource(icon);
            labelView.setText(label);
        }

    }

}