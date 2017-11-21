package com.skepticalone.armour.help;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.R;
import com.skepticalone.armour.ui.common.MainActivity;

public final class HelpFragment extends Fragment {

    public static HelpFragment newInstance(@IdRes int itemType) {
        Bundle args = new Bundle();
        args.putInt(MainActivity.ITEM_TYPE, itemType);
        HelpFragment fragment = new HelpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.help_recycler, container, false);
        final HelpAdapter adapter;
        switch (getArguments().getInt(MainActivity.ITEM_TYPE, 0)) {
            case R.id.rostered:
                adapter = new RosteredShiftHelpAdapter();
                break;
            case R.id.additional:
                adapter = new AdditionalShiftHelpAdapter();
                break;
            case R.id.cross_cover:
                adapter = new CrossCoverHelpAdapter();
                break;
            case R.id.expenses:
                adapter = new ExpensesHelpAdapter();
                break;
            default:
                throw new IllegalStateException();
        }
        recyclerView.addItemDecoration(new HelpItemDivider(getActivity(), adapter));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final @StringRes int title;
        switch (getArguments().getInt(MainActivity.ITEM_TYPE, 0)) {
            case R.id.rostered:
                title = R.string.rostered_shifts;
                break;
            case R.id.additional:
                title = R.string.additional_shifts;
                break;
            case R.id.cross_cover:
                title = R.string.cross_cover_shifts;
                break;
            case R.id.expenses:
                title = R.string.expenses;
                break;
            default:
                throw new IllegalStateException();
        }
        getActivity().setTitle(getString(R.string.help_qualified, getString(title)));
    }

}
