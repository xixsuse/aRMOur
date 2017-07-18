package com.skepticalone.mecachecker.data.viewModel;


import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.ExpenseDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

public final class ExpenseViewModel extends SingleAddItemViewModel<ExpenseEntity, ExpenseDao> {

    private final PayableModel payableModel;
    private final String expenseTitle;

    public ExpenseViewModel(@NonNull Application application) {
        super(application, AppDatabase.getInstance(application).expenseDao());
        payableModel = new PayableModel(getDao());
        expenseTitle = application.getString(R.string.new_expense_title);
    }

    public final PayableModel getPayableModel() {
        return payableModel;
    }

    @Override
    public void addNewItem() {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().insertItemSync(new ExpenseEntity(
                        expenseTitle,
                        new PaymentData(0),
                        null
                ));
            }
        });
    }

    @MainThread
    public void setTitle(final long id, @NonNull final String title) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setTitleSync(id, title);
            }
        });
    }

}
