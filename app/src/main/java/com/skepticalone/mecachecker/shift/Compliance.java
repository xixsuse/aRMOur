//package com.skepticalone.mecachecker.shift;
//
//import android.database.Cursor;
//import android.util.Log;
//
//import com.skepticalone.mecachecker.ComplianceChecks;
//import com.skepticalone.mecachecker.Shift;
//
//import java.util.ArrayList;
//import java.util.List;
//
//final class Compliance {
//    private static final String TAG = "Compliance";
//
//    private List<Shift> mShifts = new ArrayList<>();
//
//    Compliance(Cursor data, int startColumnIndex, int endColumnIndex) {
//        if (data.moveToFirst()) {
//            do {
//                mShifts.add(new ShiftWithCompliance(data.getLong(startColumnIndex), data.getLong(endColumnIndex)));
//            } while (data.moveToNext());
//        }
//        ComplianceChecks.checkMaximumWorkHours(mShifts);
//        ComplianceChecks.checkMinimumRestHours(mShifts);
//        Log.i(TAG, toString());
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder("Shifts:\n");
//        for (int i = 0; i < mShifts.size(); i++){
//            Shift shift = mShifts.get(i);
//            sb.append(i + 1).append(": ").append(shift).append('\n');
//        }
//        return sb.toString();
//    }
//
//}
