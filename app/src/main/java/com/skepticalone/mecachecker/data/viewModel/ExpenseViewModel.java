package com.skepticalone.mecachecker.data.viewModel;


import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.ExpenseDao;
import com.skepticalone.mecachecker.data.dao.ItemDaoContract;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

public final class ExpenseViewModel extends SingleAddItemViewModel<ExpenseEntity> {

    @NonNull
    private final ExpenseDao dao;
    @NonNull
    private final PayableModel payableModel;
    private final String expenseTitle;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application).expenseDao();
        payableModel = new PayableModel(dao);
        expenseTitle = application.getString(R.string.new_expense_title);
    }

    @NonNull
    @Override
    ItemDaoContract<ExpenseEntity> getDao() {
        return dao;
    }

    @NonNull
    public PayableModel getPayableModel() {
        return payableModel;
    }

    @Override
    public void addNewItem() {
        insertItem(new ExpenseEntity(
                expenseTitle,
                new PaymentData(0),
                null
        ));
    }

    @MainThread
    public void setTitle(final long id, @NonNull final String title) {
        runAsync(new SetTitleTask(dao, id, title));
    }

    static final class SetTitleTask extends ItemRunnable<ExpenseDao> {
        @NonNull
        private final String title;
        SetTitleTask(@NonNull ExpenseDao dao, long id, @NonNull String title) {
            super(dao, id);
            this.title = title;
        }
        @Override
        void run(@NonNull ExpenseDao dao, long id) {
            dao.setTitleSync(id, title);
        }
    }

}
