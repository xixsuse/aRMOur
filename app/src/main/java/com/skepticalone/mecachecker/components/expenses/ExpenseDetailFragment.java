package com.skepticalone.mecachecker.components.expenses;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class ExpenseDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] PROJECTION = {
            Contract.Expenses.COLUMN_NAME_TITLE,
            Contract.Expenses.COLUMN_NAME_PAYMENT,
            Contract.Expenses.COLUMN_NAME_CLAIMED,
            Contract.Expenses.COLUMN_NAME_PAID,
            Contract.Expenses.COLUMN_NAME_COMMENT,
    };

    private static final int
            COLUMN_INDEX_TITLE = 0,
            COLUMN_INDEX_PAYMENT = 1,
            COLUMN_INDEX_CLAIMED = 2,
            COLUMN_INDEX_PAID = 3,
            COLUMN_INDEX_COMMENT = 4;

    private long mExpenseId;
    private TextInputLayout mTitleLayout, mPaymentLayout;
    private EditText mTitleView, mPaymentView;
    private View mClaimedLayout, mPaidLayout;
    private Switch mClaimedSwitch, mPaidSwitch;
    private TextView
            mClaimedView,
            mPaidView;

    public static ExpenseDetailFragment create(long expenseId) {
        Bundle args = new Bundle();
        args.putLong(ExpenseDetailActivity.EXPENSE_ID, expenseId);
        ExpenseDetailFragment fragment = new ExpenseDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExpenseId = getArguments().getLong(ExpenseDetailActivity.EXPENSE_ID, ExpenseDetailActivity.NO_ID);
    }

    private boolean isNewExpense() {
        return mExpenseId == ExpenseDetailActivity.NO_ID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expense_detail_fragment, container, false);
        mTitleLayout = (TextInputLayout) view.findViewById(R.id.title_layout);
        mTitleView = (EditText) mTitleLayout.findViewById(R.id.expense_title);
        mPaymentLayout = (TextInputLayout) view.findViewById(R.id.payment_layout);
        mPaymentView = (EditText) mPaymentLayout.findViewById(R.id.payment);

        mClaimedLayout = view.findViewById(R.id.claimed_layout);
        ((ImageView) mClaimedLayout.findViewById(R.id.icon)).setImageResource(R.drawable.ic_check_box_half_black_24dp);
        ((TextView) mClaimedLayout.findViewById(R.id.switch_title)).setText(R.string.claimed);
        mClaimedView = (TextView) mClaimedLayout.findViewById(R.id.text);
        mClaimedSwitch = (Switch) view.findViewById(R.id.button);

        mPaidLayout = view.findViewById(R.id.paid_layout);
        ((ImageView) mPaidLayout.findViewById(R.id.icon)).setImageResource(R.drawable.ic_check_box_full_black_24dp);
        ((TextView) mPaidLayout.findViewById(R.id.switch_title)).setText(R.string.paid);
        mPaidView = (TextView) mPaidLayout.findViewById(R.id.text);
        mPaidSwitch = (Switch) mPaidLayout.findViewById(R.id.button);

        if (isNewExpense()) {
            mClaimedLayout.setVisibility(View.GONE);
            mPaidLayout.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isNewExpense())
            getLoaderManager().initLoader(ExpenseDetailActivity.DETAIL_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.expenseUri(mExpenseId), PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            mTitleLayout.setHintAnimationEnabled(false);
            mTitleView.setText(cursor.getString(COLUMN_INDEX_TITLE));
            mTitleLayout.setHintAnimationEnabled(true);
            BigDecimal payment = BigDecimal.valueOf(cursor.getInt(COLUMN_INDEX_PAYMENT), 2);
            mPaymentLayout.setHintAnimationEnabled(false);
            mPaymentView.setText(payment.toPlainString());
            mPaymentLayout.setHintAnimationEnabled(true);
            mClaimedLayout.setVisibility(View.VISIBLE);
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
                    values.put(Contract.Expenses.COLUMN_NAME_CLAIMED, isChecked ? System.currentTimeMillis() : null);
                    getActivity().getContentResolver().update(Provider.expenseUri(mExpenseId), values, null, null);
                    mClaimedSwitch.setOnCheckedChangeListener(null);
                }
            });
            mPaidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ContentValues values = getUpdatedValues();
                    values.put(Contract.Expenses.COLUMN_NAME_PAID, isChecked ? System.currentTimeMillis() : null);
                    getActivity().getContentResolver().update(Provider.expenseUri(mExpenseId), values, null, null);
                    mPaidSwitch.setOnCheckedChangeListener(null);
                }
            });

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private ContentValues getUpdatedValues() {
        ContentValues values = new ContentValues();
        String title = mTitleView.getText().toString().trim();
        if (title.length() > 0) {
            values.put(Contract.Expenses.COLUMN_NAME_TITLE, title);
        }
        try {
            BigDecimal payment = (new BigDecimal(mPaymentView.getText().toString())).setScale(2, RoundingMode.HALF_UP);
            values.put(Contract.Expenses.COLUMN_NAME_PAYMENT, payment.unscaledValue().intValue());
        } catch (NumberFormatException e) {
            // do nothing
        }
        return values;
    }

    @Override
    public void onStop() {
        super.onStop();
        ContentValues values = getUpdatedValues();
        if (!values.containsKey(Contract.Expenses.COLUMN_NAME_TITLE)) return;
        if (isNewExpense()) {
            if (!values.containsKey(Contract.Expenses.COLUMN_NAME_PAYMENT)) {
                values.put(Contract.Expenses.COLUMN_NAME_PAYMENT, 0);
            }
            Uri uri = getActivity().getContentResolver().insert(Provider.expensesUri, values);
            if (uri != null) {
                mExpenseId = Long.valueOf(uri.getLastPathSegment());
            }
        } else {
            getActivity().getContentResolver().update(Provider.expenseUri(mExpenseId), values, null, null);
        }
    }
}
