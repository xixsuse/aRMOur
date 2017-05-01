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
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.data.ShiftType;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;

public class AdditionalShiftDetailFragment extends ShiftTypeAwareFragment {

    private static final String[] PROJECTION = new String[]{
            ShiftContract.AdditionalShifts._ID,
            ShiftContract.AdditionalShifts.COLUMN_NAME_START,
            ShiftContract.AdditionalShifts.COLUMN_NAME_END,
            ShiftContract.AdditionalShifts.COLUMN_NAME_RATE,
            ShiftContract.AdditionalShifts.COLUMN_NAME_CLAIMED,
            ShiftContract.AdditionalShifts.COLUMN_NAME_PAID,
            ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT,
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
    private SimpleCursorAdapter mCommentAdapter;

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
        mCommentAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, null, new String[]{ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT}, new int[]{android.R.id.text1}, 0);
        mCommentAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String[] projection = new String[]{ShiftContract.AdditionalShifts._ID, ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT};
                boolean filtered = constraint != null && constraint.length() > 0;
                String select = filtered ? (ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT + " IS NOT NULL AND " + ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT + " LIKE ?") : null;
                String[] selectArgs = filtered ? new String[]{"%" + constraint.toString() + "%"} : null;
                String sort = ShiftContract.AdditionalShifts.COLUMN_NAME_START;
                return getActivity().getContentResolver().query(ShiftProvider.additionalShiftsUri, projection, select, selectArgs, sort);
            }
        });
        mCommentAdapter.setStringConversionColumn(1);
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
                if (s.length() > 0) {
                    float total = mShift.toDurationMillis() * Math.round(Float.parseFloat(s.toString())) / DateTimeConstants.MILLIS_PER_HOUR;
                    mTotalView.setText(getString(R.string.currency_format, total));
                }
            }
        });
        mTotalView = (TextView) view.findViewById(R.id.total_text);
        mCommentView = (AutoCompleteTextView) view.findViewById(R.id.comment_text);
        mCommentView.setAdapter(mCommentAdapter);
        mClaimedView = (TextView) view.findViewById(R.id.claimed_text);
        mClaimedSwitch = (Switch) view.findViewById(R.id.claimed_switch);
        mPaidLayout = view.findViewById(R.id.paid_layout);
        mPaidView = (TextView) mPaidLayout.findViewById(R.id.paid_text);
        mPaidSwitch = (Switch) mPaidLayout.findViewById(R.id.paid_switch);
    }

    @Override
    int getLayout() {
        return R.layout.additional_shift_detail_fragment;
    }

    @Override
    int getLoaderId() {
        return LOADER_ID_ADDITIONAL_DETAIL;
    }

    @Override
    int getTitle() {
        return R.string.additional_shift;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.additionalShiftUri(mShiftId), PROJECTION, null, null, ShiftContract.AdditionalShifts.COLUMN_NAME_START);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        if (cursor.moveToFirst()) {
            mShift = new Interval(cursor.getLong(COLUMN_INDEX_START), cursor.getLong(COLUMN_INDEX_END));
            mDateView.setText(DateTimeUtils.getFullDateString(mShift.getStart()));
            mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createDatePicker(mShiftId, false, mShift, null).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
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
            float hourlyRate = cursor.getInt(COLUMN_INDEX_RATE) / 100f;
            mHourlyRateView.setText(getString(R.string.decimal_format, hourlyRate));
//            mCommentView.removeTextChangedListener(mCommentWatcher);
            mCommentView.setText(cursor.getString(COLUMN_INDEX_COMMENT));
//            mCommentView.addTextChangedListener(mCommentWatcher);
//            mClaimedSwitch.setOnCheckedChangeListener(null);
//            mPaidSwitch.setOnCheckedChangeListener(null);
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
                    ContentValues values = new ContentValues();
                    values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_CLAIMED, isChecked ? System.currentTimeMillis() : null);
                    getActivity().getContentResolver().update(ShiftProvider.additionalShiftUri(mShiftId), values, null, null);
                    mClaimedSwitch.setOnCheckedChangeListener(null);
                }
            });
            mPaidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ContentValues values = new ContentValues();
                    values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_PAID, isChecked ? System.currentTimeMillis() : null);
                    getActivity().getContentResolver().update(ShiftProvider.additionalShiftUri(mShiftId), values, null, null);
                    mPaidSwitch.setOnCheckedChangeListener(null);
                }
            });

//            if (cursor.isNull(COLUMN_INDEX_PAID)) {
//                final boolean isClaimed = !cursor.isNull(COLUMN_INDEX_CLAIMED);
//                if (isClaimed) {
//                    mToggleButtonView.setText(R.string.mark_as_paid);
//                } else {
//                    mToggleButtonView.setText(R.string.mark_as_claimed);
//                }
//                mToggleButtonView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ContentValues values = new ContentValues();
//                        values.put(isClaimed ? ShiftContract.AdditionalShifts.COLUMN_NAME_PAID : ShiftContract.AdditionalShifts.COLUMN_NAME_CLAIMED, System.currentTimeMillis());
//                        getActivity().getContentResolver().update(ShiftProvider.additionalShiftUri(mShiftId), values, null, null);
//                    }
//                });
//                mToggleButtonView.setVisibility(View.VISIBLE);
//            } else {
//                mToggleButtonView.setVisibility(View.GONE);
//            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onStop() {
        super.onStop();
        ContentValues values = new ContentValues();
        String hourlyRateString = mHourlyRateView.getText().toString();
        if (hourlyRateString.length() > 0) {
            values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_RATE, Math.round(Float.parseFloat(hourlyRateString) * 100));
        }
        String comment = mCommentView.getText().toString();
        values.put(ShiftContract.AdditionalShifts.COLUMN_NAME_COMMENT, comment.length() > 0 ? comment : null);
        getActivity().getContentResolver().update(ShiftProvider.additionalShiftUri(mShiftId), values, null, null);
    }

    //    private class Adapter extends CursorAdapter<Cursor> {
