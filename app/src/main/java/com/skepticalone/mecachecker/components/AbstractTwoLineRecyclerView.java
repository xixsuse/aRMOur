package com.skepticalone.mecachecker.components;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;


abstract class AbstractTwoLineRecyclerView extends RecyclerView.Adapter<AbstractTwoLineRecyclerView.CustomViewHolder> {

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shift_list_content, parent, false));
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView dateView, timeSpanView;
        final ImageView shiftIconView, complianceIconView;

        CustomViewHolder(View itemView) {
            super(itemView);
            dateView = (TextView) itemView.findViewById(R.id.date);
            timeSpanView = (TextView) itemView.findViewById(R.id.time_span);
            shiftIconView = (ImageView) itemView.findViewById(R.id.shift_icon);
            complianceIconView = (ImageView) itemView.findViewById(R.id.compliance_icon);
        }
    }

}
