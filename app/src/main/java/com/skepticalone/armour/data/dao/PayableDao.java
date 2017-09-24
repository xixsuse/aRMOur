package com.skepticalone.armour.data.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.Instant;

import java.math.BigDecimal;

public interface PayableDao {
//
//    @NonNull
//    private final ItemDao dao;
//
//    PayableDao(@NonNull ItemDao dao) {
//        this.dao = dao;
//    }

    void setPaymentSync(long id, @NonNull BigDecimal payment);
//    {
//        ContentValues values = new ContentValues();
//        values.put(Contract.COLUMN_NAME_PAYMENT, MoneyConverter.moneyToCents(payment));
//        dao.updateInTransaction(id, values);
//    }
//
//    private void setClaimedOrPaidSync(long id, @NonNull String columnName, boolean claimedOrPaid) {
//        ContentValues values = new ContentValues();
//        values.put(columnName, claimedOrPaid ? Instant.now().getEpochSecond() : null);
//        dao.updateInTransaction(id, values);
//    }

    void setClaimedSync(long id, @Nullable Instant claimed);
//    {
//        setClaimedOrPaidSync(id, Contract.COLUMN_NAME_CLAIMED, claimed);
//    }

    void setPaidSync(long id, @Nullable Instant paid);
//    {
//        setClaimedOrPaidSync(id, Contract.COLUMN_NAME_PAID, paid);
//    }

}