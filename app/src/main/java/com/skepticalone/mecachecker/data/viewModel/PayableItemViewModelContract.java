package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public interface PayableItemViewModelContract<Entity> extends ItemViewModelContract<Entity> {
    void saveNewPayment(@NonNull BigDecimal payment);
}
