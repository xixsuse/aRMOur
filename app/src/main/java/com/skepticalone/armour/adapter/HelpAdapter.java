package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.armour.R;


public final class HelpAdapter extends ContextAdapter<HelpAdapter.ViewHolder> {

    private static final int
            ROW_NUMBER_FIRST_HELP_ITEM = 0,
            ROW_NUMBER_SECOND_HELP_ITEM = 1,
            ROW_COUNT = 2;

    public HelpAdapter(@NonNull Context context) {
        super(context);
    }

    @LayoutRes
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case ROW_NUMBER_FIRST_HELP_ITEM:
                return R.layout.help_item_plain;
            case ROW_NUMBER_SECOND_HELP_ITEM:
                return R.layout.help_item_fab;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        @DrawableRes final int icon;
        @NonNull final String firstLine, secondLine;
        switch (position) {
            case ROW_NUMBER_FIRST_HELP_ITEM:
                icon = R.drawable.ic_rostered_days_off_black_24dp;
                firstLine = "Help item 1";
                secondLine = "Description of this help item, which can be long and overflow onto multiple lines";
                break;
            case ROW_NUMBER_SECOND_HELP_ITEM:
                icon = R.drawable.ic_normal_day_white_24dp;
                firstLine = "Help item 2";
                secondLine = "Another help item, which has a floating action button as an icon and an even longer description";
                break;
            default:
                throw new IllegalStateException();
        }
        holder.imageView.setImageResource(icon);
        holder.primaryText.setText(firstLine);
        holder.secondaryText.setText(secondLine);
    }

    @Override
    public int getItemCount() {
        return ROW_COUNT;
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final TextView primaryText, secondaryText;
        @NonNull
        private final ImageView imageView;

        private ViewHolder(@NonNull ViewGroup parent, @LayoutRes int layout) {
            super(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
            primaryText = itemView.findViewById(R.id.primary_text);
            secondaryText = itemView.findViewById(R.id.secondary_text);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
