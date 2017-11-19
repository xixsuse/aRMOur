package com.skepticalone.armour.help;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.R;

abstract class HelpFragment extends Fragment {

    public static HelpFragment newInstance(@IdRes int itemType) {
        if (itemType == R.id.rostered) return new RosteredShiftHelpFragment();
        if (itemType == R.id.additional) return new AdditionalShiftHelpFragment();
        if (itemType == R.id.cross_cover) return new CrossCoverHelpFragment();
        if (itemType == R.id.expenses) return new ExpenseHelpFragment();
        throw new IllegalStateException();
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.help_fragment, container, false);
        onAddToView(inflater, (ViewGroup) layout.findViewById(R.id.help_items_container));
        return layout;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getString(R.string.help_qualified, getString(getTitle())));
    }

    @StringRes
    abstract int getTitle();

    abstract void onAddToView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container);

}
