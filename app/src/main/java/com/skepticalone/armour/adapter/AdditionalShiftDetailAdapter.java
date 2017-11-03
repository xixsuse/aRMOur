package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.model.AdditionalShift;

import java.util.ArrayList;
import java.util.List;

public final class AdditionalShiftDetailAdapter extends ItemDetailAdapter<AdditionalShift> {

    @NonNull
    private final Callbacks callbacks;

    public AdditionalShiftDetailAdapter(@NonNull Context context, @NonNull Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    List<ItemViewHolder.Binder> getNewList(@NonNull AdditionalShift shift) {
        List<ItemViewHolder.Binder> list = new ArrayList<>(9);
        list.add(new DateBinder(callbacks, shift.getShiftData().getStart().toLocalDate()));
        list.add(new ShiftDataBinder(callbacks, shift.getShiftData(), true, false));
        list.add(new ShiftDataBinder(callbacks, shift.getShiftData(), false, false));
        list.add(new ShiftTypeBinder(shift.getShiftType(), shift.getShiftData().getDuration()));
        list.add(new PaymentBinder(callbacks, shift.getPaymentData().getPayment(), R.drawable.ic_watch_black_24dp, R.string.hourly_rate));
        list.add(new TotalPaymentBinder(shift.getTotalPayment()));
        list.add(new CommentBinder(callbacks, shift.getComment()));
        list.add(new ClaimedSwitchBinder(callbacks, shift.getPaymentData()));
        list.add(new PaidSwitchBinder(callbacks, shift.getPaymentData()));
        return list;
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks, DateBinder.Callbacks, ShiftDataBinder.Callbacks, PaymentBinder.Callbacks, ClaimedSwitchBinder.Callbacks, PaidSwitchBinder.Callbacks {
    }

}
