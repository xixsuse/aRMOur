package com.skepticalone.armour.help;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


abstract class HelpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HelpItemDivider.Callbacks {

    static final int
            ITEM_TYPE_TITLE = 1,
            ITEM_TYPE_LABEL_PLAIN = 2,
            ITEM_TYPE_LABEL_FAB = 3,
            ITEM_TYPE_DESCRIPTION = 4;

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_TITLE:
                return TextViewHolder.title(parent);
            case ITEM_TYPE_LABEL_PLAIN:
                return new LabelViewHolder.Plain(parent);
            case ITEM_TYPE_LABEL_FAB:
                return new LabelViewHolder.Fab(parent);
            case ITEM_TYPE_DESCRIPTION:
                return TextViewHolder.description(parent);
            default:
                throw new IllegalStateException();
        }
    }

}
