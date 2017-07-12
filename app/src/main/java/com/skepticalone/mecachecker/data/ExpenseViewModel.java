package com.skepticalone.mecachecker.data;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.util.List;

public class ExpenseViewModel extends PayableViewModel<ExpenseEntity> {
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

    @Override
    void setCommentSync(long id, @Nullable String comment) {
        mExpenseDao.setComment(id, comment);
    }

    @Override
    void setClaimedSync(long id, @Nullable DateTime claimed) {
        mExpenseDao.setClaimed(id, claimed);
    }

    @Override
    void setPaidSync(long id, @Nullable DateTime paid) {
        mExpenseDao.setPaid(id, paid);
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

}
