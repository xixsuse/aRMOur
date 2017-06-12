package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;

class TitleData extends AbstractData {

    private final Callbacks mCallbacks;
    private String mTitle;
    private final View.OnClickListener mTitleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(
                    PlainTextDialogFragment.newInstance(mTitle, R.string.title, mCallbacks.getContentUri(), mCallbacks.getColumnNameTitle()),
                    LifecycleConstants.TITLE_DIALOG
            );
        }
    };

    TitleData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mTitle = cursor.getString(mCallbacks.getColumnIndexTitle());
    }

    @Override
    boolean isSwitchType(int position) throws IllegalStateException {
        if (position == mCallbacks.getColumnIndexTitle()) {
            return false;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberTitle()) {
            holder.bind(context, R.drawable.ic_title_black_24dp, R.string.title, mTitle, mTitleListener);
        } else return false;
        return true;
    }

    interface Callbacks extends HasTitle, HasContentUri, ShowsDialog {
    }
}
