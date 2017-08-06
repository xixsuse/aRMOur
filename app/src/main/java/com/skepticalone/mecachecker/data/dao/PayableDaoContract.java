//package com.skepticalone.mecachecker.data.dao;
//
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.annotation.WorkerThread;
//
//import org.joda.time.DateTime;
//
//import java.math.BigDecimal;
//
//@SuppressWarnings("NullableProblems")
//public interface PayableDaoContract<Entity> extends ItemDaoContract<Entity> {
//
//    @WorkerThread
//    void setPaymentSync(long id, @NonNull BigDecimal payment);
//
//    @WorkerThread
//    void setClaimedSync(long id, @Nullable DateTime claimed);
//
//    @WorkerThread
//    void setPaidSync(long id, @Nullable DateTime paid);
//
//}
