package com.skepticalone.mecachecker.data;


import android.app.Application;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;

import org.joda.time.DateTime;

import java.math.BigDecimal;

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
    BaseItemDao<ExpenseEntity> getDao() {
        return expenseDao;
    }

    @Override
    public void addNewItem() {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.insertItemSync(new ExpenseEntity(
                        newExpenseTitle,
                        new PaymentData(BigDecimal.ZERO, null, null),
                        null
                ));
            }
        });
    }

    @Override
    public void setPayment(final long id, @NonNull final BigDecimal payment) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.setPaymentSync(id, payment);
            }
        });
    }

    @Override
    public void setClaimed(final long id, final boolean claimed) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.setClaimedSync(id, claimed ? DateTime.now() : null);
            }
        });
    }

    @Override
    public void setPaid(final long id, final boolean paid) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.setPaidSync(id, paid ? DateTime.now() : null);
            }
        });
    }

    public void setTitle(final long id, @NonNull final String title) {
        runAsync(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws ShiftOverlapException {
                expenseDao.setTitleSync(id, title);
            }
        });
    }

}
