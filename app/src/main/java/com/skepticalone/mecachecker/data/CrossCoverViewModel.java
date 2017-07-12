package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public class CrossCoverViewModel extends PayableViewModel<CrossCoverEntity> {
    private static final String TAG = "CrossCoverViewModel";

    private final CrossCoverDao crossCoverDao;
    private final String errorMessage;

    CrossCoverViewModel(Application application) {
        super(application);
        crossCoverDao = AppDatabase.getInstance(application).crossCoverDao();
        errorMessage = application.getString(R.string.cross_cover_already_exists_date);
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

    @Override
    void setCommentSync(long id, @Nullable String comment) {
        crossCoverDao.setComment(id, comment);
    }

    @Override
    void setClaimedSync(long id, @Nullable DateTime claimed) {
        crossCoverDao.setClaimed(id, claimed);
    }

    @Override
    void setPaidSync(long id, @Nullable DateTime paid) {
        crossCoverDao.setPaid(id, paid);
    }

    @MainThread
    public void setDate(final long id, @NonNull final LocalDate date) {
        new SQLiteThread(new SQLiteTask() {
            @Override
            public void runSQLiteTask() throws SQLiteConstraintException {
                crossCoverDao.setDate(id, date);
            }

            @NonNull
            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

        }).start();
    }

}
