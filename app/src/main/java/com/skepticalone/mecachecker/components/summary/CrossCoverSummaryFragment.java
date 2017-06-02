package com.skepticalone.mecachecker.components.summary;

import android.net.Uri;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

public class CrossCoverSummaryFragment extends AbstractSinglePaymentSummaryFragment {

    @StringRes
    static final int tabTitle = R.string.cross_cover;

    @Override
    int getLoaderId() {
        return SummaryActivity.LOADER_ID_CROSS_COVER;
    }

    @Override
    int getItemsTitle() {
        return R.string.cross_cover_shifts;
    }

    @Override
    Uri getContentUri() {
        return Provider.crossCoverShiftsUri;
    }

    @Override
    String getColumnNamePayment() {
        return Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT;
    }

    @Override
    String getColumnNameClaimed() {
        return Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED;
    }

    @Override
    String getColumnNamePaid() {
        return Contract.CrossCoverShifts.COLUMN_NAME_PAID;
    }

}