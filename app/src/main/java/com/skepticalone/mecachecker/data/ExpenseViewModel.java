package com.skepticalone.mecachecker.data;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

import java.math.BigDecimal;
import java.util.List;

public final class ExpenseViewModel extends AndroidViewModel
        implements ExpenseModel, PayableViewModel {

    private final ExpenseModel expenseCallbacks;
    private final PayableViewModel payableViewModel;

    public ExpenseViewModel(Application application) {
        super(application);
        ExpenseDao expenseDao = AppDatabase.getInstance(application).expenseDao();
        expenseCallbacks = new ExpenseComposition(application, expenseDao);
        payableViewModel = new PayableComposition(application, expenseDao);
    }

    @Override
    public void addNewItem() {
        expenseCallbacks.addNewItem();
    }

    @NonNull
    @Override
    public LiveData<List<ExpenseEntity>> getItems() {
        return expenseCallbacks.getItems();
    }

    @NonNull
    @Override
    public LiveData<ExpenseEntity> getItem(long id) {
        return expenseCallbacks.getItem(id);
    }

    @Override
    public void selectItem(long id) {
        expenseCallbacks.selectItem(id);
    }

    @NonNull
    @Override
    public LiveData<ExpenseEntity> getSelectedItem() {
        return expenseCallbacks.getSelectedItem();
    }

    @Override
    public void deleteItem(long id) {
        expenseCallbacks.deleteItem(id);
    }

    @Override
    public void setTitle(long id, @NonNull String title) {
        expenseCallbacks.setTitle(id, title);
    }

    @Override
    public void setComment(long id, @Nullable String comment) {
        expenseCallbacks.setComment(id, comment);
    }

    @Override
    public void setPayment(long id, @NonNull BigDecimal payment) {
        payableViewModel.setPayment(id, payment);
    }

    @Override
    public void setClaimed(long id, boolean claimed) {
        payableViewModel.setClaimed(id, claimed);
    }

    @Override
    public void setPaid(long id, boolean paid) {
        payableViewModel.setPaid(id, paid);
    }

    static final class ExpenseComposition extends ModelComposition<ExpenseEntity> implements ExpenseModel {

        private final ExpenseDao dao;
        private final String expenseTitle;

        ExpenseComposition(Application application, ExpenseDao dao) {
            super(application, dao);
            this.dao = dao;
            expenseTitle = application.getString(R.string.new_expense_title);
        }

        @Override
        public void addNewItem() {
            runAsync(new SQLiteTask() {
                @Override
                public void runSQLiteTask() throws ShiftOverlapException {
                    dao.insertItemSync(new ExpenseEntity(
                            expenseTitle,
                            new PaymentData(BigDecimal.ZERO, null, null),
                            null
                    ));
                }
            });
        }

        @Override
        public void setTitle(final long id, @NonNull final String title) {
            runAsync(new SQLiteTask() {
                @Override
                public void runSQLiteTask() throws ShiftOverlapException {
                    dao.setTitleSync(id, title);
                }
            });
        }

    }

}
