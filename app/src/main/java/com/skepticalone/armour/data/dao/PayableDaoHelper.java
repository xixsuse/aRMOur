package com.skepticalone.armour.data.dao;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.util.MoneyConverter;

import org.threeten.bp.Instant;

import java.math.BigDecimal;

public final class PayableDaoHelper {

    @NonNull
    private final ItemDao dao;

    PayableDaoHelper(@NonNull ItemDao dao) {
        this.dao = dao;
    }

    public final void setPaymentSync(long id, @NonNull BigDecimal payment){
        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME_PAYMENT, MoneyConverter.moneyToCents(payment));
        dao.updateInTransaction(id, values);
    }

    private void setClaimedOrPaidSync(long id, @NonNull String columnName, boolean claimedOrPaid) {
        ContentValues values = new ContentValues();
        values.put(columnName, claimedOrPaid ? Instant.now().getEpochSecond() : null);
        dao.updateInTransaction(id, values);
    }

    public final void setClaimedSync(long id, boolean claimed) {
        setClaimedOrPaidSync(id, Contract.COLUMN_NAME_CLAIMED, claimed);
    }

    public final void setPaidSync(long id, boolean paid) {
        setClaimedOrPaidSync(id, Contract.COLUMN_NAME_PAID, paid);
    }

}