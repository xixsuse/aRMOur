package com.skepticalone.mecachecker.ui.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.model.CrossCover;
import com.skepticalone.mecachecker.util.Comparators;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class CrossCoverDetailAdapter extends ItemDetailAdapter<CrossCover> {

    private static final int
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_PAYMENT = 1,
            ROW_NUMBER_COMMENT = 2,
            ROW_NUMBER_CLAIMED = 3,
            ROW_NUMBER_PAID = 4,
            ROW_COUNT = 5;

    private final Callbacks mCallbacks;

    public CrossCoverDetailAdapter(Callbacks callbacks) {
        super();
        mCallbacks = callbacks;
    }

    @Override
    int getRowCount(@NonNull CrossCover item) {
        return ROW_COUNT;
    }

    @Override
    void onItemUpdated(@NonNull CrossCover oldCrossCover, @NonNull CrossCover newCrossCover) {
        if (!Comparators.equalLocalDates(oldCrossCover.getDate(), newCrossCover.getDate())) {
            notifyItemChanged(ROW_NUMBER_DATE);
        }
        if (!Comparators.equalBigDecimals(oldCrossCover.getPayment(), newCrossCover.getPayment())) {
            notifyItemChanged(ROW_NUMBER_PAYMENT);
        }
        if (!Comparators.equalStrings(oldCrossCover.getComment(), newCrossCover.getComment())) {
            notifyItemChanged(ROW_NUMBER_COMMENT);
        }
        if (!Comparators.equalDateTimes(oldCrossCover.getClaimed(), newCrossCover.getClaimed()) || !Comparators.equalDateTimes(oldCrossCover.getPaid(), newCrossCover.getPaid())) {
            notifyItemChanged(ROW_NUMBER_CLAIMED);
            notifyItemChanged(ROW_NUMBER_PAID);
        }
    }

    @Override
    void bindViewHolder(@NonNull final CrossCover crossCover, ItemViewHolder holder, int position) {
        int primaryIcon;
        boolean switchVisible = false, switchChecked = false;
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = null;
        View.OnClickListener onClickListener = null;
        switch (position) {
            case ROW_NUMBER_DATE:
                primaryIcon = R.drawable.ic_calendar_black_24dp;
                holder.setText(R.string.date, DateTimeUtils.getFullDateString(crossCover.getDate()));
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallbacks.changeDate(crossCover.getId(), crossCover.getDate());
                    }
                };
                break;
            case ROW_NUMBER_PAYMENT:
                primaryIcon = R.drawable.ic_dollar_black_24dp;
                holder.setText(R.string.payment, R.string.currency_format, crossCover.getPayment());
                break;
            case ROW_NUMBER_COMMENT:
                primaryIcon = R.drawable.ic_comment_black_24dp;
                holder.setText(R.string.comment, crossCover.getComment());
                onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallbacks.changeComment(crossCover.getId(), crossCover.getComment());
                    }
                };
                break;
            case ROW_NUMBER_CLAIMED:
                switchVisible = true;
                DateTime claimed = crossCover.getClaimed();
                if (claimed == null) {
                    primaryIcon = 0;
                    holder.setText(R.string.claimed, R.string.not_applicable);
                } else {
                    primaryIcon = R.drawable.ic_check_box_half_black_24dp;
                    holder.setText(R.string.claimed, DateTimeUtils.getDateTimeString(claimed));
                    switchChecked = true;
                }
                onCheckedChangeListener = crossCover.getPaid() == null ? new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean claimed) {
                        mCallbacks.setClaimed(crossCover.getId(), claimed);
                    }
                } : null;
                break;
            case ROW_NUMBER_PAID:
                switchVisible = true;
                final DateTime paid = crossCover.getPaid();
                if (paid == null) {
                    primaryIcon = 0;
                    holder.setText(R.string.paid, R.string.not_applicable);
                } else {
                    primaryIcon = R.drawable.ic_check_box_full_black_24dp;
                    holder.setText(R.string.paid, DateTimeUtils.getDateTimeString(paid));
                    switchChecked = true;
                }
                onCheckedChangeListener = crossCover.getClaimed() == null ? null : new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean paid) {
                        mCallbacks.setPaid(crossCover.getId(), paid);
                    }
                };
                break;
            default:
                throw new IllegalStateException();
        }
        holder.primaryIcon.setImageResource(primaryIcon);
        if (switchVisible) {
            if (holder.switchControl.isChecked() != switchChecked) {
                holder.switchControl.setOnCheckedChangeListener(null);
                holder.switchControl.setChecked(switchChecked);
            }
            holder.switchControl.setEnabled(onCheckedChangeListener != null);
            holder.switchControl.setVisibility(View.VISIBLE);
        } else {
            holder.switchControl.setVisibility(View.GONE);
        }
        holder.switchControl.setOnCheckedChangeListener(onCheckedChangeListener);
        holder.itemView.setOnClickListener(onClickListener);
    }

    public interface Callbacks extends PayableCallbacks {
        void changeDate(long id, @NonNull LocalDate currentDate);
    }

}
