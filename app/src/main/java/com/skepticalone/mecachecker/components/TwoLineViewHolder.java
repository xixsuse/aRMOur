package com.skepticalone.mecachecker.components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;

class TwoLineViewHolder extends RecyclerView.ViewHolder {

    final TextView primaryTextView, secondaryTextView;
    final ImageView primaryIconView, secondaryIconView;

    TwoLineViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.shift_list_item, parent, false));
        primaryTextView = (TextView) itemView.findViewById(R.id.primary_text);
        secondaryTextView = (TextView) itemView.findViewById(R.id.secondary_text);
        primaryIconView = (ImageView) itemView.findViewById(R.id.primary_icon);
        secondaryIconView = (ImageView) itemView.findViewById(R.id.secondary_icon);
    }

}