package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.StringRes;

import java.util.Observable;
import java.util.Observer;


public abstract class ErrorMessageObserver implements Observer {

    @Override
    public final void update(Observable observable, Object o) {
        update(((ErrorMessageObservable) observable).getErrorMessage());
    }

    public abstract void update(@StringRes int errorMessage);

}
