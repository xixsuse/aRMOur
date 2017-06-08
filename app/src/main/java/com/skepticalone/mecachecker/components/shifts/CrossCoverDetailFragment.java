package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class CrossCoverDetailFragment extends DetailFragment {

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

    private boolean mLoaded = false;
    private DateTime mDate;
    private BigDecimal mPayment;
    @Nullable
    private String mComment;
    @Nullable
    private DateTime mClaimed, mPaid;

    public CrossCoverDetailFragment() {
    }

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
    Uri getContentUri() {
        return Provider.crossCoverShiftUri(getItemId());
    }

    @Override
    String getColumnNameClaimed() {
        return Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED;
    }

    @Override
    String getColumnNamePaid() {
        return Contract.CrossCoverShifts.COLUMN_NAME_PAID;
    }

    @Nullable
    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @Override
    void useCursor(@Nullable Cursor cursor) {
        mLoaded = cursor != null && cursor.moveToFirst();
        if (mLoaded) {
            mDate = new DateTime(cursor.getLong(COLUMN_INDEX_DATE));
            mPayment = BigDecimal.valueOf(cursor.getInt(COLUMN_INDEX_PAYMENT), 2);
            mComment = cursor.isNull(COLUMN_INDEX_COMMENT) ? null : cursor.getString(COLUMN_INDEX_COMMENT);
            mClaimed = cursor.isNull(COLUMN_INDEX_CLAIMED) ? null : new DateTime(cursor.getLong(COLUMN_INDEX_CLAIMED));
            mPaid = (mClaimed == null || cursor.isNull(COLUMN_INDEX_PAID)) ? null : new DateTime(cursor.getLong(COLUMN_INDEX_PAID));
        }
    }

    private void update(String column, boolean isChecked) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, isChecked ? System.currentTimeMillis() : null);
        getActivity().getContentResolver().update(getContentUri(), contentValues, null, null);
    }

    @Override
    int getLoaderId() {
        return MainActivity.LOADER_ID_CROSS_COVER_DETAIL;
    }

    private void onBindViewHolder(ListItemViewHolder holder, @DrawableRes int primaryIcon, @StringRes int key, @Nullable String value) {
        holder.primaryIcon.setImageResource(primaryIcon);
        holder.setText(getString(key), value);
    }

    @Override
    void onBindPlainViewHolder(PlainListItemViewHolder holder, int position) {
        int primaryIcon, key;
        String value;
        switch (position) {
            case ROW_NUMBER_DATE:
                primaryIcon = R.drawable.ic_calendar_black_24dp;
                key = R.string.date;
                value = DateTimeUtils.getFullDateString(mDate);
                break;
            case ROW_NUMBER_PAYMENT:
                primaryIcon = R.drawable.ic_dollar_black_24dp;
                key = R.string.payment;
                value = getString(R.string.currency_format, mPayment);
                break;
            case ROW_NUMBER_COMMENT:
                primaryIcon = R.drawable.ic_comment_black_24dp;
                key = R.string.comment;
                value = mComment;
                break;
            default:
                throw new IllegalStateException();
        }
        onBindViewHolder(holder, primaryIcon, key, value);
    }

    @Override
    void onBindSwitchViewHolder(SwitchListItemViewHolder holder, int position) {
        switch (position) {
            case ROW_NUMBER_CLAIMED:
                holder.bindClaimed(getActivity(), mClaimed, mPaid == null);
                return;
            case ROW_NUMBER_PAID:
                holder.bindPaid(getActivity(), mPaid);
                return;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    boolean isSwitchType(int position) {
        switch (position) {
            case ROW_NUMBER_DATE:
            case ROW_NUMBER_PAYMENT:
            case ROW_NUMBER_COMMENT:
                return false;
            case ROW_NUMBER_CLAIMED:
            case ROW_NUMBER_PAID:
                return true;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    int getItemCount() {
        return mLoaded ? ROW_COUNT - (mClaimed == null ? 1 : 0) : 0;
    }

}
