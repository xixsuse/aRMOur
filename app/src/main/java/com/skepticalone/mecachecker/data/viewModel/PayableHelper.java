package com.skepticalone.mecachecker.data.viewModel;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.dao.PayableDaoContract;
import com.skepticalone.mecachecker.data.model.Payable;

import org.joda.time.DateTime;

import java.math.BigDecimal;

final class PayableHelper {

    @NonNull
    private final PayableDaoContract dao;

    PayableHelper(@NonNull PayableDaoContract dao) {
        this.dao = dao;
    }

    void saveNewPayment(@NonNull LiveData<? extends Payable> currentItem, @NonNull BigDecimal payment) {
        Payable item = currentItem.getValue();
        if (item != null) {
            dao.setPaymentSync(item.getId(), payment);
        }
    }

    void setClaimed(@NonNull LiveData<? extends Payable> currentItem, boolean claimed) {
        Payable item = currentItem.getValue();
        if (item != null) {
            dao.setClaimedSync(item.getId(), claimed ? DateTime.now() : null);
        }
    }

    void setPaid(@NonNull LiveData<? extends Payable> currentItem, boolean paid) {
        Payable item = currentItem.getValue();
        if (item != null) {
            dao.setPaidSync(item.getId(), paid ? DateTime.now() : null);
        }
    }
}
