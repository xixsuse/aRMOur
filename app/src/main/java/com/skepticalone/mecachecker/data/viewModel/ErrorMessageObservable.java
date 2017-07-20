package com.skepticalone.mecachecker.data.viewModel;

import android.support.annotation.StringRes;

import java.util.Observable;

public final class ErrorMessageObservable extends Observable {

    @StringRes
    private int errorMessage;

    ErrorMessageObservable() {
        super();
    }

    @StringRes
    final int getErrorMessage() {
        return errorMessage;
    }

    final void setErrorMessage(@StringRes int errorMessage) {
        this.errorMessage = errorMessage;
        setChanged();
        notifyObservers();
    }
}
