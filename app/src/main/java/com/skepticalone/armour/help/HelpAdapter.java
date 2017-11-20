package com.skepticalone.armour.help;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


abstract class HelpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int
            ITEM_TYPE_TITLE = 1,
            ITEM_TYPE_LABEL = 2,
            ITEM_TYPE_DESCRIPTION = 3;

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_TITLE:
                return TextViewHolder.title(parent);
            case ITEM_TYPE_LABEL:
                return new LabelViewHolder(parent);
            case ITEM_TYPE_DESCRIPTION:
                return TextViewHolder.description(parent);
            default:
                throw new IllegalStateException();
        }
    }

}
