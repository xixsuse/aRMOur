package com.skepticalone.mecachecker.components;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.ShiftContract;
import com.skepticalone.mecachecker.data.ShiftProvider;
import com.skepticalone.mecachecker.util.DateTimeUtils;

import org.joda.time.DateTime;


public class CrossCoverListFragment extends BaseFragment {

    private static final String[] PROJECTION = {
            ShiftContract.CrossCoverShifts._ID,
            ShiftContract.CrossCoverShifts.COLUMN_NAME_DATE,
            ShiftContract.CrossCoverShifts.COLUMN_NAME_CLAIMED,
            ShiftContract.CrossCoverShifts.COLUMN_NAME_PAID,
            ShiftContract.CrossCoverShifts.COLUMN_NAME_COMMENT,
    };

    private static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_DATE = 1,
            COLUMN_INDEX_CLAIMED = 2,
            COLUMN_INDEX_PAID = 3,
            COLUMN_INDEX_COMMENT = 4;

    private final Adapter mAdapter = new Adapter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    final int getLayout() {
        return R.layout.shift_list_fragment;
    }

    @Override
    final int getLoaderId() {
        return ShiftListActivity.LOADER_ID_CROSS_COVER_LIST;
    }

    @Override
    final int getTitle() {
        return R.string.cross_cover;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL)
        );
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cross_cover_shift_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_cross_cover) {
            ContentValues values = new ContentValues();
            values.put(ShiftContract.CrossCoverShifts.COLUMN_NAME_DATE, mAdapter.getNewShiftDate().getMillis());
            values.put(ShiftContract.CrossCoverShifts.COLUMN_NAME_RATE, PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.key_cross_cover_payment), getResources().getInteger(R.integer.default_cross_cover_payment)));
            getActivity().getContentResolver().insert(ShiftProvider.crossCoverShiftsUri, values);
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ShiftProvider.crossCoverShiftsUri, PROJECTION, null, null, ShiftContract.CrossCoverShifts.COLUMN_NAME_DATE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private
        @Nullable
        Cursor mCursor;

        Adapter() {
            super();
            setHasStableIds(true);
        }

        void swapCursor(@Nullable Cursor cursor) {
            mCursor = cursor;
            notifyDataSetChanged();
        }

        DateTime getNewShiftDate() {
            if (mCursor != null) {
                return (mCursor.moveToLast() ? new DateTime(mCursor.getLong(COLUMN_INDEX_DATE)).plusDays(1) : new DateTime()).withTimeAtStartOfDay();
            } else throw new IllegalStateException();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cross_cover_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            //noinspection ConstantConditions
            mCursor.moveToPosition(position);
            holder.mPrimaryText.setText(DateTimeUtils.getFullDateString(new DateTime(mCursor.getLong(COLUMN_INDEX_DATE))));
            if (mCursor.isNull(COLUMN_INDEX_COMMENT)) {
                holder.mSecondaryText.setVisibility(View.GONE);
            } else {
                holder.mSecondaryText.setText(mCursor.getString(COLUMN_INDEX_COMMENT));
                holder.mSecondaryText.setVisibility(View.VISIBLE);
            }
            holder.mIcon.setImageResource(mCursor.isNull(COLUMN_INDEX_PAID) ? mCursor.isNull(COLUMN_INDEX_CLAIMED) ? R.drawable.ic_check_box_empty_black_24dp : R.drawable.ic_check_box_half_black_24dp : R.drawable.ic_check_box_full_black_24dp);
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        @Override
        public long getItemId(int position) {
            //noinspection ConstantConditions
            mCursor.moveToPosition(position);
            return mCursor.getLong(COLUMN_INDEX_ID);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView mPrimaryText, mSecondaryText;
            private final ImageView mIcon;

            ViewHolder(View itemView) {
                super(itemView);
                mPrimaryText = (TextView) itemView.findViewById(R.id.primary_text);
                mSecondaryText = (TextView) itemView.findViewById(R.id.secondary_text);
                mIcon = (ImageView) itemView.findViewById(R.id.icon);
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        getActivity().getContentResolver().delete(ShiftProvider.crossCoverShiftUri(getItemId()), null, null);
                        return true;
                    }
                });
            }
        }
    }
}
