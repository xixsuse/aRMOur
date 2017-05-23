package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
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
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CrossCoverDetailFragment extends BaseFragment {

    private static final String[] PROJECTION = {
            Contract.CrossCoverShifts._ID,
            Contract.CrossCoverShifts.COLUMN_NAME_DATE,
            Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT,
            Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED,
            Contract.CrossCoverShifts.COLUMN_NAME_PAID,
            Contract.CrossCoverShifts.COLUMN_NAME_COMMENT,
    };
    private static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_DATE = 1,
            COLUMN_INDEX_PAYMENT = 2,
            COLUMN_INDEX_CLAIMED = 3,
            COLUMN_INDEX_PAID = 4,
            COLUMN_INDEX_COMMENT = 5;
    private long mShiftId;
    private View mPaidLayout;
    private TextView
            mDateView,
            mClaimedView,
            mPaidView;
    private AutoCompleteTextView mCommentView;
    private EditText mCrossCoverPaymentView;
    private Switch mClaimedSwitch, mPaidSwitch;

    public static CrossCoverDetailFragment create(long id) {
        Bundle args = new Bundle();
        args.putLong(ShiftDetailActivity.SHIFT_ID, id);
        CrossCoverDetailFragment fragment = new CrossCoverDetailFragment();
        fragment.setArguments(args);
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
        mCrossCoverPaymentView = (EditText) view.findViewById(R.id.cross_cover_payment_edit);
        mCommentView = (AutoCompleteTextView) view.findViewById(R.id.comment_text);
        SimpleCursorAdapter commentAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, new String[]{Contract.CrossCoverShifts.COLUMN_NAME_COMMENT}, new int[]{android.R.id.text1}, 0);
        commentAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String[] projection = new String[]{Contract.CrossCoverShifts._ID, Contract.CrossCoverShifts.COLUMN_NAME_COMMENT};
                boolean filtered = constraint != null && constraint.length() > 0;
                String select = filtered ? (Contract.CrossCoverShifts.COLUMN_NAME_COMMENT + " LIKE ?") : null;
                String[] selectArgs = filtered ? new String[]{"%" + constraint.toString() + "%"} : null;
                String sort = Contract.CrossCoverShifts.COLUMN_NAME_COMMENT;
                return getActivity().getContentResolver().query(Provider.crossCoverShiftsDistinctCommentsUri, projection, select, selectArgs, sort);
            }
        });
        commentAdapter.setStringConversionColumn(1);
        mCommentView.setAdapter(commentAdapter);

        View claimedLayout = view.findViewById(R.id.claimed_layout);
        ((ImageView) claimedLayout.findViewById(R.id.icon)).setImageResource(R.drawable.ic_check_box_half_black_24dp);
        ((TextView) claimedLayout.findViewById(R.id.title)).setText(R.string.claimed);
        mClaimedView = (TextView) claimedLayout.findViewById(R.id.text);
        mClaimedSwitch = (Switch) view.findViewById(R.id.button);

        mPaidLayout = view.findViewById(R.id.paid_layout);
        ((ImageView) mPaidLayout.findViewById(R.id.icon)).setImageResource(R.drawable.ic_check_box_full_black_24dp);
        ((TextView) mPaidLayout.findViewById(R.id.title)).setText(R.string.paid);
        mPaidView = (TextView) mPaidLayout.findViewById(R.id.text);
        mPaidSwitch = (Switch) mPaidLayout.findViewById(R.id.button);

    }

    @Override
    public int getLayout() {
        return R.layout.cross_cover_detail_fragment;
    }

    @Override
    public int getLoaderId() {
        return ShiftListActivity.LOADER_ID_CROSS_COVER_DETAIL;
    }

    @Override
    public int getTitle() {
        return R.string.cross_cover;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.crossCoverShiftUri(mShiftId), PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            final DateTime date = new DateTime(cursor.getLong(COLUMN_INDEX_DATE));
            mDateView.setText(DateTimeUtils.getFullDateString(date));
            mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PickerFragment.createCrossCoverDatePicker(mShiftId, date).show(getFragmentManager(), ShiftDetailActivity.PICKER_FRAGMENT);
                }
            });
            BigDecimal crossCoverPayment = BigDecimal.valueOf(cursor.getInt(COLUMN_INDEX_PAYMENT), 2);
            mCrossCoverPaymentView.setText(crossCoverPayment.toPlainString());
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
                    values.put(Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED, isChecked ? System.currentTimeMillis() : null);
                    getActivity().getContentResolver().update(Provider.crossCoverShiftUri(mShiftId), values, null, null);
                    mClaimedSwitch.setOnCheckedChangeListener(null);
                }
            });
            mPaidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ContentValues values = getUpdatedValues();
                    values.put(Contract.CrossCoverShifts.COLUMN_NAME_PAID, isChecked ? System.currentTimeMillis() : null);
                    getActivity().getContentResolver().update(Provider.crossCoverShiftUri(mShiftId), values, null, null);
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
            BigDecimal payment = (new BigDecimal(mCrossCoverPaymentView.getText().toString())).setScale(2, RoundingMode.HALF_UP);
            values.put(Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT, payment.unscaledValue().intValue());
        } catch (NumberFormatException e) {
            // do nothing
        }
        String comment = mCommentView.getText().toString().trim();
        values.put(Contract.CrossCoverShifts.COLUMN_NAME_COMMENT, comment.length() > 0 ? comment : null);
        return values;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getContentResolver().update(Provider.crossCoverShiftUri(mShiftId), getUpdatedValues(), null, null);
    }
}
