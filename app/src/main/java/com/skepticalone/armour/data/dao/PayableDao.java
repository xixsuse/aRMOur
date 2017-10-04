package com.skepticalone.armour.data.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.Instant;

import java.math.BigDecimal;

@SuppressWarnings("NullableProblems")
public interface PayableDao {
//
//    @NonNull
//    private final ItemDao dao;
//
//    PayableDao(@NonNull ItemDao dao) {
//        this.dao = dao;
//    }

    @SuppressWarnings("EmptyMethod")
    void setPaymentSync(@SuppressWarnings("unused") long id, @SuppressWarnings("unused") @NonNull BigDecimal payment);
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

    @SuppressWarnings("EmptyMethod")
    void setClaimedSync(@SuppressWarnings("unused") long id, @SuppressWarnings("unused") @Nullable Instant claimed);
//    {
//        setClaimedOrPaidSync(id, Contract.COLUMN_NAME_CLAIMED, claimed);
//    }

    @SuppressWarnings("EmptyMethod")
    void setPaidSync(@SuppressWarnings("unused") long id, @SuppressWarnings("unused") @Nullable Instant paid);
//    {
//        setClaimedOrPaidSync(id, Contract.COLUMN_NAME_PAID, paid);
//    }

}