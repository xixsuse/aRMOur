package com.skepticalone.armour.data.viewModel;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

public interface PayableViewModelContract<FinalItem> extends ItemViewModelContract<FinalItem> {
    @SuppressWarnings("unused")
    void setClaimed(boolean claimed);

    @SuppressWarnings("unused")
    void setPaid(boolean paid);
    void saveNewPayment(@NonNull BigDecimal payment);
}
