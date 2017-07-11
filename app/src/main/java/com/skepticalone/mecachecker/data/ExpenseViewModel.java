package com.skepticalone.mecachecker.data;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.List;

public class ExpenseViewModel extends ItemViewModel<ExpenseEntity> {
    private final ExpenseDao mExpenseDao;

    public ExpenseViewModel(Application application) {
        super(application);
        mExpenseDao = AppDatabase.getInstance(application).expenseDao();
    }

    @Override
    public LiveData<List<ExpenseEntity>> getItems() {
        return mExpenseDao.getExpenses();
    }

    @Override
    LiveData<ExpenseEntity> getItem(long id) {
        return mExpenseDao.getExpense(id);
    }

    @Override
    public void deleteItemSync(long id) {
        mExpenseDao.deleteExpense(id);
    }

    @Override
    void insertItemSync(@NonNull ExpenseEntity expense) {
        mExpenseDao.insertExpense(expense);
    }

    @Override
    ExpenseEntity generateRandomItem() {
        return DatabaseInitUtil.randomExpense();
    }

    @MainThread
    public void setTitle(final long id, @NonNull final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mExpenseDao.setTitle(id, title);
            }
        }).start();
    }

    @MainThread
    public void setClaimed(final long id, final boolean claimed) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mExpenseDao.setClaimed(id, claimed ? DateTime.now() : null);
            }
        }).start();
    }

    @MainThread
    public void setPaid(final long id, final boolean paid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mExpenseDao.setPaid(id, paid ? DateTime.now() : null);
            }
        }).start();
    }
}
