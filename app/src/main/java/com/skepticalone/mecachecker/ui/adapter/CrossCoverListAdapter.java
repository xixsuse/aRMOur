package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

public class CrossCoverListAdapter extends ItemListAdapter<CrossCover> {

    public CrossCoverListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull CrossCover crossCover1, @NonNull CrossCover crossCover2) {
        return
                Comparators.equalLocalDates(crossCover1.getDate(), crossCover2.getDate()) &&
                        Comparators.equalStrings(crossCover1.getComment(), crossCover2.getComment()) &&
                        Comparators.equalBigDecimals(crossCover1.getPayment(), crossCover2.getPayment()) &&
                        Comparators.equalDateTimes(crossCover1.getClaimed(), crossCover2.getClaimed()) &&
                        Comparators.equalDateTimes(crossCover1.getPaid(), crossCover2.getPaid())
                ;
    }

    @Override
    void bindViewHolder(@NonNull CrossCover crossCover, ItemViewHolder holder) {
        holder.setText(DateTimeUtils.getFullDateString(crossCover.getDate()), crossCover.getComment());
        holder.secondaryIcon.setImageResource(crossCover.getPaid() == null ? crossCover.getClaimed() == null ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp);
    }
}
