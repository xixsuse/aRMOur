package com.skepticalone.mecachecker.data;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.model.CrossCover;

import org.joda.time.LocalDate;

public interface CrossCoverModel extends Model<CrossCover>, SingleAddModel {

    @MainThread
    void setDate(long id, @NonNull LocalDate date);

}
