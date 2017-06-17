package com.skepticalone.mecachecker.composition;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.behaviours.DetailFragmentBehaviour;
import com.skepticalone.mecachecker.behaviours.WithTitle;
import com.skepticalone.mecachecker.components.ListItemViewHolder;
import com.skepticalone.mecachecker.dialog.PlainTextDialogFragment;
import com.skepticalone.mecachecker.util.LifecycleConstants;

public class TitleData extends AbstractData {

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

    public TitleData(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void readFromPositionedCursor(@NonNull Cursor cursor) {
        mTitle = cursor.getString(mCallbacks.getColumnIndexTitle());
    }

    @Override
    public boolean bindToHolder(Context context, ListItemViewHolder holder, int position) {
        if (position == mCallbacks.getRowNumberTitle()) {
            holder.bindPlain(R.drawable.ic_title_black_24dp, context.getString(R.string.title), mTitle, null, 0);
            holder.itemView.setOnClickListener(mTitleListener);
        } else return false;
        return true;
    }

    public interface Callbacks extends DetailFragmentBehaviour, WithTitle {
    }
}
