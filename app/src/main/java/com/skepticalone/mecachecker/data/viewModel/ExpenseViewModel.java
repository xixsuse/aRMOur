package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.ExpenseDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

import java.math.BigDecimal;


public final class ExpenseViewModel extends ItemViewModel<ExpenseEntity, ExpenseDao>
        implements SingleAddViewModelContract<ExpenseEntity>, PayableViewModelContract<ExpenseEntity> {

    private final PayableHelper payableHelper;

    public ExpenseViewModel(Application application) {
        super(application);
        payableHelper = new PayableHelper(getDao());
    }

    @NonNull
    @Override
    ExpenseDao onCreateDao(@NonNull AppDatabase database) {
        return database.expenseDao();
    }

    @Override
    public void saveNewPayment(@NonNull BigDecimal payment) {
        payableHelper.saveNewPayment(getCurrentItemId(), payment);
    }

    @Override
    public void setClaimed(boolean claimed) {
        payableHelper.setClaimed(getCurrentItemId(), claimed);
    }

    @Override
    public void setPaid(boolean paid) {
        payableHelper.setPaid(getCurrentItemId(), paid);
    }

    @Override
    public void addNewItem() {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().insertItemSync(new ExpenseEntity(
                        getApplication().getString(R.string.new_expense_title),
                        new PaymentData(0),
                        null
                ));
            }
        });
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
