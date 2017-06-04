package com.skepticalone.mecachecker.components.shifts;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.components.ShiftListActivity;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.DateTimeUtils;
import com.skepticalone.mecachecker.util.ShiftTypeCalculator;

import org.joda.time.Interval;

public class AdditionalShiftsListFragment extends AbstractPaymentItemListFragment {

    private static final String[] PROJECTION = {
            Contract.AdditionalShifts._ID,
            Contract.AdditionalShifts.COLUMN_NAME_START,
            Contract.AdditionalShifts.COLUMN_NAME_END,
            Contract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            Contract.AdditionalShifts.COLUMN_NAME_PAID,
            Contract.AdditionalShifts.COLUMN_NAME_COMMENT,
    };
    private final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_CLAIMED = 3,
            COLUMN_INDEX_PAID = 4,
            COLUMN_INDEX_COMMENT = 5;

    private ShiftTypeCalculator shiftTypeCalculator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        shiftTypeCalculator = new ShiftTypeCalculator(context);
    }

    @DrawableRes
    private int getShiftTypeIcon(ShiftType shiftType) {
        switch (shiftType) {
            case NORMAL_DAY:
                return R.drawable.ic_normal_day_black_24dp;
            case LONG_DAY:
                return R.drawable.ic_long_day_black_24dp;
            case NIGHT_SHIFT:
                return R.drawable.ic_night_shift_black_24dp;
            case OTHER:
                return R.drawable.ic_custom_shift_black_24dp;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    int getColumnIndexClaimed() {
        return COLUMN_INDEX_CLAIMED;
    }

    @Override
    int getColumnIndexPaid() {
        return COLUMN_INDEX_PAID;
    }

    @Override
    int getTitle() {
        return R.string.additional_shifts;
    }

    @Override
    int getLoaderId() {
        return ShiftListActivity.LOADER_ID_ADDITIONAL_LIST;
    }

    @Override
    Uri getContentUri() {
        return Provider.additionalShiftsUri;
    }

    @Override
    String[] getProjection() {
        return PROJECTION;
    }

    @Nullable
    @Override
    String getSortOrder() {
        return Contract.AdditionalShifts.COLUMN_NAME_START;
    }

    @Override
    int getColumnIndexId() {
        return COLUMN_INDEX_ID;
    }

    @Override
    void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor) {
        super.bindViewHolderToCursor(holder, cursor);
        Interval shift = new Interval(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END));
        SpannableStringBuilder ssb = new SpannableStringBuilder(DateTimeUtils.getFullDateString(shift.getStart()) + '\n');
        ssb.setSpan(new TextAppearanceSpan(getActivity(), R.style.TextAppearance_AppCompat_Body1), ssb.length(), ssb.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.append(DateTimeUtils.getTimeSpanString(shift));
        if (!cursor.isNull(COLUMN_INDEX_COMMENT)) {
            ssb.append('\n').append(cursor.getString(COLUMN_INDEX_COMMENT));
        }
        holder.text.setText(ssb);
        ShiftType shiftType = shiftTypeCalculator.getShiftType(shift, getActivity());
        holder.primaryIcon.setImageResource(getShiftTypeIcon(shiftType));
    }

}
