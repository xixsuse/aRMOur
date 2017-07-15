package com.skepticalone.mecachecker.data;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import com.skepticalone.mecachecker.ui.Constants;

abstract class Composition {

    private final Application application;

    Composition(Application application) {
        this.application = application;
    }

    Application getApplication() {
        return application;
    }

    final void runAsync(SQLiteTask task) {
        new DatabaseOperation(application).execute(task);
    }

    private static final class DatabaseOperation extends AsyncTask<SQLiteTask, Void, Void> {

        private final LocalBroadcastManager mBroadcastManager;

        DatabaseOperation(Application application) {
            super();
            mBroadcastManager = LocalBroadcastManager.getInstance(application);
        }

        @Override
        protected final Void doInBackground(SQLiteTask[] tasks) {
            for (SQLiteTask task : tasks) {
                try {
                    task.runSQLiteTask();
                } catch (ShiftOverlapException e) {
                    Intent intent = new Intent();
                    intent.setAction(Constants.DISPLAY_ERROR);
                    intent.putExtra(Intent.EXTRA_TEXT, e.getMessage());
                    mBroadcastManager.sendBroadcast(intent);
                }
            }
            return null;
        }

    }

}
