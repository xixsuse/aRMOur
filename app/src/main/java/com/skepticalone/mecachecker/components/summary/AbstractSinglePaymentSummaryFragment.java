package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.support.annotation.NonNull;

import java.math.BigDecimal;

public abstract class AbstractSinglePaymentSummaryFragment extends AbstractClaimableSummaryFragment {
    private static final int
            COLUMN_INDEX_PAYMENT = 0,
            COLUMN_INDEX_CLAIMED = 1,
            COLUMN_INDEX_PAID = 2;

    private final String[] PROJECTION = {
            getColumnNamePayment(),
            getColumnNameClaimed(),
            getColumnNamePaid()
    };

    abstract String getColumnNamePayment();

    abstract String getColumnNameClaimed();

    abstract String getColumnNamePaid();

    @Override
    final String[] getProjection() {
        return PROJECTION;
    }

    @Override
    final int getColumnIndexClaimed() {
        return COLUMN_INDEX_CLAIMED;
    }

    @Override
    final int getColumnIndexPaid() {
        return COLUMN_INDEX_PAID;
    }

    @Override
    final BigDecimal getCurrentSum(@NonNull Cursor cursor) {
        return BigDecimal.valueOf(cursor.getInt(COLUMN_INDEX_PAYMENT), 2);
    }
}
