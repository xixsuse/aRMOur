package com.skepticalone.armour.help;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.armour.R;

final class TextViewHolder extends RecyclerView.ViewHolder {

    private TextViewHolder(@NonNull ViewGroup parent, @LayoutRes int layout) {
        super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @NonNull
    static TextViewHolder title(@NonNull ViewGroup parent) {
        return new TextViewHolder(parent, R.layout.help_title_view);
    }

    @NonNull
    static TextViewHolder description(@NonNull ViewGroup parent) {
        return new TextViewHolder(parent, R.layout.help_description_view);
    }

    final void bind(@StringRes int text) {
        ((TextView) itemView).setText(text);
    }

    final void bind(@NonNull String text) {
        ((TextView) itemView).setText(text);
    }

}