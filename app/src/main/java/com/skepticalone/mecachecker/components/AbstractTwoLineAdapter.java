package com.skepticalone.mecachecker.components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;


abstract class AbstractTwoLineAdapter extends RecyclerView.Adapter<AbstractTwoLineAdapter.CustomViewHolder> {
    AbstractTwoLineAdapter() {
        super();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shift_list_item, parent, false));
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView primaryTextView, secondaryTextView;
        final ImageView primaryIconView, secondaryIconView;

        CustomViewHolder(View itemView) {
            super(itemView);
            primaryTextView = (TextView) itemView.findViewById(R.id.primary_text);
            secondaryTextView = (TextView) itemView.findViewById(R.id.secondary_text);
            primaryIconView = (ImageView) itemView.findViewById(R.id.primary_icon);
            secondaryIconView = (ImageView) itemView.findViewById(R.id.secondary_icon);
        }
    }

}
