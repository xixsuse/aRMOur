package com.skepticalone.mecachecker.data;


import android.app.Application;
import android.arch.lifecycle.LiveData;
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
    public void deleteItem(long id) {
        mExpenseDao.deleteExpense(id);
    }

    @Override
    public void addItem(ExpenseEntity expense) {
        mExpenseDao.insertExpense(expense);
    }

    public void setTitle(long id, @NonNull String title) {
        mExpenseDao.setTitle(id, title);
    }

    public void setClaimed(long id, boolean claimed) {
        mExpenseDao.setClaimed(id, claimed ? DateTime.now() : null);
    }

    public void setPaid(long id, boolean paid) {
        mExpenseDao.setPaid(id, paid ? DateTime.now() : null);
    }

    @Override
    ExpenseEntity generateRandomItem() {
        return DatabaseInitUtil.randomExpense();
    }
}
