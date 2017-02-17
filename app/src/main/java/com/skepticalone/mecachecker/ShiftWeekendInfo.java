package com.skepticalone.mecachecker;

import android.support.annotation.Nullable;

public final class ShiftWeekendInfo {

    @Nullable
    Period currentWeekend = null;
    @Nullable
    Period lastWeekend = null;

    public void setCurrentWeekend(long start, long end) {
        currentWeekend = new Period(start, end);
    }

    public void setLastWeekend(long start, long end) {
        lastWeekend = new Period(start, end);
    }

    public final static class Period {
        public final long start;
        public final long end;

        public Period(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }

}
