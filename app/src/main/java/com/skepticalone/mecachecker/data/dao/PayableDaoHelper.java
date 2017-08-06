package com.skepticalone.mecachecker.data.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.util.MoneyConverter;

import java.math.BigDecimal;

public final class PayableDaoHelper {

    @NonNull
    private final CustomDao dao;
    @NonNull
    private final SupportSQLiteStatement setPaymentStatement, setClaimedStatement, setPaidStatement;

    PayableDaoHelper(@NonNull CustomDao dao) {
        this.dao = dao;
        setPaymentStatement = dao.getUpdateStatement(Contract.COLUMN_NAME_PAYMENT);
        setClaimedStatement = dao.getUpdateStatement(Contract.COLUMN_NAME_CLAIMED);
        setPaidStatement = dao.getUpdateStatement(Contract.COLUMN_NAME_PAID);
    }

    public final void setPaymentSync(long id, @NonNull BigDecimal payment){
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
        setClaimedOrPaidSync(id, claimed, setClaimedStatement);
    }

    public final void setPaidSync(long id, boolean paid) {
        setClaimedOrPaidSync(id, paid, setPaidStatement);
    }

}