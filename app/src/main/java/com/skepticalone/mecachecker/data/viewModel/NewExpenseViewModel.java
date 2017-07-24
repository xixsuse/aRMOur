package com.skepticalone.mecachecker.data.viewModel;

import android.app.Application;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.dao.ExpenseDao;
import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.entity.ExpenseEntity;
import com.skepticalone.mecachecker.dialog.PayableViewModel;
import com.skepticalone.mecachecker.dialog.TitleViewModel;

import java.math.BigDecimal;


public final class NewExpenseViewModel extends NewBaseViewModel<ExpenseEntity, ExpenseDao>
        implements TitleViewModel, PayableViewModel<ExpenseEntity> {

    public NewExpenseViewModel(Application application) {
        super(application);
    }

    @NonNull
    @Override
    ExpenseDao onCreateDao(@NonNull AppDatabase database) {
        return database.expenseDao();
    }

    @Override
    public void saveNewTitle(@NonNull String newTitle) {
        ExpenseEntity expense = getCurrentItem().getValue();
        if (expense != null) {
            getDao().setTitleSync(expense.getId(), newTitle);
        }
    }

    @Override
    public void saveNewPayment(@NonNull BigDecimal payment) {
        ExpenseEntity expense = getCurrentItem().getValue();
        if (expense != null) {
            getDao().setPaymentSync(expense.getId(), payment);
        }
    }

}
