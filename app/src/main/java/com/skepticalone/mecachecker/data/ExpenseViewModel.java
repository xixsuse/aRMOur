package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.skepticalone.mecachecker.db.AppDatabase;
import com.skepticalone.mecachecker.db.DatabaseInitUtil;
import com.skepticalone.mecachecker.db.dao.ExpenseDao;
import com.skepticalone.mecachecker.db.entity.ExpenseEntity;

import java.util.List;
import java.util.Random;

public class ExpenseViewModel extends AndroidViewModel {

    private static final MutableLiveData<ExpenseEntity> NO_EXPENSE = new MutableLiveData<>();
    private static final MutableLiveData<List<ExpenseEntity>> NO_EXPENSES = new MutableLiveData<>();
    private final LiveData<List<ExpenseEntity>> expenses;
    private final LiveData<ExpenseEntity> selectedExpense;
    private final MutableLiveData<Long> selectedId = new MutableLiveData<>();
    private final Random mRandom = new Random();

    public ExpenseViewModel(final Application application) {
        super(application);
        expenses = getExpenseDao().getExpenses();
        selectedExpense = Transformations.switchMap(selectedId, new Function<Long, LiveData<ExpenseEntity>>() {
            @Override
            public LiveData<ExpenseEntity> apply(Long id) {
                return id == null ? NO_EXPENSE : getExpenseDao().getExpense(id);
            }
        });
    }

    private ExpenseDao getExpenseDao() {
        return AppDatabase.getInstance(getApplication()).expenseDao();
    }

    public LiveData<List<ExpenseEntity>> getExpenses() {
        return expenses;
    }

    public LiveData<ExpenseEntity> getSelectedExpense() {
        return selectedExpense;
    }

    public void selectExpense(long id) {
        selectedId.setValue(id);
    }

    public void addExpense() {
        getExpenseDao().insertExpense(DatabaseInitUtil.randomExpense(mRandom));
    }

    //    public LiveData<List<ExpenseEntity>> getExpenses() {
//        if (expenses == null) {
//            expenses = new MutableLiveData<>();
//            loadExpenses();
//        }
//        return expenses;
//    }
//    private void loadExpenses() {
//        // TODO: 18/06/17: do async operation to fetch expenses
//    }

}
