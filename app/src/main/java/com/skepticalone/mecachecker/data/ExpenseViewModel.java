package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();

    static {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    private final LiveData<List<Expense>> expenses;

    public ExpenseViewModel(Application application) {
        super(application);
        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());

        LiveData<Boolean> databaseCreated = databaseCreator.isDatabaseCreated();
        expenses = Transformations.switchMap(
                databaseCreated,
                new Function<Boolean, LiveData<List<Expense>>>() {
                    @Override
                    public LiveData<List<Expense>> apply(Boolean isDbCreated) {
                        if (!Boolean.TRUE.equals(isDbCreated)) { // Not needed here, but watch out for null
                            //noinspection unchecked
                            return ABSENT;
                        } else {
                            //noinspection ConstantConditions
                            return databaseCreator.getDatabase().expenseDao().getExpenses();
                        }
                    }
                }
        );
        databaseCreator.createDb(this.getApplication());

    }

    public LiveData<List<Expense>> getExpenses() {
        return expenses;
    }

    //    public LiveData<List<Expense>> getExpenses() {
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
