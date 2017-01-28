package com.skepticalone.mecachecker.shift;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;

import java.util.ArrayList;
import java.util.List;

public class ShiftListActivity extends AppCompatActivity implements
        TimePickerFragment.OnShiftTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    private static final String SHIFT_DETAIL_FRAGMENT = "SHIFT_DETAIL_FRAGMENT";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_list_activity);
        View recyclerView = findViewById(R.id.shift_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        mTwoPane = findViewById(R.id.shift_detail_container) != null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getSupportFragmentManager().findFragmentByTag(SHIFT_DETAIL_FRAGMENT);
        if (fragment != null) {
            fragment.onDateSet(year, month, dayOfMonth);
        }
    }

    @Override
    public void onStartTimeSet(int hourOfDay, int minute) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getSupportFragmentManager().findFragmentByTag(SHIFT_DETAIL_FRAGMENT);
        if (fragment != null) {
            fragment.onStartTimeSet(hourOfDay, minute);
        }
    }

    @Override
    public void onEndTimeSet(int hourOfDay, int minute) {
        ShiftDetailFragment fragment = (ShiftDetailFragment) getSupportFragmentManager().findFragmentByTag(SHIFT_DETAIL_FRAGMENT);
        if (fragment != null) {
            fragment.onEndTimeSet(hourOfDay, minute);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter());
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<String> mValues;

        SimpleItemRecyclerViewAdapter() {
            mValues = new ArrayList<>();
            mValues.add("One");
            mValues.add("Two");
            mValues.add("Three");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shift_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = (TextView) view.findViewById(R.id.content);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTwoPane) {
                            ShiftDetailFragment fragment = new ShiftDetailFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.shift_detail_container, fragment, SHIFT_DETAIL_FRAGMENT)
                                    .commit();
                        } else {
                            Context context = v.getContext();
                            Intent intent = new Intent(context, ShiftDetailActivity.class);
                            context.startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
