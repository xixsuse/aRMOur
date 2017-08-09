package com.skepticalone.armour.data.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.support.annotation.NonNull;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.util.MoneyConverter;

import java.math.BigDecimal;

public final class PayableDaoHelper {

    @NonNull
    private final ItemDao dao;

    PayableDaoHelper(@NonNull ItemDao dao) {
        this.dao = dao;
    }

    public final void setPaymentSync(long id, @NonNull BigDecimal payment){
        SupportSQLiteStatement setPaymentStatement = dao.getUpdateStatement(Contract.COLUMN_NAME_PAYMENT);
        setPaymentStatement.bindLong(1, MoneyConverter.moneyToCents(payment));
        setPaymentStatement.bindLong(2, id);
        dao.updateInTransaction(setPaymentStatement);
    }

    private void setClaimedOrPaidSync(long id, boolean claimedOrPaid, @NonNull SupportSQLiteStatement statement) {
        if (claimedOrPaid) statement.bindLong(1, System.currentTimeMillis());
        else statement.bindNull(1);
        statement.bindLong(2, id);
        dao.updateInTransaction(statement);
    }

    public final void setClaimedSync(long id, boolean claimed) {
        SupportSQLiteStatement setClaimedStatement = dao.getUpdateStatement(Contract.COLUMN_NAME_CLAIMED);
        setClaimedOrPaidSync(id, claimed, setClaimedStatement);
    }

    public final void setPaidSync(long id, boolean paid) {
        SupportSQLiteStatement setPaidStatement = dao.getUpdateStatement(Contract.COLUMN_NAME_PAID);
        setClaimedOrPaidSync(id, paid, setPaidStatement);
    }

}