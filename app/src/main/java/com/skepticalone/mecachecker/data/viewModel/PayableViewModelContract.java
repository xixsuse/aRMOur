package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public interface PayableViewModelContract<Entity> extends ViewModelContract<Entity> {

    void saveNewPayment(@NonNull BigDecimal payment);
    void setClaimed(boolean claimed);
    void setPaid(boolean paid);

}
