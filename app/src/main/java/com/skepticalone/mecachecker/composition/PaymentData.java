package com.skepticalone.mecachecker.composition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.InvolvesPayment;
import com.skepticalone.mecachecker.components.ListItemViewHolder;
import com.skepticalone.mecachecker.dialog.MoneyDialogFragment;
import com.skepticalone.mecachecker.dialog.PlainTextDialogFragment;
import com.skepticalone.mecachecker.util.LifecycleConstants;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class PaymentData extends AbstractData implements ListItemViewHolder.SwitchCallbacks {

    private final Callbacks mCallbacks;
    private BigDecimal mPayment;
    @NonNull
    private final View.OnClickListener
            mPaymentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(
                    MoneyDialogFragment.newInstance(mPayment, mCallbacks.getMoneyDescriptor(), mCallbacks.getUpdateContentUri(), mCallbacks.getColumnNameMoney()),
                    LifecycleConstants.MONEY_DIALOG
            );
        }
    },
            mCommentListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.showDialogFragment(
                            PlainTextDialogFragment.newInstance(mComment, R.string.comment, mCallbacks.getUpdateContentUri(), mCallbacks.getColumnNameComment()),
                            LifecycleConstants.COMMENT_DIALOG
                    );
                }
            };
    @Nullable
    private String mComment;
    @Nullable
    private DateTime mClaimed, mPaid;

    public PaymentData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public boolean isClaimed() {
        return mClaimed != null;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mPayment = BigDecimal.valueOf(cursor.getInt(mCallbacks.getColumnIndexMoney()), 2);
        mComment = cursor.isNull(mCallbacks.getColumnIndexComment()) ? null : cursor.getString(mCallbacks.getColumnIndexComment());
        mClaimed = cursor.isNull(mCallbacks.getColumnIndexClaimed()) ? null : new DateTime(cursor.getLong(mCallbacks.getColumnIndexClaimed()));
        mPaid = (!isClaimed() || cursor.isNull(mCallbacks.getColumnIndexPaid())) ? null : new DateTime(cursor.getLong(mCallbacks.getColumnIndexPaid()));
    }

    @Override
    public boolean bindToHolder(Context context, ListItemViewHolder holder, int position) {
        View.OnClickListener listener = null;
        if (position == mCallbacks.getRowNumberMoney()) {
            holder.bindPlain(mCallbacks.getMoneyIcon(), context.getString(mCallbacks.getMoneyDescriptor()), context.getString(R.string.currency_format, mPayment), null, 0);
            listener = mPaymentListener;
        } else if (position == mCallbacks.getRowNumberComment()) {
            holder.bindPlain(R.drawable.ic_comment_black_24dp, context.getString(R.string.comment), mComment, null, 0);
            listener = mCommentListener;
        } else if (position == mCallbacks.getRowNumberClaimed()) {
            holder.bindClaimed(context, mClaimed, this, mPaid != null);
        } else if (position == mCallbacks.getRowNumberPaid()) {
            holder.bindPaid(context, mPaid, this);
        } else return false;
        holder.itemView.setOnClickListener(listener);
        return true;
    }

    @Override
    public void onCheckedChanged(ListItemViewHolder.SwitchType switchType, boolean isChecked) {
        String columnName;
        switch (switchType) {
            case PAID:
                columnName = mCallbacks.getColumnNamePaid();
                break;
            case CLAIMED:
                columnName = mCallbacks.getColumnNameClaimed();
                break;
            default:
                throw new IllegalStateException();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(columnName, isChecked ? System.currentTimeMillis() : null);
        mCallbacks.update(contentValues);
    }

    public interface Callbacks extends DetailFragmentBehaviour, InvolvesPayment {
    }
}