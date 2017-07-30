package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.math.BigDecimal;

@MainThread
public interface PayableViewModelContract<Entity> extends ViewModelContract<Entity> {

    void saveNewPayment(@NonNull BigDecimal payment);
    void setClaimed(long id, boolean claimed);
    void setPaid(long id, boolean paid);

}
