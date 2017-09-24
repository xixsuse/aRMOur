package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.ExpenseDao;
import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.model.Expense;
import com.skepticalone.armour.data.model.LiveExpenses;
import com.skepticalone.armour.data.model.RawExpenseEntity;

import java.math.BigDecimal;
import java.util.List;

public final class ExpenseViewModel extends ItemViewModel<RawExpenseEntity, Expense> implements PayableViewModelContract<Expense>, SingleAddItemViewModelContract<Expense> {

    @NonNull
    private final LiveData<List<Expense>> expenses;

    @NonNull
    private final PayableViewModelHelper payableViewModelHelper;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        payableViewModelHelper = new PayableViewModelHelper(getDao());
        expenses = new LiveExpenses(getApplication(), getDao().fetchItems());
    }

    @NonNull
    @Override
    ExpenseDao getDao() {
        return AppDatabase.getInstance(getApplication()).expenseDao();
    }

    @Override
    public void addNewItem() {
        runAsync(new Runnable() {
            @Override
            public void run() {
                postSelectedId(getDao().insertSync(
                        getApplication().getString(R.string.new_expense_title))
                );
            }
        });
    }

    @Override
    public void setClaimed(boolean claimed) {
        payableViewModelHelper.setClaimed(getCurrentItemId(), claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        payableViewModelHelper.setPaid(getCurrentItemId(), paid);
    }

    @Override
    public void saveNewPayment(@NonNull BigDecimal payment) {
        payableViewModelHelper.saveNewPayment(getCurrentItemId(), payment);
    }

    public void saveNewTitle(@NonNull final String newTitle) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setTitleSync(getCurrentItemId(), newTitle);
            }
        });
    }

    @NonNull
    @Override
    public LiveData<List<Expense>> getItems() {
        return expenses;
    }

}
