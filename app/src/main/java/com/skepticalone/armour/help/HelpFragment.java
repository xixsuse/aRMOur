package com.skepticalone.armour.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skepticalone.armour.adapter.HelpAdapter;
import com.skepticalone.armour.ui.common.MainActivity;

public final class HelpFragment extends Fragment {

    public static HelpFragment newInstance(int itemType) {
        Bundle args = new Bundle();
        args.putInt(MainActivity.ITEM_TYPE, itemType);
        HelpFragment fragment = new HelpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(HelpAdapter.newInstance(getActivity(), getArguments().getInt(MainActivity.ITEM_TYPE)));
        return recyclerView;
    }

}
