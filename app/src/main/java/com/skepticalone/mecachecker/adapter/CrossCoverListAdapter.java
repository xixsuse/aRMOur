package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

public class CrossCoverListAdapter extends PayableItemListAdapter<CrossCover> {

    public CrossCoverListAdapter(@NonNull Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    boolean areContentsTheSame(@NonNull CrossCover crossCover1, @NonNull CrossCover crossCover2) {
        return super.areContentsTheSame(crossCover1, crossCover2) &&
                Comparators.equalLocalDates(crossCover1.getDate(), crossCover2.getDate());
    }

    @Override
    String getViewHolderTitle(@NonNull CrossCover crossCover) {
        return DateTimeUtils.getFullDateString(crossCover.getDate());
    }

}
