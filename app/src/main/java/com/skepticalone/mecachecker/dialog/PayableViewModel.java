package com.skepticalone.mecachecker.dialog;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.model.PayableItem;

import java.math.BigDecimal;

public interface PayableViewModel<Entity extends PayableItem> extends BaseViewModel<Entity> {
    void saveNewPayment(@NonNull BigDecimal payment);
}
