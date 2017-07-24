package com.skepticalone.mecachecker.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Item;

public abstract class IndependentCommentDialogFragment<ItemType extends Item> extends IndependentPlainTextDialogFragment<ItemType, BaseViewModel<ItemType>> {

    @Override
    final int getTitle() {
        return R.string.title;
    }

    @Override
    final int getHint() {
        return super.getHint();
    }

    @Override
    final void onCurrentItemChanged(@NonNull ItemType item) {
        String comment = item.getComment();
        if (comment != null) setText(comment);
    }

    @Override
    final void saveText(@Nullable String comment) {
        getViewModel().saveNewComment(comment);
    }

}
