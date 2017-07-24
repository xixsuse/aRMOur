package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public interface PayableViewModel<Entity> extends BaseViewModel<Entity> {

    void saveNewPayment(@NonNull BigDecimal payment);
    void setClaimed(boolean claimed);
    void setPaid(boolean paid);

}
