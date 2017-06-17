package com.skepticalone.mecachecker.components;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.composition.AbstractData;
import com.skepticalone.mecachecker.composition.DateData;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.util.LifecycleConstants;

public class CrossCoverDetailFragment extends SinglePaymentItemDetailFragment implements DateData.Callbacks {

    private static final String[] PROJECTION = {
            Contract.CrossCoverShifts.COLUMN_NAME_DATE,
            Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT,
            Contract.CrossCoverShifts.COLUMN_NAME_COMMENT,
            Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED,
            Contract.CrossCoverShifts.COLUMN_NAME_PAID
    };
    private static final int
            COLUMN_INDEX_DATE = 0,
            COLUMN_INDEX_PAYMENT = 1,
            COLUMN_INDEX_COMMENT = 2,
            COLUMN_INDEX_CLAIMED = 3,
            COLUMN_INDEX_PAID = 4,
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private final DateData mDateData = new DateData(this);

    @NonNull
    @Override
    AbstractData getData() {
        return mDateData;
    }

    @Override
    int getTitle() {
        return R.string.cross_cover;
    }

    @Override
    int getLoaderId() {
        return LifecycleConstants.LOADER_ID_CROSS_COVER_DETAIL;
    }

    @Override
    public Uri getReadContentUri() {
        return Provider.uriWithId(Provider.crossCoverShiftsUri, getItemId());
    }

    @Nullable
    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @NonNull
    @Override
    public String getColumnNameStartOrDate() {
        return Contract.CrossCoverShifts.COLUMN_NAME_DATE;
    }

    @NonNull
    @Override
    public String getColumnNameMoney() {
        return Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT;
    }

    @NonNull
    @Override
    public String getColumnNameComment() {
        return Contract.CrossCoverShifts.COLUMN_NAME_COMMENT;
    }

    @NonNull
    @Override
    public String getColumnNameClaimed() {
        return Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED;
    }

    @NonNull
    @Override
    public String getColumnNamePaid() {
        return Contract.CrossCoverShifts.COLUMN_NAME_PAID;
    }

    @Override
    public int getColumnIndexStartOrDate() {
        return COLUMN_INDEX_DATE;
    }

    @Override
    public int getColumnIndexMoney() {
        return COLUMN_INDEX_PAYMENT;
    }

    @Override
    public int getColumnIndexComment() {
        return COLUMN_INDEX_COMMENT;
    }

    @Override
    public int getColumnIndexClaimed() {
        return COLUMN_INDEX_CLAIMED;
    }

    @Override
    public int getColumnIndexPaid() {
        return COLUMN_INDEX_PAID;
    }

    @Override
    public int getRowNumberDate() {
        return ROW_NUMBER_DATE;
    }

    @Override
    public int getRowNumberMoney() {
        return ROW_NUMBER_PAYMENT;
    }

    @Override
    public int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    public int getRowNumberClaimed() {
        return ROW_NUMBER_CLAIMED;
    }

    @Override
    int getRowNumberPaidIfClaimed() {
        return ROW_NUMBER_PAID;
    }

    @Override
    int getRowCountIfClaimed() {
        return ROW_COUNT;
    }

}
