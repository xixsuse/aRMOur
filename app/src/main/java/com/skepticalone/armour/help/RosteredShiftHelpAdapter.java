package com.skepticalone.armour.help;

import android.support.v7.widget.RecyclerView;

import com.skepticalone.armour.R;

final class RosteredShiftHelpAdapter extends HelpAdapter {

    private final static int
            POSITION_SHIFTS_TITLE = 0,
            POSITION_SHIFTS_NORMAL_DAY = 1,
            POSITION_SHIFTS_LONG_DAY = 2,
            POSITION_SHIFTS_NIGHT_SHIFT = 3,
            POSITION_SHIFTS_CUSTOM_SHIFT = 4,
            POSITION_SHIFTS_DESCRIPTION = 5,
            ROW_COUNT = 36;

    @Override
    public int getItemViewType(int position) {
        switch (position % 6) {
            case POSITION_SHIFTS_TITLE:
                return ITEM_TYPE_TITLE;
            case POSITION_SHIFTS_NORMAL_DAY:
            case POSITION_SHIFTS_LONG_DAY:
            case POSITION_SHIFTS_NIGHT_SHIFT:
            case POSITION_SHIFTS_CUSTOM_SHIFT:
                return ITEM_TYPE_LABEL;
            case POSITION_SHIFTS_DESCRIPTION:
                return ITEM_TYPE_DESCRIPTION;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position % 6) {
            case POSITION_SHIFTS_TITLE:
                ((TextViewHolder) holder).bind(R.string.help_shift_type_title);
                break;
            case POSITION_SHIFTS_NORMAL_DAY:
                ((LabelViewHolder) holder).bind(false, R.drawable.ic_normal_day_black_24dp, R.string.normal_day);
                break;
            case POSITION_SHIFTS_LONG_DAY:
                ((LabelViewHolder) holder).bind(false, R.drawable.ic_long_day_black_24dp, R.string.long_day);
                break;
            case POSITION_SHIFTS_NIGHT_SHIFT:
                ((LabelViewHolder) holder).bind(false, R.drawable.ic_night_shift_black_24dp, R.string.night_shift);
                break;
            case POSITION_SHIFTS_CUSTOM_SHIFT:
                ((LabelViewHolder) holder).bind(false, R.drawable.ic_custom_shift_black_24dp, R.string.custom_shift);
                break;
            case POSITION_SHIFTS_DESCRIPTION:
                ((TextViewHolder) holder).bind(R.string.help_shift_type_description);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int getItemCount() {
        return ROW_COUNT;
    }
}
