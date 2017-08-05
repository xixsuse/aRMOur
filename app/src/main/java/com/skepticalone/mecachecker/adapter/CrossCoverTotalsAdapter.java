package com.skepticalone.mecachecker.adapter;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.CrossCover;

public final class CrossCoverTotalsAdapter extends PayableTotalsAdapter<CrossCover> {

    private static final int
            ROW_NUMBER_TOTAL_NUMBER = 0,
            ROW_NUMBER_TOTAL_PAYMENT = 1,
            ROW_COUNT = 2;

    CrossCoverTotalsAdapter(Callbacks callbacks) {
        super(callbacks);
    }

    @Override
    int getRowNumberTotalNumber() {
        return ROW_NUMBER_TOTAL_NUMBER;
    }

    @Override
    int getRowNumberTotalPayment() {
        return ROW_NUMBER_TOTAL_PAYMENT;
    }

    @Override
    int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    int getTitle() {
        return R.string.cross_cover_shifts;
    }

}