package com.skepticalone.mecachecker.components.summary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;

import java.util.Locale;

public class SummaryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Adapter mAdapter = new AdditionalShiftsAdapter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.summary_fragment, container, false);
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(SummaryActivity.LOADER_ID_ADDITIONAL, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
//            case SummaryActivity.LOADER_ID_ROSTERED:
//                break;
            case SummaryActivity.LOADER_ID_ADDITIONAL:
                return new CursorLoader(getActivity(), ShiftProvider.additionalShiftsUri, AdditionalShiftsAdapter.PROJECTION, null, null, ShiftContract.AdditionalShifts.COLUMN_NAME_START);
//            case SummaryActivity.LOADER_ID_CROSS_COVER:
//                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private static abstract class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        @Nullable
        Cursor mCursor = null;

        Adapter() {
            super();
        }

        @Override
        public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_list_item, parent, false));
        }

        void swapCursor(@Nullable Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView primaryText, secondaryText;

            ViewHolder(View itemView) {
                super(itemView);
                primaryText = (TextView) itemView.findViewById(R.id.primary_text);
                secondaryText = (TextView) itemView.findViewById(R.id.secondary_text);
            }
        }
    }

    private static class AdditionalShiftsAdapter extends Adapter {

        static final String[] PROJECTION = {
                ShiftContract.AdditionalShifts.COLUMN_NAME_START,
                ShiftContract.AdditionalShifts.COLUMN_NAME_END,
                ShiftContract.AdditionalShifts.COLUMN_NAME_RATE,
                ShiftContract.AdditionalShifts.COLUMN_NAME_CLAIMED,
                ShiftContract.AdditionalShifts.COLUMN_NAME_PAID,
        };
        private static final int
                COLUMN_INDEX_START = 0,
                COLUMN_INDEX_END = 1,
                COLUMN_INDEX_RATE = 2,
                COLUMN_INDEX_CLAIMED = 3,
                COLUMN_INDEX_PAID = 4;

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                case 3:
                    holder.primaryText.setText(position == 0 ? "All" : position == 1 ? "Unclaimed" : position == 2 ? "Claimed" : "Paid");
                    int count = 0;
                    Duration totalDuration = Duration.ZERO;
                    int totalPaymentInCents = 0;
                    //noinspection ConstantConditions
                    if (mCursor.moveToFirst()) {
                        do {
                            if (
                                    (position == 1 && !mCursor.isNull(COLUMN_INDEX_CLAIMED)) ||
                                            (position == 2 && (mCursor.isNull(COLUMN_INDEX_CLAIMED) || !mCursor.isNull(COLUMN_INDEX_PAID))) ||
                                            (position == 3 && (mCursor.isNull(COLUMN_INDEX_CLAIMED) || mCursor.isNull(COLUMN_INDEX_PAID)))
                                    ) continue;
                            Duration current = new Duration(mCursor.getLong(COLUMN_INDEX_START), mCursor.getLong(COLUMN_INDEX_END));
                            totalDuration = totalDuration.plus(current);
                            totalPaymentInCents += Math.round(mCursor.getInt(COLUMN_INDEX_RATE) * (float) current.getMillis() / DateTimeConstants.MILLIS_PER_HOUR);
                            count++;
                        } while (mCursor.moveToNext());
                    }
                    holder.secondaryText.setText(String.format(Locale.US, "%d shifts (%s) $%.2f", count, DateTimeUtils.getPeriodString(totalDuration.toPeriod()), totalPaymentInCents / 100f));
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : 4;
        }
    }
}
