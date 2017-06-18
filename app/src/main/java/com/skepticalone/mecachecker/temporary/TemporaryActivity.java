package com.skepticalone.mecachecker.temporary;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.Expense;
import com.skepticalone.mecachecker.data.ExpenseViewModel;

import java.util.List;

public class TemporaryActivity extends LifecycleActivity {

    private static final String TAG = "TemporaryActivity";
    private TextView itemCountView;
    private Button addButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.temporary_activity);
        itemCountView = findViewById(R.id.item_count);
        addButton = findViewById(R.id.add_button);
        final ExpenseViewModel model = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        model.getExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expenses) {
                if (expenses == null) {
                    itemCountView.setText("Loading");
                } else {
                    itemCountView.setText(Integer.toString(expenses.size()));
                }
            }
        });
    }


}
