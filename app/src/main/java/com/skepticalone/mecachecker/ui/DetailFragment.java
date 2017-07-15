package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.Model;
import com.skepticalone.mecachecker.dialog.CommentDialogFragment;
import com.skepticalone.mecachecker.model.Item;

abstract class DetailFragment<ItemType extends Item, Entity extends ItemType, ViewModel extends Model<Entity>> extends LifecycleFragment
        implements ItemDetailAdapter.Callbacks, CommentDialogFragment.Callbacks {

    private static final String DIALOG_FRAGMENT = "DIALOG_FRAGMENT";
    private final ItemDetailAdapter<ItemType> mAdapter = createAdapter();
    private final IntentFilter mErrorIntentFilter = new IntentFilter(Constants.DISPLAY_ERROR);
    private SnackCallbacks mSnackCallbacks;
    private final BroadcastReceiver mErrorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mSnackCallbacks.showSnackbar(intent.getStringExtra(Intent.EXTRA_TEXT), "Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: 15/07/17
                }
            });
        }
    };
    private ViewModel mModel;

    @NonNull
    abstract ItemDetailAdapter<ItemType> createAdapter();

    @NonNull
    abstract ViewModel createViewModel();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSnackCallbacks = (SnackCallbacks) context;
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.detail_recycler, container, false);
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    final ViewModel getViewModel() {
        return mModel;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mModel = createViewModel();
        mModel.getSelectedItem().observe(this, new Observer<Entity>() {
                    @Override
                    public void onChanged(@Nullable Entity entity) {
                        if (entity != null) mAdapter.setItem(entity);
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mErrorReceiver, mErrorIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mErrorReceiver);
    }

    void showDialogFragment(AppCompatDialogFragment dialogFragment) {
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }

    @Override
    public final void changeComment(long itemId, @Nullable String currentComment) {
        showDialogFragment(CommentDialogFragment.newInstance(itemId, currentComment));
    }

    @Override
    public final void setComment(long itemId, @Nullable String trimmedComment) {
        getViewModel().setComment(itemId, trimmedComment);
    }

}
