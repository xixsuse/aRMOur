package com.skepticalone.mecachecker.components.expenses;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import com.skepticalone.mecachecker.components.BaseFragment;
import com.skepticalone.mecachecker.components.ShiftListActivity;
import com.skepticalone.mecachecker.data.Contract;
import com.skepticalone.mecachecker.data.Provider;

public class ExpensesListFragment extends BaseFragment {

    private static final String[] PROJECTION = {
            Contract.Expenses._ID,
            Contract.Expenses.COLUMN_NAME_TITLE,
            Contract.Expenses.COLUMN_NAME_CLAIMED,
            Contract.Expenses.COLUMN_NAME_PAID,
            Contract.Expenses.COLUMN_NAME_COMMENT,
    };

    private static final int
            COLUMN_INDEX_ID = 0,
            COLUMN_INDEX_TITLE = 1,
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
    public final int getLayout() {
        return R.layout.recycler_view;
    }

    @Override
    public final int getLoaderId() {
        return ShiftListActivity.LOADER_ID_EXPENSES_LIST;
    }

    @Override
    public final int getTitle() {
        return R.string.expenses;
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
        inflater.inflate(R.menu.menu_with_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            startActivity(new Intent(getActivity(), ExpenseDetailActivity.class));
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Provider.expensesUri, PROJECTION, null, null, Contract.Expenses._ID);
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

        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.two_line_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
            //noinspection ConstantConditions
            mCursor.moveToPosition(position);
            holder.mPrimaryText.setText(mCursor.getString(COLUMN_INDEX_TITLE));
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
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ExpenseDetailActivity.class);
                        intent.putExtra(ExpenseDetailActivity.EXPENSE_ID, getItemId());
                        startActivity(intent);
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        getActivity().getContentResolver().delete(Provider.expenseUri(getItemId()), null, null);
                        return true;
                    }
                });
            }
        }
    }

}
