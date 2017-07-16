package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.LocalDate;

public final class CrossCoverDetailAdapter extends ItemDetailAdapter<CrossCover> {

    private final Callbacks callbacks;
    private final PayableDetailAdapterHelper payableDetailAdapterHelper;

    public CrossCoverDetailAdapter(Callbacks callbacks) {
        super(callbacks);
        this.callbacks = callbacks;
        payableDetailAdapterHelper = new PayableDetailAdapterHelper(callbacks);
    }

    @Override
    void onItemUpdated(@NonNull CrossCover oldCrossCover, @NonNull CrossCover newCrossCover) {
        super.onItemUpdated(oldCrossCover, newCrossCover);
        payableDetailAdapterHelper.onItemUpdated(oldCrossCover, newCrossCover, this);
        if (!Comparators.equalLocalDates(oldCrossCover.getDate(), newCrossCover.getDate())) {
            notifyItemChanged(callbacks.getRowNumberDate());
        }
    }

    @Override
    boolean bindViewHolder(@NonNull final CrossCover crossCover, ItemViewHolder holder, int position) {
        if (position == callbacks.getRowNumberDate()) {
            holder.setupPlain(R.drawable.ic_calendar_black_24dp, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callbacks.changeDate(crossCover.getId(), crossCover.getDate());
                }
            });
            holder.setText(holder.getText(R.string.date), DateTimeUtils.getFullDateString(crossCover.getDate()));
            return true;
        } else return payableDetailAdapterHelper.bindViewHolder(crossCover, holder, position) || super.bindViewHolder(crossCover, holder, position);
    }

    public interface Callbacks extends ItemDetailAdapter.Callbacks<CrossCover>, PayableDetailAdapterHelper.Callbacks {
        int getRowNumberDate();
        void changeDate(long id, @NonNull LocalDate currentDate);
    }

}
