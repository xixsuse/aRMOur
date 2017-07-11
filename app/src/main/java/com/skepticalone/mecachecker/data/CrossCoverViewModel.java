package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public class CrossCoverViewModel extends ItemViewModel<CrossCoverEntity> {
    private static final String TAG = "CrossCoverViewModel";

    private final CrossCoverDao crossCoverDao;

    CrossCoverViewModel(Application application) {
        super(application);
        crossCoverDao = AppDatabase.getInstance(application).crossCoverDao();
    }

    @Override
    public LiveData<List<CrossCoverEntity>> getItems() {
        return crossCoverDao.getCrossCoverShifts();
    }

    @Override
    LiveData<CrossCoverEntity> getItem(long id) {
        return crossCoverDao.getCrossCoverShift(id);
    }

    @Override
    public void deleteItemSync(long id) {
        crossCoverDao.deleteCrossCoverShift(id);
    }

    @Override
    void insertItemSync(@NonNull CrossCoverEntity crossCoverShift) {
        crossCoverDao.insertCrossCoverShift(crossCoverShift);
    }

    @Override
    CrossCoverEntity generateRandomItem() {
        return DatabaseInitUtil.randomCrossCoverShift();
    }

    @MainThread
    public void setDate(final long id, @NonNull final LocalDate date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    crossCoverDao.setDate(id, date);
                } catch (SQLiteConstraintException e) {
                    Log.e(TAG, "setDate: ", e);
                }
            }
        }).start();
    }

    @MainThread
    public void setClaimed(final long id, final boolean claimed) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                crossCoverDao.setClaimed(id, claimed ? DateTime.now() : null);
            }
        }).start();
    }

    @MainThread
    public void setPaid(final long id, final boolean paid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                crossCoverDao.setPaid(id, paid ? DateTime.now() : null);
            }
        }).start();
    }
}
