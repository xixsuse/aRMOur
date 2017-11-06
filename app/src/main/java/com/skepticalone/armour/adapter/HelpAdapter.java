package com.skepticalone.armour.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.skepticalone.armour.R;


public abstract class HelpAdapter extends ContextAdapter<HelpViewHolder> {

    static final HelpViewHolder.Binder FIRST_HELP_ITEM = new HelpViewHolder.Binder(false, R.drawable.ic_rostered_days_off_black_24dp, "Help item 1", "Description of this help item, which can be long and overflow onto multiple lines");

    HelpAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    public static HelpAdapter newInstance(@NonNull Context context, int itemType) {
        switch (itemType) {
            case R.id.rostered:
                return new RosteredShiftHelpAdapter(context);
            case R.id.additional:
                return new AdditionalShiftHelpAdapter(context);
            case R.id.cross_cover:
                return new CrossCoverHelpAdapter(context);
            case R.id.expenses:
                return new ExpenseHelpAdapter(context);
            default:
                throw new IllegalStateException();
        }
    }

    @NonNull
    abstract HelpViewHolder.Binder[] getRows();

    @LayoutRes
    @Override
    public final int getItemViewType(int position) {
        return getRows()[position].getItemViewType();
    }

    @Override
    public final HelpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HelpViewHolder(parent, viewType);
    }

    @Override
    public final void onBindViewHolder(HelpViewHolder holder, int position) {
        getRows()[position].bindViewHolder(holder);
    }

    @Override
    public final int getItemCount() {
        return getRows().length;
    }

}
