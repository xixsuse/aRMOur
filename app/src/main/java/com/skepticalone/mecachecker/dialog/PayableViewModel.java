package com.skepticalone.mecachecker.dialog;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public interface PayableViewModel<Entity> extends BaseViewModel<Entity> {

    void saveNewPayment(@NonNull BigDecimal payment);
}
