package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.ExpenseCustomDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;

import java.math.BigDecimal;

public final class ExpenseViewModel extends ItemViewModel<ExpenseEntity> implements PayableItemViewModelContract<ExpenseEntity>, SingleAddItemViewModelContract<ExpenseEntity> {

    @NonNull
    private final PayableViewModelHelper payableViewModelHelper;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        this.payableViewModelHelper = new PayableViewModelHelper(getDao().getPayableDaoHelper());
    }

    @NonNull
    @Override
    ExpenseCustomDao getDao() {
        return AppDatabase.getInstance(getApplication()).expenseCustomDao();
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
