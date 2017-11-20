package com.skepticalone.armour.help;

import android.support.v7.widget.RecyclerView;

import com.skepticalone.armour.R;
import com.skepticalone.armour.util.AppConstants;

final class RosteredShiftHelpAdapter extends HelpAdapter {

    @Override
    public int getItemViewType(int position) {
        switch (POSITIONS.values()[position]) {
            case SHIFTS_TITLE:
            case COMPLIANCE_TITLE:
            case ADD_TITLE:
                return ITEM_TYPE_TITLE;
            case SHIFTS_NORMAL_DAY:
            case SHIFTS_LONG_DAY:
            case SHIFTS_NIGHT_SHIFT:
            case SHIFTS_CUSTOM_SHIFT:
            case COMPLIANCE_COMPLIANT:
            case COMPLIANCE_NON_COMPLIANT:
                return ITEM_TYPE_LABEL_PLAIN;
            case ADD_NORMAL_DAY:
            case ADD_LONG_DAY:
            case ADD_NIGHT_SHIFT:
                return ITEM_TYPE_LABEL_FAB;
            case SHIFTS_DESCRIPTION:
            case COMPLIANCE_DESCRIPTION_1:
            case COMPLIANCE_DESCRIPTION_2:
            case ADD_DESCRIPTION_1:
            case ADD_DESCRIPTION_2:
                return ITEM_TYPE_DESCRIPTION;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (POSITIONS.values()[position]) {
            case SHIFTS_TITLE:
                ((TextViewHolder) holder).bind(R.string.help_shift_type_title);
                break;
            case SHIFTS_NORMAL_DAY:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.ic_normal_day_black_24dp, R.string.normal_day);
                break;
            case SHIFTS_LONG_DAY:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.ic_long_day_black_24dp, R.string.long_day);
                break;
            case SHIFTS_NIGHT_SHIFT:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.ic_night_shift_black_24dp, R.string.night_shift);
                break;
            case SHIFTS_CUSTOM_SHIFT:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.ic_custom_shift_black_24dp, R.string.custom_shift);
                break;
            case SHIFTS_DESCRIPTION:
                ((TextViewHolder) holder).bind(R.string.help_shift_type_description);
                break;
            case COMPLIANCE_TITLE:
                ((TextViewHolder) holder).bind(R.string.help_compliance_title);
                break;
            case COMPLIANCE_COMPLIANT:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.compliant_black_24dp, R.string.help_compliance_compliant_label);
                break;
            case COMPLIANCE_NON_COMPLIANT:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.non_compliant_red_24dp, R.string.help_compliance_non_compliant_label);
                break;
            case COMPLIANCE_DESCRIPTION_1:
                ((TextViewHolder) holder).bind(R.string.help_compliance_description_1);
                break;
            case COMPLIANCE_DESCRIPTION_2:
                ((TextViewHolder) holder).bind(R.string.help_compliance_description_2);
                break;
            case ADD_TITLE:
                ((TextViewHolder) holder).bind(R.string.new_shift);
                break;
            case ADD_NORMAL_DAY:
                ((LabelViewHolder.Fab) holder).bind(R.drawable.ic_normal_day_white_24dp, R.string.help_add_normal_day_label);
                break;
            case ADD_LONG_DAY:
                ((LabelViewHolder.Fab) holder).bind(R.drawable.ic_long_day_white_24dp, R.string.help_add_long_day_label);
                break;
            case ADD_NIGHT_SHIFT:
                ((LabelViewHolder.Fab) holder).bind(R.drawable.ic_night_shift_white_24dp, R.string.help_add_night_shift_label);
                break;
            case ADD_DESCRIPTION_1:
                ((TextViewHolder) holder).bind(holder.itemView.getContext().getString(R.string.help_add_rostered_shift_description_1, holder.itemView.getContext().getResources().getQuantityString(R.plurals.hours, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS, AppConstants.MINIMUM_HOURS_BETWEEN_SHIFTS)));
                break;
            case ADD_DESCRIPTION_2:
                ((TextViewHolder) holder).bind(R.string.help_add_rostered_shift_description_2);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public boolean shouldHaveDividerBelow(int position) {
        switch (POSITIONS.values()[position]) {
            case SHIFTS_DESCRIPTION:
            case COMPLIANCE_DESCRIPTION_2:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getItemCount() {
        return POSITIONS.values().length;
    }

    enum POSITIONS {
        SHIFTS_TITLE,
        SHIFTS_NORMAL_DAY,
        SHIFTS_LONG_DAY,
        SHIFTS_NIGHT_SHIFT,
        SHIFTS_CUSTOM_SHIFT,
        SHIFTS_DESCRIPTION,

        COMPLIANCE_TITLE,
        COMPLIANCE_COMPLIANT,
        COMPLIANCE_NON_COMPLIANT,
        COMPLIANCE_DESCRIPTION_1,
        COMPLIANCE_DESCRIPTION_2,

        ADD_TITLE,
        ADD_NORMAL_DAY,
        ADD_LONG_DAY,
        ADD_NIGHT_SHIFT,
        ADD_DESCRIPTION_1,
        ADD_DESCRIPTION_2,

    }
}
