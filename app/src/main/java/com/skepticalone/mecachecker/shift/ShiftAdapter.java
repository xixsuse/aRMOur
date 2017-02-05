package com.skepticalone.mecachecker.shift;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;

class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {

    private final OnShiftClickListener mListener;
    private Cursor mCursor = null;

    ShiftAdapter(OnShiftClickListener listener) {
        super();
        setHasStableIds(true);
        mListener = listener;
    }

    void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(ShiftListActivity.COLUMN_INDEX_ID);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shift_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        final long id = mCursor.getLong(ShiftListActivity.COLUMN_INDEX_ID);
        holder.dateView.setText(mCursor.getString(ShiftListActivity.COLUMN_INDEX_FORMATTED_DATE));
        holder.startView.setText(mCursor.getString(ShiftListActivity.COLUMN_INDEX_FORMATTED_START));
        holder.endView.setText(mCursor.getString(ShiftListActivity.COLUMN_INDEX_FORMATTED_END));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onShiftClick(id);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onShiftLongClick(id);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    //
//    public long getLastShiftEndSeconds(){
//        mCursor.moveToLast();
//        return mCursor.get
//    }
//
    interface OnShiftClickListener {
        void onShiftClick(long id);

        void onShiftLongClick(long id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView
                dateView,
                startView,
                endView;

        ViewHolder(View view) {
            super(view);
            dateView = (TextView) view.findViewById(R.id.date);
            startView = (TextView) view.findViewById(R.id.start);
            endView = (TextView) view.findViewById(R.id.end);
        }
    }
}
