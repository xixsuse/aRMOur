package com.skepticalone.mecachecker.adapter;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.CrossCoverEntity;

public final class CrossCoverTotalsAdapter extends PayableTotalsAdapter<CrossCoverEntity> {

    private static final int
            ROW_NUMBER_TOTAL_NUMBER = 0,
            ROW_NUMBER_TOTAL_PAYMENT = 1,
            ROW_COUNT = 2;

    public CrossCoverTotalsAdapter(Callbacks callbacks) {
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
    int getTotalNumberTitle() {
        return R.string.cross_cover_shifts;
    }

}