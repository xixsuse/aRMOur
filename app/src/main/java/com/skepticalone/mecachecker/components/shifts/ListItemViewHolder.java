package com.skepticalone.mecachecker.components.shifts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;


class ListItemViewHolder extends RecyclerView.ViewHolder {

    final TextView text;
    final ImageView primaryIcon, secondaryIcon;

    ListItemViewHolder(View itemView) {
        super(itemView);
        text = (TextView) itemView.findViewById(R.id.text);
        primaryIcon = (ImageView) itemView.findViewById(R.id.primary_icon);
        secondaryIcon = (ImageView) itemView.findViewById(R.id.secondary_icon);
    }

}
