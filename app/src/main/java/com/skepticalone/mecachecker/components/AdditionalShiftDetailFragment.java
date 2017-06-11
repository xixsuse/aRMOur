package com.skepticalone.mecachecker.components;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

public class AdditionalShiftDetailFragment extends DetailFragment implements ShiftData.Callbacks, PaymentData.Callbacks {

    private static final String[] PROJECTION = {
            Contract.AdditionalShifts.COLUMN_NAME_START,
            Contract.AdditionalShifts.COLUMN_NAME_END,
            Contract.AdditionalShifts.COLUMN_NAME_RATE,
            Contract.AdditionalShifts.COLUMN_NAME_COMMENT,
            Contract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            Contract.AdditionalShifts.COLUMN_NAME_PAID,
    };
    private final static int
            COLUMN_INDEX_START = 0,
            COLUMN_INDEX_END = 1,
            COLUMN_INDEX_RATE = 2,
            COLUMN_INDEX_COMMENT = 3,
            COLUMN_INDEX_CLAIMED = 4,
            COLUMN_INDEX_PAID = 5,
            ROW_NUMBER_DATE = 0,
            ROW_NUMBER_START = 1,
            ROW_NUMBER_END = 2,
            ROW_NUMBER_RATE = 3,
            ROW_NUMBER_COMMENT = 4,
            ROW_NUMBER_CLAIMED = 5,
            ROW_NUMBER_PAID = 6,
            ROW_COUNT = 7;
    private final ShiftData mShiftData = new ShiftData(this);
    private final PaymentData mPaymentData = new PaymentData(this);

    static AdditionalShiftDetailFragment create(long id) {
        AdditionalShiftDetailFragment fragment = new AdditionalShiftDetailFragment();
        fragment.setArguments(createArguments(id));
        return fragment;
    }

    @Override
    int getTitle() {
        return R.string.additional_shift;
    }

    @Override
    int getLoaderId() {
        return LifecycleConstants.LOADER_ID_ADDITIONAL_DETAIL;
    }

    @Override
    public Uri getContentUri() {
        return Provider.additionalShiftUri(getItemId());
    }

    @NonNull
    @Override
    public String getColumnNameStart() {
        return Contract.AdditionalShifts.COLUMN_NAME_START;
    }

    @NonNull
    @Override
    public String getColumnNameEnd() {
        return Contract.AdditionalShifts.COLUMN_NAME_END;
    }

    @NonNull
    @Override
    public String getColumnNameMoney() {
        return Contract.AdditionalShifts.COLUMN_NAME_RATE;
    }

    @NonNull
    @Override
    public String getColumnNameComment() {
        return Contract.AdditionalShifts.COLUMN_NAME_COMMENT;
    }

    @NonNull
    @Override
    public String getColumnNameClaimed() {
        return Contract.AdditionalShifts.COLUMN_NAME_CLAIMED;
    }

    @NonNull
    @Override
    public String getColumnNamePaid() {
        return Contract.AdditionalShifts.COLUMN_NAME_PAID;
    }

    @Nullable
    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @Override
    public int getColumnIndexStart() {
        return COLUMN_INDEX_START;
    }

    @Override
    public int getColumnIndexEnd() {
        return COLUMN_INDEX_END;
    }

    @Override
    public int getColumnIndexMoney() {
        return COLUMN_INDEX_RATE;
    }

    @Override
    public int getColumnIndexComment() {
        return COLUMN_INDEX_COMMENT;
    }

    @Override
    public int getColumnIndexClaimed() {
        return COLUMN_INDEX_CLAIMED;
    }

    @Override
    public int getColumnIndexPaid() {
        return COLUMN_INDEX_PAID;
    }

    @Override
    public int getRowNumberDate() {
        return ROW_NUMBER_DATE;
    }

    @Override
    public int getRowNumberStart() {
        return ROW_NUMBER_START;
    }

    @Override
    public int getRowNumberEnd() {
        return ROW_NUMBER_END;
    }

    @Override
    public int getRowNumberMoney() {
        return ROW_NUMBER_RATE;
    }

    @Override
    public int getRowNumberComment() {
        return ROW_NUMBER_COMMENT;
    }

    @Override
    public int getRowNumberClaimed() {
        return ROW_NUMBER_CLAIMED;
    }

    @Override
    public int getRowNumberPaid() {
        return ROW_NUMBER_PAID;
    }

    @Override
    public int getMoneyTitle() {
        return R.string.hourly_rate;
    }

    @Override
    public int getMoneyIcon() {
        return R.drawable.ic_watch_black_24dp;
    }

    @Override
    void readFromPositionedCursor(@NonNull Cursor cursor) {
        mShiftData.readFromPositionedCursor(cursor);
        mPaymentData.readFromPositionedCursor(cursor);
    }

    @Override
    int getRowCountIfLoaded() {
        return mPaymentData.getAdjustedRowCount(ROW_COUNT);
    }

    @Override
    boolean isSwitchType(int position) {
        return AbstractData.isSwitchType(position, mShiftData, mPaymentData);
    }

    @Override
    PlainListItemViewHolder onCreatePlainListItemViewHolder(ViewGroup parent) {
        PlainListItemViewHolder holder = super.onCreatePlainListItemViewHolder(parent);
        holder.secondaryIcon.setVisibility(View.GONE);
        return holder;
    }

    @Override
    SwitchListItemViewHolder onCreateSwitchListItemViewHolder(ViewGroup parent) {
        return new SwitchListItemViewHolder(parent, mPaymentData);
    }

    @Override
    boolean bindPlainListItemViewHolder(PlainListItemViewHolder holder, int position) {
        return mShiftData.bindToHolder(getActivity(), holder, position) || mPaymentData.bindToHolder(getActivity(), holder, position);
    }

    @Override
    boolean bindSwitchListItemViewHolder(SwitchListItemViewHolder holder, int position) {
        return mPaymentData.bindToHolder(getActivity(), holder, position);
    }

}