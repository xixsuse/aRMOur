package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import java.math.BigDecimal;
import java.util.Locale;

public class AllSummaryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[]
            ROSTERED_PROJECTION = {
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_START,
            Contract.RosteredShifts.COLUMN_NAME_ROSTERED_END,
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_START,
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_END
    },
            ADDITIONAL_PROJECTION = {
                    Contract.AdditionalShifts.COLUMN_NAME_START,
                    Contract.AdditionalShifts.COLUMN_NAME_END,
                    Contract.AdditionalShifts.COLUMN_NAME_RATE,
                    Contract.AdditionalShifts.COLUMN_NAME_CLAIMED,
                    Contract.AdditionalShifts.COLUMN_NAME_PAID,
            },
            CROSS_COVER_PROJECTION = {
                    Contract.CrossCoverShifts.COLUMN_NAME_PAYMENT,
                    Contract.CrossCoverShifts.COLUMN_NAME_CLAIMED,
                    Contract.CrossCoverShifts.COLUMN_NAME_PAID
            },
            EXPENSES_PROJECTION = {
                    Contract.Expenses.COLUMN_NAME_PAYMENT,
                    Contract.Expenses.COLUMN_NAME_CLAIMED,
                    Contract.Expenses.COLUMN_NAME_PAID
            };

    private static final int
            ROSTERED_COLUMN_INDEX_ROSTERED_START = 0,
            ROSTERED_COLUMN_INDEX_ROSTERED_END = 1,
            ROSTERED_COLUMN_INDEX_LOGGED_START = 2,
            ROSTERED_COLUMN_INDEX_LOGGED_END = 3,
            ADDITIONAL_COLUMN_INDEX_START = 0,
            ADDITIONAL_COLUMN_INDEX_END = 1,
            ADDITIONAL_COLUMN_INDEX_RATE = 2,
            ADDITIONAL_COLUMN_INDEX_CLAIMED = 3,
            ADDITIONAL_COLUMN_INDEX_PAID = 4,
            SINGLE_PAYMENT_COLUMN_INDEX_PAYMENT = 0,
            SINGLE_PAYMENT_COLUMN_INDEX_CLAIMED = 1,
            SINGLE_PAYMENT_COLUMN_INDEX_PAID = 2,
            POSITION_ROSTERED_HEADER = 0,
            POSITION_ROSTERED_NUMBER = 1,
            POSITION_ROSTERED_DURATION = 2,
            POSITION_LOGGED_HEADER = 3,
            POSITION_LOGGED_NUMBER = 4,
            POSITION_LOGGED_DURATION = 5,
            POSITION_ADDITIONAL_HEADER = 6,
            POSITION_ADDITIONAL_NUMBER = 7,
            POSITION_ADDITIONAL_DURATION = 8,
            POSITION_ADDITIONAL_MONEY = 9,
            POSITION_CROSS_COVER_HEADER = 10,
            POSITION_CROSS_COVER_NUMBER = 11,
            POSITION_CROSS_COVER_MONEY = 12,
            POSITION_EXPENSES_HEADER = 13,
            POSITION_EXPENSES_NUMBER = 14,
            POSITION_EXPENSES_MONEY = 15,
            ITEM_COUNT = 16;

    private final Adapter mAdapter = new Adapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.summary_fragment, container, false);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (position) {
                        case POSITION_ROSTERED_HEADER:
                        case POSITION_LOGGED_HEADER:
                        case POSITION_ADDITIONAL_HEADER:
                        case POSITION_CROSS_COVER_HEADER:
                        case POSITION_EXPENSES_HEADER:
                            return gridLayoutManager.getSpanCount();
                        default:
                            return 1;
                    }
                }
            });
        }
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(SummaryActivity.LOADER_ID_ROSTERED, null, this);
        getLoaderManager().initLoader(SummaryActivity.LOADER_ID_ADDITIONAL, null, this);
        getLoaderManager().initLoader(SummaryActivity.LOADER_ID_CROSS_COVER, null, this);
        getLoaderManager().initLoader(SummaryActivity.LOADER_ID_EXPENSES, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri;
        String[] projection;
        switch (id) {
            case SummaryActivity.LOADER_ID_ROSTERED:
                contentUri = Provider.rosteredShiftsUri;
                projection = ROSTERED_PROJECTION;
                break;
            case SummaryActivity.LOADER_ID_ADDITIONAL:
                contentUri = Provider.additionalShiftsUri;
                projection = ADDITIONAL_PROJECTION;
                break;
            case SummaryActivity.LOADER_ID_CROSS_COVER:
                contentUri = Provider.crossCoverShiftsUri;
                projection = CROSS_COVER_PROJECTION;
                break;
            case SummaryActivity.LOADER_ID_EXPENSES:
                contentUri = Provider.expensesUri;
                projection = EXPENSES_PROJECTION;
                break;
            default:
                throw new IllegalStateException();
        }
//        Log.i(TAG, "onCreateLoader(): id = [" + id + "], contentUri = [" + contentUri + "]");
        return new CursorLoader(getActivity(), contentUri, projection, null, null, null);
    }

    void getRosteredData(@NonNull Cursor cursor) {
        int totalCount = 0, loggedCount = 0;
        Duration totalDuration = Duration.ZERO, loggedDuration = Duration.ZERO;
        if (cursor.moveToFirst()) {
            do {
                if (!cursor.isNull(ROSTERED_COLUMN_INDEX_LOGGED_START) && !cursor.isNull(ROSTERED_COLUMN_INDEX_LOGGED_END)) {
                    loggedCount++;
                    loggedDuration = loggedDuration.plus(new Duration(cursor.getLong(ROSTERED_COLUMN_INDEX_LOGGED_START), cursor.getLong(ROSTERED_COLUMN_INDEX_LOGGED_END)));
                }
                totalCount++;
                totalDuration = totalDuration.plus(new Duration(cursor.getLong(ROSTERED_COLUMN_INDEX_ROSTERED_START), cursor.getLong(ROSTERED_COLUMN_INDEX_ROSTERED_END)));
            } while (cursor.moveToNext());
        }
        mAdapter.update(POSITION_ROSTERED_NUMBER, new Adapter.Payload(totalCount));
        mAdapter.update(POSITION_ROSTERED_DURATION, new Adapter.Payload(totalDuration));
        mAdapter.update(POSITION_LOGGED_NUMBER, new Adapter.Payload(loggedCount));
        mAdapter.update(POSITION_LOGGED_DURATION, new Adapter.Payload(loggedDuration));
    }

    void getAdditionalData(@NonNull Cursor cursor) {
        int totalCount = 0, unclaimedCount = 0, claimedCount = 0, paidCount = 0;
        Duration totalDuration = Duration.ZERO, unclaimedDuration = Duration.ZERO, claimedDuration = Duration.ZERO, paidDuration = Duration.ZERO;
        BigDecimal totalMoney = BigDecimal.ZERO, unclaimedMoney = BigDecimal.ZERO, claimedMoney = BigDecimal.ZERO, paidMoney = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            do {
                Duration currentDuration = new Duration(cursor.getLong(ADDITIONAL_COLUMN_INDEX_START), cursor.getLong(ADDITIONAL_COLUMN_INDEX_END));
                BigDecimal currentMoney =
                        BigDecimal.valueOf(cursor.getInt(ADDITIONAL_COLUMN_INDEX_RATE), 2)
                                .multiply(BigDecimal.valueOf(currentDuration.getMillis()))
                                .divide(BigDecimal.valueOf(DateTimeConstants.MILLIS_PER_HOUR), 2, BigDecimal.ROUND_HALF_UP);
                if (!cursor.isNull(ADDITIONAL_COLUMN_INDEX_PAID)) {
                    paidCount++;
                    paidDuration = paidDuration.plus(currentDuration);
                    paidMoney = paidMoney.add(currentMoney);
                } else if (!cursor.isNull(ADDITIONAL_COLUMN_INDEX_CLAIMED)) {
                    claimedCount++;
                    claimedDuration = claimedDuration.plus(currentDuration);
                    claimedMoney = claimedMoney.add(currentMoney);
                } else {
                    unclaimedCount++;
                    unclaimedDuration = unclaimedDuration.plus(currentDuration);
                    unclaimedMoney = unclaimedMoney.add(currentMoney);
                }
                totalCount++;
                totalDuration = totalDuration.plus(currentDuration);
                totalMoney = totalMoney.add(currentMoney);
            } while (cursor.moveToNext());
        }
        mAdapter.update(POSITION_ADDITIONAL_NUMBER, new Adapter.Payload(totalCount, unclaimedCount, claimedCount, paidCount));
        mAdapter.update(POSITION_ADDITIONAL_DURATION, new Adapter.Payload(totalDuration, unclaimedDuration, claimedDuration, paidDuration));
        mAdapter.update(POSITION_ADDITIONAL_MONEY, new Adapter.Payload(totalMoney, unclaimedMoney, claimedMoney, paidMoney));
    }

    void getSinglePaymentData(int id, @NonNull Cursor cursor) {
        int totalCount = 0, unclaimedCount = 0, claimedCount = 0, paidCount = 0;
        BigDecimal totalMoney = BigDecimal.ZERO, unclaimedMoney = BigDecimal.ZERO, claimedMoney = BigDecimal.ZERO, paidMoney = BigDecimal.ZERO;
        if (cursor.moveToFirst()) {
            do {
                BigDecimal currentMoney = BigDecimal.valueOf(cursor.getInt(SINGLE_PAYMENT_COLUMN_INDEX_PAYMENT), 2);
                if (!cursor.isNull(SINGLE_PAYMENT_COLUMN_INDEX_PAID)) {
                    paidCount++;
                    paidMoney = paidMoney.add(currentMoney);
                } else if (!cursor.isNull(SINGLE_PAYMENT_COLUMN_INDEX_CLAIMED)) {
                    claimedCount++;
                    claimedMoney = claimedMoney.add(currentMoney);
                } else {
                    unclaimedCount++;
                    unclaimedMoney = unclaimedMoney.add(currentMoney);
                }
                totalCount++;
                totalMoney = totalMoney.add(currentMoney);
            } while (cursor.moveToNext());
        }
        mAdapter.update(id == SummaryActivity.LOADER_ID_CROSS_COVER ? POSITION_CROSS_COVER_NUMBER : POSITION_EXPENSES_NUMBER, new Adapter.Payload(totalCount, unclaimedCount, claimedCount, paidCount));
        mAdapter.update(id == SummaryActivity.LOADER_ID_CROSS_COVER ? POSITION_CROSS_COVER_MONEY : POSITION_EXPENSES_MONEY, new Adapter.Payload(totalMoney, unclaimedMoney, claimedMoney, paidMoney));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            int id = loader.getId();
            switch (id) {
                case SummaryActivity.LOADER_ID_ROSTERED:
                    getRosteredData(data);
                    return;
                case SummaryActivity.LOADER_ID_ADDITIONAL:
                    getAdditionalData(data);
                    return;
                case SummaryActivity.LOADER_ID_CROSS_COVER:
                case SummaryActivity.LOADER_ID_EXPENSES:
                    getSinglePaymentData(id, data);
                    return;
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case SummaryActivity.LOADER_ID_ROSTERED:
            case SummaryActivity.LOADER_ID_ADDITIONAL:
            case SummaryActivity.LOADER_ID_CROSS_COVER:
            case SummaryActivity.LOADER_ID_EXPENSES:
                return;
            default:
                throw new IllegalStateException();
        }
    }

    private static class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_TYPE_HEADER = 1, VIEW_TYPE_CONTENT = 2;
        private final Payload[] mPayloads = new Payload[ITEM_COUNT];

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_HEADER) {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false));
            } else if (viewType == VIEW_TYPE_CONTENT) {
                return new BodyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_list_item, parent, false));
            } else {
                throw new IllegalStateException();
            }
        }

        void update(int position, Payload payload) {
            mPayloads[position] = payload;
            notifyItemChanged(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int headerRes;
            if (position == POSITION_ROSTERED_HEADER) {
                headerRes = R.string.rostered_shifts;
            } else if (position == POSITION_LOGGED_HEADER) {
                headerRes = R.string.logged_shifts;
            } else if (position == POSITION_ADDITIONAL_HEADER) {
                headerRes = R.string.additional_shifts;
            } else if (position == POSITION_CROSS_COVER_HEADER) {
                headerRes = R.string.cross_cover_shifts;
            } else if (position == POSITION_EXPENSES_HEADER) {
                headerRes = R.string.expenses;
            } else {
                ((BodyViewHolder) holder).bindTo(mPayloads[position]);
                return;
            }
            ((HeaderViewHolder) holder).headerView.setText(headerRes);
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case POSITION_ROSTERED_HEADER:
                case POSITION_LOGGED_HEADER:
                case POSITION_ADDITIONAL_HEADER:
                case POSITION_CROSS_COVER_HEADER:
                case POSITION_EXPENSES_HEADER:
                    return VIEW_TYPE_HEADER;
                default:
                    return VIEW_TYPE_CONTENT;
            }
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }

        static class Payload {

            @StringRes
            private final int title;
            private final boolean showExtra;
            @NonNull
            private final String totalText;
            private String unclaimedText, claimedText, paidText;
            private long unclaimedPart, claimedPart, paidPart;

            private Payload(@StringRes int titleRes, @NonNull String totalText, boolean showExtra) {
                title = titleRes;
                this.totalText = totalText;
                this.showExtra = showExtra;
            }

            private Payload(int total, boolean showExtra) {
                this(R.string.number, String.format(Locale.US, "%d", total), showExtra);
            }

            private Payload(Duration total, boolean showExtra) {
                this(R.string.hours, String.format(Locale.US, "%.1f", (float) total.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR), showExtra);
            }

            private Payload(BigDecimal total, boolean showExtra) {
                this(R.string.money, String.format(Locale.US, "$%.2f", total), showExtra);
            }

            Payload(int total) {
                this(total, false);
            }

            Payload(int total, int unclaimed, int claimed, int paid) {
                this(total, total != 0);
                if (showExtra) {
                    unclaimedText = String.format(Locale.US, "Unclaimed: %d (%d%%)", unclaimed, 100 * unclaimed / total);
                    unclaimedPart = (long) unclaimed;
                    claimedText = String.format(Locale.US, "Claimed: %d (%d%%)", claimed, 100 * claimed / total);
                    claimedPart = (long) claimed;
                    paidText = String.format(Locale.US, "Paid: %d (%d%%)", paid, 100 * paid / total);
                    paidPart = (long) paid;
                }
            }

            Payload(BigDecimal total) {
                this(total, false);
            }

            Payload(BigDecimal total, BigDecimal unclaimed, BigDecimal claimed, BigDecimal paid) {
                this(total, !total.equals(BigDecimal.ZERO));
                if (showExtra) {
                    unclaimedPart = unclaimed.longValue();
                    unclaimedText = String.format(Locale.US, "Unclaimed: $%.2f (%d%%)", unclaimed, unclaimed.scaleByPowerOfTen(2).divide(total, 0, BigDecimal.ROUND_HALF_UP).intValue());
                    claimedPart = claimed.longValue();
                    claimedText = String.format(Locale.US, "Claimed: $%.2f (%d%%)", claimed, claimed.scaleByPowerOfTen(2).divide(total, 0, BigDecimal.ROUND_HALF_UP).intValue());
                    paidPart = paid.longValue();
                    paidText = String.format(Locale.US, "Paid: $%.2f (%d%%)", paid, paid.scaleByPowerOfTen(2).divide(total, 0, BigDecimal.ROUND_HALF_UP).intValue());
                }
            }

            Payload(Duration total) {
                this(total, false);
            }

            Payload(Duration total, Duration unclaimed, Duration claimed, Duration paid) {
                this(total, !total.isEqual(Duration.ZERO));
                if (showExtra) {
                    unclaimedPart = unclaimed.getMillis();
                    unclaimedText = String.format(Locale.US, "Unclaimed: %.1f (%d%%)", (float) unclaimed.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * unclaimed.getMillis() / total.getMillis());
                    claimedPart = claimed.getMillis();
                    claimedText = String.format(Locale.US, "Claimed: %.1f (%d%%)", (float) claimed.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * claimed.getMillis() / total.getMillis());
                    paidPart = paid.getMillis();
                    paidText = String.format(Locale.US, "Paid: %.1f (%d%%)", (float) paid.getStandardMinutes() / DateTimeConstants.MINUTES_PER_HOUR, 100 * paid.getMillis() / total.getMillis());
                }
            }

        }

        static class BodyViewHolder extends RecyclerView.ViewHolder {
            final TextView
                    titleView, totalView, unclaimedView, claimedView, paidView;
            final View
                    subtotalsView;
            final PieView
                    pieView;

            private BodyViewHolder(View itemView) {
                super(itemView);
                titleView = (TextView) itemView.findViewById(R.id.title);
                totalView = (TextView) itemView.findViewById(R.id.total);
                subtotalsView = itemView.findViewById(R.id.subtotals);
                unclaimedView = (TextView) subtotalsView.findViewById(R.id.unclaimed);
                claimedView = (TextView) subtotalsView.findViewById(R.id.claimed);
                paidView = (TextView) subtotalsView.findViewById(R.id.paid);
                pieView = (PieView) itemView.findViewById(R.id.pie);
            }

            void bindTo(@Nullable Payload payload) {
                if (payload == null) return;
                titleView.setText(payload.title);
                totalView.setText(payload.totalText);
                if (payload.showExtra) {
                    unclaimedView.setText(payload.unclaimedText);
                    claimedView.setText(payload.claimedText);
                    paidView.setText(payload.paidText);
                    subtotalsView.setVisibility(View.VISIBLE);
                    pieView.set(payload.unclaimedPart, payload.claimedPart, payload.paidPart);
                    pieView.setVisibility(View.VISIBLE);
                } else {
                    subtotalsView.setVisibility(View.GONE);
                    pieView.setVisibility(View.GONE);
                }
            }

        }

        static class HeaderViewHolder extends RecyclerView.ViewHolder {
            final TextView headerView;

            HeaderViewHolder(View itemView) {
                super(itemView);
                headerView = (TextView) itemView;
            }
        }
    }
}
