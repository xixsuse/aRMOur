package com.skepticalone.mecachecker.components.shifts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;


abstract class ListItemViewAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

}
