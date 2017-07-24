package com.skepticalone.mecachecker.dialog;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.BaseViewModel;

public abstract class IndependentCommentDialogFragment<Entity extends Item> extends IndependentPlainTextDialogFragment<Entity, BaseViewModel<Entity>> {

    @Override
    final int getTitle() {
        return R.string.comment;
    }

    @Override
    final int getHint() {
        return super.getHint();
    }

    @Nullable
    @Override
    final String getTextForDisplay(@NonNull Entity item) {
        return item.getComment();
    }

    @Override
    final void saveText(@Nullable String comment) {
        getViewModel().saveNewComment(comment);
    }

}
