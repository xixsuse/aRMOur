package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

public abstract class ItemDetailAdapter<FinalItem> extends ObservableAdapter<FinalItem> {

    @Nullable
    private List<ItemViewHolder.Binder> mList;

    ItemDetailAdapter(@NonNull Context context) {
        super(context);
    }

    private static int getItemCount(@Nullable List<ItemViewHolder.Binder> list) {
        return list == null ? 0 : list.size();
    }

    @Override
    public final void onChanged(@Nullable FinalItem item) {
        final List<ItemViewHolder.Binder> newList = item == null ? null : getNewList(item);
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return getItemCount(mList);
            }

            @Override
            public int getNewListSize() {
                return getItemCount(newList);
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions
                return mList.get(oldItemPosition).areItemsTheSame(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                //noinspection ConstantConditions
                return mList.get(oldItemPosition).areContentsTheSame(newList.get(newItemPosition));
            }

        }, false).dispatchUpdatesTo(this);
        mList = newList;
    }

    @Override
    public final void onBindViewHolder(ItemViewHolder holder, int position) {
        //noinspection ConstantConditions
        mList.get(position).onBindViewHolder(holder);
    }

    @NonNull
    abstract List<ItemViewHolder.Binder> getNewList(@NonNull FinalItem item);

    @Override
    public final int getItemCount() {
        return getItemCount(mList);
    }

    public interface Callbacks extends CommentBinder.Callbacks {
    }
}
