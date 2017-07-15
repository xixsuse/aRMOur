package com.skepticalone.mecachecker.ui;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.PayableItemDetailAdapter;
import com.skepticalone.mecachecker.data.PayableItemViewModel;
import com.skepticalone.mecachecker.dialog.MoneyDialogFragment;
import com.skepticalone.mecachecker.model.PayableItem;

import java.math.BigDecimal;

abstract class PayableDetailFragment<ItemType extends PayableItem, Entity extends ItemType, ViewModel extends PayableItemViewModel<Entity>> extends DetailFragment<ItemType, Entity, ViewModel> implements PayableItemDetailAdapter.Callbacks, MoneyDialogFragment.Callbacks {

    @Override
    public final void changePayment(long itemId, @NonNull BigDecimal payment) {
        showDialogFragment(MoneyDialogFragment.newInstance(itemId, payment, R.string.payment));
    }

    @Override
    public final void saveMoney(long itemId, @NonNull BigDecimal payment) {
        getViewModel().setPayment(itemId, payment);
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
