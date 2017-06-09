package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import com.skepticalone.mecachecker.R;

import org.joda.time.DateTime;

import java.math.BigDecimal;

abstract class SinglePaymentItemDetailFragment extends DetailFragment {

    private static final String PAYMENT_DIALOG = "PAYMENT_DIALOG";
    private static final String COMMENT_DIALOG = "COMMENT_DIALOG";

    private boolean mLoaded = false;
    private BigDecimal mPayment;
    private final View.OnClickListener
            mPaymentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MoneyDialogFragment.newInstance(mPayment, R.string.payment, getContentUri(), getColumnNamePayment())
                    .show(getFragmentManager(), PAYMENT_DIALOG);
        }
    },
            mCommentListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlainTextDialogFragment.newInstance(mComment, R.string.comment, getContentUri(), getColumnNameComment())
                            .show(getFragmentManager(), COMMENT_DIALOG);
                }
            };
    @Nullable
    private String mComment;
    @Nullable
    private DateTime mClaimed, mPaid;

    @NonNull
    abstract String getColumnNamePayment();

    @NonNull
    abstract String getColumnNameComment();

    abstract int getRowNumberPayment();

    abstract int getRowNumberComment();

    abstract int getRowNumberClaimed();

    abstract int getRowNumberPaid();

    abstract int getRowCountIfLoaded();

    abstract int getColumnIndexPayment();

    abstract int getColumnIndexComment();

    abstract int getColumnIndexClaimed();

    abstract int getColumnIndexPaid();

    void readFromPositionedCursor(@NonNull Cursor cursor) {
        mPayment = BigDecimal.valueOf(cursor.getInt(getColumnIndexPayment()), 2);
        mComment = cursor.isNull(getColumnIndexComment()) ? null : cursor.getString(getColumnIndexComment());
        mClaimed = cursor.isNull(getColumnIndexClaimed()) ? null : new DateTime(cursor.getLong(getColumnIndexClaimed()));
        mPaid = (mClaimed == null || cursor.isNull(getColumnIndexPaid())) ? null : new DateTime(cursor.getLong(getColumnIndexPaid()));
    }

    @Override
    final void useCursor(@Nullable Cursor cursor) {
        mLoaded = cursor != null && cursor.moveToFirst();
        if (mLoaded) {
            readFromPositionedCursor(cursor);
        }
    }

    private void update(String column, boolean isChecked) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, isChecked ? System.currentTimeMillis() : null);
        getActivity().getContentResolver().update(getContentUri(), contentValues, null, null);
    }

    @Override
    void onViewHolderCreated(PlainListItemViewHolder holder) {
        holder.secondaryIcon.setVisibility(View.GONE);
    }

    void onBindViewHolder(ListItemViewHolder holder, @DrawableRes int primaryIcon, @StringRes int key, @Nullable String value, @Nullable View.OnClickListener listener) {
        holder.primaryIcon.setImageResource(primaryIcon);
        holder.setText(getString(key), value);
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    void onBindViewHolder(PlainListItemViewHolder holder, int position) {
        if (position == getRowNumberPayment()) {
            onBindViewHolder(holder, R.drawable.ic_dollar_black_24dp, R.string.payment, getString(R.string.currency_format, mPayment), mPaymentListener);
        } else if (position == getRowNumberComment()) {
            onBindViewHolder(holder, R.drawable.ic_comment_black_24dp, R.string.comment, mComment, mCommentListener);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    void onBindViewHolder(SwitchListItemViewHolder holder, int position) {
        if (position == getRowNumberClaimed()) {
            holder.bindClaimed(getActivity(), mClaimed, mPaid == null);
        } else if (position == getRowNumberPaid()) {
            holder.bindPaid(getActivity(), mPaid);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    boolean isSwitchType(int position) {
        if (position == getRowNumberClaimed() || position == getRowNumberPaid()) {
            return true;
        } else if (position == getRowNumberPayment() || position == getRowNumberComment()) {
            return false;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    int getItemCount() {
        if (!mLoaded) return 0;
        int count = getRowCountIfLoaded();
        if (mClaimed == null) count--;
        return count;
    }

}
