package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.db.AppDatabase;
import com.skepticalone.mecachecker.db.DatabaseInitUtil;
import com.skepticalone.mecachecker.db.dao.CrossCoverDao;
import com.skepticalone.mecachecker.db.entity.CrossCoverEntity;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

public class CrossCoverViewModel extends ItemViewModel<CrossCoverEntity> {

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
    public void deleteItem(long id) {
        crossCoverDao.deleteCrossCoverShift(id);
    }

    public void addRandomCrossCoverShift() {
        crossCoverDao.insertCrossCoverShift(DatabaseInitUtil.randomCrossCoverShift());
    }


    public void setDate(long id, @NonNull LocalDate date) {
        crossCoverDao.setDate(id, date);
    }

    public void setClaimed(long id, boolean claimed) {
        crossCoverDao.setClaimed(id, claimed ? DateTime.now() : null);
    }

    public void setPaid(long id, boolean paid) {
        crossCoverDao.setPaid(id, paid ? DateTime.now() : null);
    }
}
