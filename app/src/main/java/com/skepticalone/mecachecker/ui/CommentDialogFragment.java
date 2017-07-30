package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

abstract class CommentDialogFragment<Entity extends Item> extends PlainTextDialogFragment<Entity, ViewModelContract<Entity>> {

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
        getViewModel().saveNewComment(getCurrentItem().getId(), comment);
    }

}
