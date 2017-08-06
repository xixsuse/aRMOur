package com.skepticalone.mecachecker.data.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.util.DateTimeConverter;
import com.skepticalone.mecachecker.data.util.MoneyConverter;

import org.joda.time.DateTime;

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

    private void setClaimedOrPaidSync(long id, @Nullable DateTime claimedOrPaid, @NonNull SupportSQLiteStatement statement) {
        Long millis = DateTimeConverter.dateTimeToMillis(claimedOrPaid);
        if (millis == null) statement.bindNull(1);
        else statement.bindLong(1, millis);
        statement.bindLong(2, id);
        dao.updateInTransaction(statement);
    }

    public final void setClaimedSync(long id, @Nullable DateTime claimed) {
        setClaimedOrPaidSync(id, claimed, setClaimedStatement);
    }

    public final void setPaidSync(long id, @Nullable DateTime paid) {
        setClaimedOrPaidSync(id, paid, setPaidStatement);
    }

}