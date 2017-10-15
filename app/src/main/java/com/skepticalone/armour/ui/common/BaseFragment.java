package com.skepticalone.armour.ui.common;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.skepticalone.armour.data.viewModel.ItemViewModelContract;

public abstract class BaseFragment<FinalItem> extends Fragment {

    private static final String DIALOG_FRAGMENT = "DIALOG_FRAGMENT";

    @LayoutRes
    protected abstract int getLayout();

    protected abstract void onCreateAdapter(@NonNull Context context);

    @NonNull
    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract void onCreateViewModel(@NonNull ViewModelProvider viewModelProvider);

    @NonNull
    protected abstract ItemViewModelContract<FinalItem> getViewModel();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onCreateAdapter(context);
    }

    @NonNull
    @Override
    @CallSuper
    public RecyclerView onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(getLayout(), container, false);
        recyclerView.setAdapter(getAdapter());
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCreateViewModel(ViewModelProviders.of(getActivity()));
    }

    protected final void showDialogFragment(AppCompatDialogFragment dialogFragment) {
        dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }

}
