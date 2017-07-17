package com.skepticalone.mecachecker.data;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

import java.math.BigDecimal;
import java.util.List;

public final class ExpenseViewModel extends AndroidViewModel
        implements ExpenseModel, PayableModel, Model<ExpenseEntity> {

    private final ExpenseModel expenseModel;
    private final PayableModel payableModel;

    public ExpenseViewModel(Application application) {
        super(application);
        ExpenseDao expenseDao = AppDatabase.getInstance(application).expenseDao();
        expenseModel = new ExpenseComposition(application, expenseDao);
        payableModel = new PayableComposition(application, expenseDao);
    }

    @Override
    public void insertItem(@NonNull ExpenseEntity item) {
        expenseModel.insertItem(item);
    }

    @NonNull
    @Override
    public MutableLiveData<ExpenseEntity> getLastDeletedItem() {
        return expenseModel.getLastDeletedItem();
    }

    @Override
    public void addNewItem() {
        expenseModel.addNewItem();
    }

    @NonNull
    @Override
    public LiveData<List<ExpenseEntity>> getItems() {
        return expenseModel.getItems();
    }

    @NonNull
    @Override
    public LiveData<ExpenseEntity> getItem(long id) {
        return expenseModel.getItem(id);
    }

    @Override
    public void selectItem(long id) {
        expenseModel.selectItem(id);
    }

    @NonNull
    @Override
    public LiveData<ExpenseEntity> getSelectedItem() {
        return expenseModel.getSelectedItem();
    }

    @Override
    public void deleteItem(long id) {
        expenseModel.deleteItem(id);
    }

    @Override
    public void setTitle(long id, @NonNull String title) {
        expenseModel.setTitle(id, title);
    }

    @Override
    public void setComment(long id, @Nullable String comment) {
        expenseModel.setComment(id, comment);
    }

    @Override
    public void setPayment(long id, @NonNull BigDecimal payment) {
        payableModel.setPayment(id, payment);
    }

    @Override
    public void setClaimed(long id, boolean claimed) {
        payableModel.setClaimed(id, claimed);
    }

    @Override
    public void setPaid(long id, boolean paid) {
        payableModel.setPaid(id, paid);
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
