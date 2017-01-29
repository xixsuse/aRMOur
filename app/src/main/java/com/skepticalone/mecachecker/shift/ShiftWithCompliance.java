//package com.skepticalone.mecachecker.shift;
//
//import com.skepticalone.mecachecker.Shift;
//
//import java.util.ArrayList;
//import java.util.List;
//
//class ShiftWithCompliance extends Shift {
//
//    ShiftWithCompliance(long startSeconds, long endSeconds) {
//        super(startSeconds, endSeconds);
//    }
//
//    private List<String> mIssues = new ArrayList<>();
//
//    void addIssue(String issue){
//        mIssues.add(issue);
//    }
//
//    @Override
//    public String toString() {
//        if(mIssues.isEmpty()) return super.toString();
//        StringBuilder sb = new StringBuilder(super.toString());
//        for (String issue: mIssues){
//            sb.append("\n   --- issue: ").append(issue);
//        }
//        return sb.toString();
//    }
//}
