package com.skepticalone.mecachecker.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.PayableViewModel;
import com.skepticalone.mecachecker.model.PayableItem;
import com.skepticalone.mecachecker.ui.adapter.PayableCallbacks;
import com.skepticalone.mecachecker.ui.components.MoneyDialogFragment;
import com.skepticalone.mecachecker.ui.components.PlainTextDialogFragment;

import java.math.BigDecimal;

abstract class SinglePaymentDetailFragment<ItemType extends PayableItem, Entity extends ItemType, ViewModel extends PayableViewModel<Entity>> extends DetailFragment<ItemType, Entity, ViewModel> implements PayableCallbacks, MoneyDialogFragment.Callbacks, PlainTextDialogFragment.Callbacks {

    @Override
    public final void changePayment(long itemId, @NonNull BigDecimal payment) {
        showDialogFragment(MoneyDialogFragment.newInstance(itemId, payment, R.string.payment));
    }

    @Override
    public void saveMoney(long itemId, @NonNull BigDecimal payment) {
        getViewModel().setPayment(itemId, payment);
    }

    final void changeString(long itemId, @Nullable String currentString, @StringRes int title, boolean isTitle) {
        showDialogFragment(PlainTextDialogFragment.newInstance(itemId, currentString, title, isTitle));
    }

    @Override
    public final void changeComment(long itemId, @Nullable String currentComment) {
        changeString(itemId, currentComment, R.string.comment, false);
    }

    @Override
    @CallSuper
    public void savePlainText(long itemId, @Nullable String trimmedTextWithLength, boolean isTitle) {
        if (!isTitle) {
            getViewModel().setComment(itemId, trimmedTextWithLength);
        }
    }

    @Override
    public final void setClaimed(long itemId, boolean claimed) {
        getViewModel().setClaimed(itemId, claimed);
    }

    @Override
    public final void setPaid(long itemId, boolean paid) {
        getViewModel().setPaid(itemId, paid);
    }
}
