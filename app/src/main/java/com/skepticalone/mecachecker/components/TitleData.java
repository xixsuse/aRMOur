package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.WithTitle;

class TitleData extends AbstractData {

    private final Callbacks mCallbacks;
    private String mTitle;
    @NonNull
    private final View.OnClickListener mTitleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallbacks.showDialogFragment(
                    PlainTextDialogFragment.newInstance(mTitle, R.string.title, mCallbacks.getUpdateContentUri(), mCallbacks.getColumnNameTitle()),
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

    @Nullable
    @Override
    ViewHolderType getViewHolderType(int position) {
        if (position == mCallbacks.getColumnIndexTitle()) {
            return ViewHolderType.PLAIN;
        } else {
            return null;
        }
    }

    @Override
    public boolean bindToHolder(Context context, PlainListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberTitle()) {
            holder.rootBind(context, R.drawable.ic_title_black_24dp, R.string.title, mTitle, mTitleListener);
        } else return false;
        return true;
    }

    interface Callbacks extends DetailFragmentBehaviour, WithTitle {
    }
}
