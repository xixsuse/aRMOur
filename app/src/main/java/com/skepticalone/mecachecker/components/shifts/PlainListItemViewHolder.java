package com.skepticalone.mecachecker.components.shifts;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.skepticalone.mecachecker.R;

class PlainListItemViewHolder extends ListItemViewHolder {

    final ImageView secondaryIcon;

    PlainListItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_plain);
        secondaryIcon = (ImageView) itemView.findViewById(R.id.secondary_icon);
    }
}
