package com.skepticalone.armour.adapter;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.armour.R;

final class HelpViewHolder extends RecyclerView.ViewHolder {
    @NonNull
    private final TextView primaryText, secondaryText;
    @NonNull
    private final ImageView imageView;

    HelpViewHolder(@NonNull ViewGroup parent, @LayoutRes int layout) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
        primaryText = itemView.findViewById(R.id.primary_text);
        secondaryText = itemView.findViewById(R.id.secondary_text);
        imageView = itemView.findViewById(R.id.image);
    }

    static final class Binder {
        @DrawableRes
        private final int icon;
        private final boolean fab;
        @NonNull
        private final String firstLine;
        @Nullable
        private final String secondLine;

        Binder(boolean fab, @DrawableRes int icon, @NonNull String firstLine, @Nullable String secondLine) {
            this.icon = icon;
            this.fab = fab;
            this.firstLine = firstLine;
            this.secondLine = secondLine;
        }

        final void bindViewHolder(@NonNull HelpViewHolder holder) {
            holder.imageView.setImageResource(icon);
            holder.primaryText.setText(firstLine);
            if (secondLine == null) {
                holder.secondaryText.setVisibility(View.GONE);
            } else {
                holder.secondaryText.setText(secondLine);
                holder.secondaryText.setVisibility(View.VISIBLE);
            }
        }

        @LayoutRes
        final int getItemViewType() {
            return fab ? R.layout.help_item_fab : R.layout.help_item_plain;
        }

    }

}
