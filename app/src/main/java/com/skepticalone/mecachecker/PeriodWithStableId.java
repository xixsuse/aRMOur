package com.skepticalone.mecachecker;

@SuppressWarnings("WeakerAccess")
class PeriodWithStableId extends PeriodWithComplianceData {
    private final long mId;

    public PeriodWithStableId(long startSeconds, long endSeconds, long id) {
        super(startSeconds, endSeconds);
        mId = id;
    }

    public long getId() {
        return mId;
    }

}