//
//        @Override
//        public void onBindViewHolder(TwoLineViewHolder holder, int position) {
//            if (mCursor != null && mCursor.moveToFirst()) {
//                holder.itemView.setOnClickListener(null);
//                switch (position) {
//                    case 0:
//                        holder.primaryTextView.setText(R.string.shift_type);
//                        Interval shift = new Interval(mCursor.getLong(COLUMN_INDEX_START), mCursor.getLong(COLUMN_INDEX_END));
//                        ShiftType shiftType = getShiftType(shift.getStart().getMinuteOfDay(), shift.getEnd().getMinuteOfDay());
//                        int shiftTypeStringId, shiftTypeDrawableId;
//                        if (shiftType == ShiftType.NORMAL_DAY) {
//                            shiftTypeDrawableId = R.drawable.ic_normal_day_black_24dp;
//                            shiftTypeStringId = R.string.normal_day;
//                        } else if (shiftType == ShiftType.LONG_DAY) {
//                            shiftTypeDrawableId = R.drawable.ic_long_day_black_24dp;
//                            shiftTypeStringId = R.string.long_day;
//                        } else if (shiftType == ShiftType.NIGHT_SHIFT) {
//                            shiftTypeDrawableId = R.drawable.ic_night_shift_black_24dp;
//                            shiftTypeStringId = R.string.night_shift;
//                        } else {
//                            shiftTypeDrawableId = R.drawable.ic_custom_shift_black_24dp;
//                            shiftTypeStringId = R.string.custom;
//                        }
//                        holder.primaryIconView.setImageResource(shiftTypeDrawableId);
//                        holder.secondaryTextView.setText(DateTimeUtils.getShiftTypeWithDurationString(getString(shiftTypeStringId), shift.toPeriod()));
//                        break;
//                    case 1:
//                        holder.primaryIconView.setImageResource(R.drawable.ic_watch_black_24dp);
//                        holder.primaryTextView.setText(R.string.hourly_rate);
//                        final int hourlyRate = mCursor.getInt(COLUMN_INDEX_RATE);
//                        holder.secondaryTextView.setText(getString(R.string.currency_format, hourlyRate / 100f));
//                        holder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                HourlyRateEditFragment.create(mShiftId, hourlyRate).show(getFragmentManager(), ShiftDetailActivity.HOURLY_RATE_FRAGMENT);
//                            }
//                        });
//                        break;
//                    case 2:
//                        holder.primaryIconView.setImageResource(R.drawable.ic_dollar_black_24dp);
//                        holder.primaryTextView.setText(R.string.total);
//                        float total = ((mCursor.getLong(COLUMN_INDEX_END) - mCursor.getLong(COLUMN_INDEX_START)) * mCursor.getInt(COLUMN_INDEX_RATE)) / (DateTimeConstants.MILLIS_PER_HOUR * 100f);
//                        holder.secondaryTextView.setText(getString(R.string.currency_format, total));
//                        break;
////                    case 3:
////                        holder.primaryIconView.setImageResource(R.drawable.ic_check_box_half_black_24dp);
////                        holder.primaryTextView.setText(R.string.claimed);
////                        holder.secondaryTextView.setText(mCursor.isNull(COLUMN_INDEX_CLAIMED) ? getString(R.string.not_applicable) : DateTimeUtils.getDateTimeString(mCursor.getLong(COLUMN_INDEX_CLAIMED)));
////                        break;
////                    case 4:
////                        holder.primaryIconView.setImageResource(R.drawable.ic_check_box_full_black_24dp);
////                        holder.primaryTextView.setText(R.string.paid);
////                        holder.secondaryTextView.setText(DateTimeUtils.getDateTimeString(mCursor.getLong(COLUMN_INDEX_PAID)));
////                        break;
//                    default:
//                        if (position == 3 && !mCursor.isNull(COLUMN_INDEX_COMMENT)) {
//                            holder.primaryIconView.setImageResource(R.drawable.ic_pencil_black_24dp);
//                            holder.primaryTextView.setText(R.string.comment);
//                            holder.secondaryTextView.setText(mCursor.getString(COLUMN_INDEX_COMMENT));
//                        } else if (position == 3 || (position == 4 && !mCursor.isNull(COLUMN_INDEX_COMMENT))) {
//                            holder.primaryIconView.setImageResource(R.drawable.ic_check_box_half_black_24dp);
//                            holder.primaryTextView.setText(R.string.claimed);
//                            holder.secondaryTextView.setText(mCursor.isNull(COLUMN_INDEX_CLAIMED) ? getString(R.string.not_applicable) : DateTimeUtils.getDateTimeString(mCursor.getLong(COLUMN_INDEX_CLAIMED)));
//                        } else {
//                            holder.primaryIconView.setImageResource(R.drawable.ic_check_box_full_black_24dp);
//                            holder.primaryTextView.setText(R.string.paid);
//                            holder.secondaryTextView.setText(DateTimeUtils.getDateTimeString(mCursor.getLong(COLUMN_INDEX_PAID)));
//                        }
//                }
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            int count = 0;
//            if (mCursor != null) {
//                count += 3;
//                if (!mCursor.isNull(COLUMN_INDEX_COMMENT)) count++;
//                if (!mCursor.isNull(COLUMN_INDEX_PAID)) count += 2;
//                else if (!mCursor.isNull(COLUMN_INDEX_CLAIMED)) count++;
//            }
//            return count;
//        }
//    }

}
