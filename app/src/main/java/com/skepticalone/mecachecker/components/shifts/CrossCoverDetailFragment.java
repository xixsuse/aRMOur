package com.skepticalone.mecachecker.components.shifts;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

public class CrossCoverDetailFragment extends SinglePaymentItemDetailFragment {

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
            COLUMN_INDEX_PAID = 4;
    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private static final String DATE_DIALOG = "DATE_DIALOG";

    private DateTime mDate;

    private final View.OnClickListener mDateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MidnightDatePickerDialogFragment.newInstance(mDate.toLocalDate(), getContentUri(), Contract.CrossCoverShifts.COLUMN_NAME_DATE)
                    .show(getFragmentManager(), DATE_DIALOG);
        }
    };

    static CrossCoverDetailFragment create(long id) {
        CrossCoverDetailFragment fragment = new CrossCoverDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }


    @Override
    int getTitle() {
        return R.string.cross_cover;
    }

    @Override
    int getLoaderId() {
        return MainActivity.LOADER_ID_CROSS_COVER_DETAIL;
    }

    @Override
    Uri getContentUri() {
        return Provider.crossCoverShiftUri(getItemId());
    }

    @Nullable
    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @NonNull
    @Override
    String getColumnNamePayment() {
        return Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT;
    }

    @NonNull
    @Override
    String getColumnNameComment() {
        return Contract.CrossCoverShifts.COLUMN_NAME_COMMENT;
    }

    @NonNull
    @Override
    String getColumnNameClaimed() {
        return Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED;
    }

    @NonNull
    @Override
    String getColumnNamePaid() {
        return Contract.CrossCoverShifts.COLUMN_NAME_PAID;
    }

    @Override
    int getRowNumberPayment() {
        return ROW_NUMBER_PAYMENT;
    }

    @Override
    int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    int getRowNumberClaimed() {
        return ROW_NUMBER_CLAIMED;
    }

    @Override
    int getRowNumberPaid() {
        return ROW_NUMBER_PAID;
    }

    @Override
    int getRowCountIfLoaded() {
        return ROW_COUNT;
    }

    @Override
    int getColumnIndexPayment() {
        return COLUMN_INDEX_PAYMENT;
    }

    @Override
    int getColumnIndexComment() {
        return COLUMN_INDEX_COMMENT;
    }

    @Override
    int getColumnIndexClaimed() {
        return COLUMN_INDEX_CLAIMED;
    }

    @Override
    int getColumnIndexPaid() {
        return COLUMN_INDEX_PAID;
    }

    @Override
    void readFromPositionedCursor(@NonNull Cursor cursor) {
        super.readFromPositionedCursor(cursor);
        mDate = new DateTime(cursor.getLong(COLUMN_INDEX_DATE));
    }

    @Override
    void onBindViewHolder(PlainListItemViewHolder holder, int position) {
        if (position == ROW_NUMBER_DATE) {
            onBindViewHolder(holder, R.drawable.ic_calendar_black_24dp, R.string.date, DateTimeUtils.getFullDateString(mDate), mDateListener);
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    boolean isSwitchType(int position) {
        return position != ROW_NUMBER_DATE && super.isSwitchType(position);
    }

}
