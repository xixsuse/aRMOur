package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.dao.ExpenseDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.data.util.PaymentData;

public final class ExpenseViewModel extends PayableViewModel<ExpenseEntity, ExpenseDao>
        implements SingleAddViewModelContract<ExpenseEntity> {

    public ExpenseViewModel(Application application) {
        super(application);
    }

    @NonNull
    @Override
    ExpenseDao onCreateDao(@NonNull AppDatabase database) {
        return database.expenseDao();
    }

    @Override
    public void addNewItem() {
        runAsync(new Runnable() {
            @Override
            public void run() {
                selectedId.postValue(getDao().insertItemSync(new ExpenseEntity(
                        getApplication().getString(R.string.new_expense_title),
                        PaymentData.fromPayment(0),
                        null
                )));
            }
        });
    }

    public void saveNewTitle(final long id, @NonNull final String newTitle) {
        runAsync(new Runnable() {
            @Override
            public void run() {
                getDao().setTitleSync(id, newTitle);
            }
        });
    }

}
