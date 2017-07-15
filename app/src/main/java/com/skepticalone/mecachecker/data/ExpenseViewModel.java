package com.skepticalone.mecachecker.data;


import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.List;

public final class ExpenseViewModel extends ItemViewModel<ExpenseEntity>
        implements PayableItemViewModel<ExpenseEntity>, SingleAddItemViewModel<ExpenseEntity> {
    private final String newExpenseTitle;
    private final ExpenseDao expenseDao;

    public ExpenseViewModel(Application application) {
        super(application);
        expenseDao = AppDatabase.getInstance(application).expenseDao();
        newExpenseTitle = application.getString(R.string.new_expense_title);
    }

    @Override
    public LiveData<List<ExpenseEntity>> getItems() {
        return expenseDao.getExpenses();
    }

    @Override
    public LiveData<ExpenseEntity> getItem(long id) {
        return expenseDao.getExpense(id);
    }

    @Override
    public void addNewItem() {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.insertExpense(new ExpenseEntity(
                        newExpenseTitle,
                        new PaymentData(BigDecimal.ZERO, null, null),
                        null
                ));
            }
        });
    }

    @Override
    public void deleteItem(final long id) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.deleteExpense(id);
            }
        });
    }

    @Override
    public void setPayment(final long id, @NonNull final BigDecimal payment) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.setPayment(id, payment);
            }
        });
    }

    @Override
    public void setClaimed(final long id, final boolean claimed) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.setClaimed(id, claimed ? DateTime.now() : null);
            }
        });
    }

    @Override
    public void setPaid(final long id, final boolean paid) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.setPaid(id, paid ? DateTime.now() : null);
            }
        });
    }

    @Override
    public void setComment(final long id, @Nullable final String comment) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.setComment(id, comment);
            }
        });
    }

    public void setTitle(final long id, @NonNull final String title) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.setTitle(id, title);
            }
        });
    }

}
