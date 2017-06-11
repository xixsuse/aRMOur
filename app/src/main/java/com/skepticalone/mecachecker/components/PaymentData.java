package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;

import org.joda.time.DateTime;

import java.math.BigDecimal;

final class PaymentData extends AbstractData implements SwitchListItemViewHolder.Callbacks {

    private final Callbacks mCallbacks;

    private BigDecimal mPayment;
    private final View.OnClickListener
            mPaymentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(
                    MoneyDialogFragment.newInstance(mPayment, mCallbacks.getMoneyTitle(), mCallbacks.getContentUri(), mCallbacks.getColumnNameMoney()),
                    LifecycleConstants.MONEY_DIALOG
            );
        }
    },
            mCommentListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.showDialogFragment(
                            PlainTextDialogFragment.newInstance(mComment, R.string.comment, mCallbacks.getContentUri(), mCallbacks.getColumnNameComment()),
                            LifecycleConstants.COMMENT_DIALOG
                    );
                }
            };
    @Nullable
    private String mComment;
    @Nullable
    private DateTime mClaimed, mPaid;

    PaymentData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mPayment = BigDecimal.valueOf(cursor.getInt(mCallbacks.getColumnIndexMoney()), 2);
        mComment = cursor.isNull(mCallbacks.getColumnIndexComment()) ? null : cursor.getString(mCallbacks.getColumnIndexComment());
        mClaimed = cursor.isNull(mCallbacks.getColumnIndexClaimed()) ? null : new DateTime(cursor.getLong(mCallbacks.getColumnIndexClaimed()));
        mPaid = (mClaimed == null || cursor.isNull(mCallbacks.getColumnIndexPaid())) ? null : new DateTime(cursor.getLong(mCallbacks.getColumnIndexPaid()));
    }

    int getAdjustedRowCount(int rowCountIfPaid) {
        if (mClaimed == null) rowCountIfPaid--;
        return rowCountIfPaid;
    }

    @Override
    boolean isSwitchType(int position) throws IllegalStateException {
        if (position == mCallbacks.getRowNumberMoney() || position == mCallbacks.getRowNumberComment()) {
            return false;
        } else if (position == mCallbacks.getRowNumberClaimed() || position == mCallbacks.getRowNumberPaid()) {
            return true;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberMoney()) {
            holder.bind(context, mCallbacks.getMoneyIcon(), mCallbacks.getMoneyTitle(), context.getString(R.string.currency_format, mPayment), mPaymentListener);
        } else if (position == mCallbacks.getRowNumberComment()) {
            holder.bind(context, R.drawable.ic_comment_black_24dp, R.string.comment, mComment, mCommentListener);
        } else return false;
        return true;
    }

    @Override
    boolean bindToHolder(Context context, SwitchListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberClaimed()) {
            holder.bindClaimed(context, mClaimed, mPaid == null);
        } else if (position == mCallbacks.getRowNumberPaid()) {
            holder.bindPaid(context, mPaid);
        } else return false;
        return true;
    }

    @Override
    public void onCheckedChanged(boolean isPaidSwitch, boolean isChecked) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(isPaidSwitch ? mCallbacks.getColumnNamePaid() : mCallbacks.getColumnNameClaimed(), isChecked ? System.currentTimeMillis() : null);
        mCallbacks.update(contentValues);
    }

    interface Callbacks extends Payment, CanUpdate, ShowsDialog {
    }
}