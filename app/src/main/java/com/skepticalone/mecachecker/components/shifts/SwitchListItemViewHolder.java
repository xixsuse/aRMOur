package com.skepticalone.mecachecker.components.shifts;

import android.view.ViewGroup;
import android.widget.Switch;

import com.skepticalone.mecachecker.R;

class SwitchListItemViewHolder extends ListItemViewHolder {

    final Switch switchControl;

    SwitchListItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.list_item_switch);
        switchControl = (Switch) itemView.findViewById(R.id.switch_control);
    }

}
