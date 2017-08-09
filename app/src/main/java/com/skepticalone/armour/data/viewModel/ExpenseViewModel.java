package com.skepticalone.armour.data.viewModel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.skepticalone.armour.R;
import com.skepticalone.armour.data.dao.ExpenseDao;
import com.skepticalone.armour.data.db.AppDatabase;
import com.skepticalone.armour.data.entity.ExpenseEntity;

import java.math.BigDecimal;

public final class ExpenseViewModel extends ItemViewModel<ExpenseEntity> implements PayableViewModelContract<ExpenseEntity>, SingleAddItemViewModelContract<ExpenseEntity> {

    @NonNull
    private final PayableViewModelHelper payableViewModelHelper;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        this.payableViewModelHelper = new PayableViewModelHelper(getDao().getPayableDaoHelper());
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
                postSelectedId(getDao().insertSync(getApplication().getString(R.string.new_expense_title)));
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

}
