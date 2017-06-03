package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AdditionalShiftDetailFragment extends ShiftTypeAwareFragment {

    private static final String[] PROJECTION = {
            Contract.AdditionalShifts._ID,
            Contract.AdditionalShifts.COLUMN_NAME_START,
            Contract.AdditionalShifts.COLUMN_NAME_END,
            Contract.AdditionalShifts.COLUMN_NAME_RATE,
            Contract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            Contract.AdditionalShifts.COLUMN_NAME_PAID,
            Contract.AdditionalShifts.COLUMN_NAME_COMMENT,
    };
    private final static int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_START = 1,
            COLUMN_INDEX_END = 2,
            COLUMN_INDEX_RATE = 3,
            COLUMN_INDEX_CLAIMED = 4,
            COLUMN_INDEX_PAID = 5,
            COLUMN_INDEX_COMMENT = 6;

    private long mShiftId;
    private Interval mShift;
    private View mPaidLayout;
    private TextView
            mDateView,
            mStartTimeView,
            mEndTimeView,
            mShiftTypeView,
            mDurationView,
            mTotalView,
            mClaimedView,
            mPaidView;
    private AutoCompleteTextView mCommentView;
    private EditText mHourlyRateView;
    private ImageView mShiftIconView;
    private Switch mClaimedSwitch, mPaidSwitch;

    static AdditionalShiftDetailFragment create(long id) {
        AdditionalShiftDetailFragment fragment = new AdditionalShiftDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(ShiftDetailActivity.SHIFT_ID, id);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mShiftId = getArguments().getLong(ShiftDetailActivity.SHIFT_ID, ShiftDetailActivity.NO_ID);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDateView = (TextView) view.findViewById(R.id.date);
        mStartTimeView = (TextView) view.findViewById(R.id.start_time);
        mEndTimeView = (TextView) view.findViewById(R.id.end_time);
        mShiftTypeView = (TextView) view.findViewById(R.id.shift_type);
        mDurationView = (TextView) view.findViewById(R.id.shift_duration);
        mShiftIconView = (ImageView) view.findViewById(R.id.shift_icon);
        mHourlyRateView = (EditText) view.findViewById(R.id.hourly_rate_edit);
        mHourlyRateView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    BigDecimal rate = (new BigDecimal(s.toString())).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal total = rate.multiply(BigDecimal.valueOf(mShift.toDurationMillis())).divide(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_HOUR), 2, BigDecimal.ROUND_HALF_UP);
                    mTotalView.setText(getString(R.string.currency_format, total));
                } catch (NumberFormatException e) {
                    mTotalView.setText(R.string.not_applicable);
                }
            }
        });
        mTotalView = (TextView) view.findViewById(R.id.total_text);
        mCommentView = (AutoCompleteTextView) view.findViewById(R.id.comment_text);
        SimpleCursorAdapter commentAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, new String[]{Contract.AdditionalShifts.COLUMN_NAME_COMMENT}, new int[]{android.R.id.text1}, 0);
        commentAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String[] projection = new String[]{Contract.AdditionalShifts._ID, Contract.AdditionalShifts.COLUMN_NAME_COMMENT};
                boolean filtered = constraint != null && constraint.length() > 0;
                String select = filtered ? (Contract.AdditionalShifts.COLUMN_NAME_COMMENT + " LIKE ?") : null;
                String[] selectArgs = filtered ? new String[]{"%" + constraint.toString() + "%"} : null;
                String sort = Contract.AdditionalShifts.COLUMN_NAME_COMMENT;
                return getActivity().getContentResolver().query(Provider.additionalShiftsDistinctCommentsUri, projection, select, selectArgs, sort);
            }
        });
        commentAdapter.setStringConversionColumn(1);
        mCommentView.setAdapter(commentAdapter);

        View claimedLayout = view.findViewById(R.id.claimed_layout);
        ((ImageView) claimedLayout.findViewById(R.id.icon)).setImageResource(R.drawable.ic_check_box_half_black_24dp);
        ((TextView) claimedLayout.findViewById(R.id.switch_title)).setText(R.string.claimed);
        mClaimedView = (TextView) claimedLayout.findViewById(R.id.text);
        mClaimedSwitch = (Switch) view.findViewById(R.id.button);

        mPaidLayout = view.findViewById(R.id.paid_layout);
        ((ImageView) mPaidLayout.findViewById(R.id.icon)).setImageResource(R.drawable.ic_check_box_full_black_24dp);
        ((TextView) mPaidLayout.findViewById(R.id.switch_title)).setText(R.string.paid);
        mPaidView = (TextView) mPaidLayout.findViewById(R.id.text);
        mPaidSwitch = (Switch) mPaidLayout.findViewById(R.id.button);

    }

    @Override
    public int getLayout() {
        return R.layout.additional_shift_detail_fragment;
    }

    @Override
    public int getLoaderId() {
        return ShiftListActivity.LOADER_ID_ADDITIONAL_DETAIL;
    }

    @Override
    public int getTitle() {
        return R.string.additional_shift;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.additionalShiftUri(mShiftId), PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        if (cursor.moveToFirst()) {
            mShift = new Interval(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END));
            mDateView.setText(DateTimeUtils.getFullDateString(mShift.getStart()));
            mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createIntervalDatePicker(mShiftId, false, mShift, null).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            mStartTimeView.setText(DateTimeUtils.getTimeString(mShift, true, null));
            mStartTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createTimePicker(mShiftId, false, mShift, null, true, false).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            mEndTimeView.setText(DateTimeUtils.getTimeString(mShift, false, null));
            mEndTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createTimePicker(mShiftId, false, mShift, null, false, false).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            Interval shift = new Interval(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END));
            ShiftType shiftType = getShiftType(shift);
            int shiftTypeStringId, shiftTypeDrawableId;
            if (shiftType == ShiftType.NORMAL_DAY) {
                shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
                shiftTypeStringId = R.string.normal_day;
            } else if (shiftType == ShiftType.LONG_DAY) {
                shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
                shiftTypeStringId = R.string.long_day;
            } else if (shiftType == ShiftType.NIGHT_SHIFT) {
                shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
                shiftTypeStringId = R.string.night_shift;
            } else {
                shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
                shiftTypeStringId = R.string.custom;
            }
            mShiftTypeView.setText(shiftTypeStringId);
            mShiftIconView.setImageResource(shiftTypeDrawableId);
            mDurationView.setText(DateTimeUtils.getPeriodString(shift.toPeriod()));
            BigDecimal hourlyRate = BigDecimal.valueOf(cursor.getInt(COLUMN_INDEX_RATE), 2);
            mHourlyRateView.setText(hourlyRate.toPlainString());
            mCommentView.setText(cursor.isNull(COLUMN_INDEX_COMMENT) ? "" : cursor.getString(COLUMN_INDEX_COMMENT));
            mClaimedSwitch.setVisibility(View.VISIBLE);
            if (cursor.isNull(COLUMN_INDEX_CLAIMED)) {
                mClaimedView.setText(R.string.not_applicable);
                mClaimedSwitch.setChecked(false);
                mPaidLayout.setVisibility(View.GONE);
            } else {
                mClaimedView.setText(DateTimeUtils.getDateTimeString(cursor.getLong(COLUMN_INDEX_CLAIMED)));
                mClaimedSwitch.setChecked(true);
                if (cursor.isNull(COLUMN_INDEX_PAID)) {
                    mPaidView.setText(R.string.not_applicable);
                    mPaidSwitch.setChecked(false);
                } else {
                    mClaimedSwitch.setVisibility(View.INVISIBLE);
                    mPaidView.setText(DateTimeUtils.getDateTimeString(cursor.getLong(COLUMN_INDEX_PAID)));
                    mPaidSwitch.setChecked(true);
                }
                mPaidLayout.setVisibility(View.VISIBLE);
            }
            mClaimedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ContentValues values = getUpdatedValues();
                    values.put(Contract.AdditionalShifts.COLUMN_NAME_CLAIMED, isChecked ? System.currentTimeMillis() : null);
                    getActivity().getContentResolver().update(Provider.additionalShiftUri(mShiftId), values, null, null);
                    mClaimedSwitch.setOnCheckedChangeListener(null);
                }
            });
            mPaidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ContentValues values = getUpdatedValues();
                    values.put(Contract.AdditionalShifts.COLUMN_NAME_PAID, isChecked ? System.currentTimeMillis() : null);
                    getActivity().getContentResolver().update(Provider.additionalShiftUri(mShiftId), values, null, null);
                    mPaidSwitch.setOnCheckedChangeListener(null);
                }
            });

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mClaimedSwitch.setOnCheckedChangeListener(null);
        mPaidSwitch.setOnCheckedChangeListener(null);
    }

    private ContentValues getUpdatedValues() {
        ContentValues values = new ContentValues();
        try {
            values.put(Contract.AdditionalShifts.COLUMN_NAME_RATE, (new BigDecimal(mHourlyRateView.getText().toString())).setScale(2, RoundingMode.HALF_UP).unscaledValue().intValue());
        } catch (NumberFormatException e) {
            // do nothing
        }
        String comment = mCommentView.getText().toString().trim();
        values.put(Contract.AdditionalShifts.COLUMN_NAME_COMMENT, comment.length() > 0 ? comment : null);
        return values;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getContentResolver().update(Provider.additionalShiftUri(mShiftId), getUpdatedValues(), null, null);
    }

}
