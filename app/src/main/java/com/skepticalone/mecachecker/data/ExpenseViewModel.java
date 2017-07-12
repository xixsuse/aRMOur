package com.skepticalone.mecachecker.data;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseViewModel extends PayableViewModel<ExpenseEntity> {
    private final ExpenseDao expenseDao;

    public ExpenseViewModel(Application application) {
        super(application);
        expenseDao = AppDatabase.getInstance(application).expenseDao();
    }

    @Override
    public LiveData<List<ExpenseEntity>> getItems() {
        return expenseDao.getExpenses();
    }

    @Override
    LiveData<ExpenseEntity> getItem(long id) {
        return expenseDao.getExpense(id);
    }

    @Override
    public void deleteItemSync(long id) {
        expenseDao.deleteExpense(id);
    }

    @Override
    void insertItemSync(@NonNull ExpenseEntity expense) {
        expenseDao.insertExpense(expense);
    }

    @Override
    ExpenseEntity generateRandomItem() {
        return DatabaseInitUtil.randomExpense();
    }

    @Override
    void setPaymentSync(long id, @NonNull BigDecimal payment) {
        expenseDao.setPayment(id, payment);
    }

    @Override
    void setCommentSync(long id, @Nullable String comment) {
        expenseDao.setComment(id, comment);
    }

    @Override
    void setClaimedSync(long id, @Nullable DateTime claimed) {
        expenseDao.setClaimed(id, claimed);
    }

    @Override
    void setPaidSync(long id, @Nullable DateTime paid) {
        expenseDao.setPaid(id, paid);
    }

    @MainThread
    public void setTitle(final long id, @NonNull final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                expenseDao.setTitle(id, title);
            }
        }).start();
    }

}
