package com.skepticalone.mecachecker.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.dao.PayableDaoContract;
import com.skepticalone.mecachecker.data.model.PayableItem;

import org.joda.time.DateTime;

import java.math.BigDecimal;

final class PayableHelper {

    @NonNull
    private final PayableDaoContract dao;

    PayableHelper(@NonNull PayableDaoContract dao) {
        this.dao = dao;
    }

    void saveNewPayment(@NonNull LiveData<? extends PayableItem> currentItem, @NonNull BigDecimal payment) {
        PayableItem item = currentItem.getValue();
        if (item != null) {
            dao.setPaymentSync(item.getId(), payment);
        }
    }

    void setClaimed(@NonNull LiveData<? extends PayableItem> currentItem, boolean claimed) {
        PayableItem item = currentItem.getValue();
        if (item != null) {
            dao.setClaimedSync(item.getId(), claimed ? DateTime.now() : null);
        }
    }

    void setPaid(@NonNull LiveData<? extends PayableItem> currentItem, boolean paid) {
        PayableItem item = currentItem.getValue();
        if (item != null) {
            dao.setPaidSync(item.getId(), paid ? DateTime.now() : null);
        }
    }
}
